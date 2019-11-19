package onlineJavaCode;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.EntityExistsException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.sql.DataSource;

import model.Cart;
import model.CartProduct;
import model.Category;
import model.Product;
import model.User;

/*
 * TODO
 * ERASE THIS COMMENT BEFORE DELIVERING
 * Current version written by PABLO
 *
 * -> Revise the static stuff
 *
 */

/**
 * Represents the DAO and provides a transparent access to the database.
 * 
 * @author Pablo Brox, Luis Ibañez, Cristina Dominguez, Álvaro Arbe
 * @version 0.1
 */

public class dataManager {
	
	//Persistence Entity Manager factory 
	EntityManagerFactory factory;
	
	//JDBC objects
	Context ctx;
	DataSource ds;

	
	/*
	 * AUXILIAR CONSTANTS FOR CATEGORY
	 */
	public final  int CATEGORY = 0;
	public final  int SUB_CATEGORY = 1;
	public final int S_SUBCATEGORY = 2;

	// Methods

	public dataManager() {
		//Create the entity manager factory
		factory = Persistence.createEntityManagerFactory("online_store");
		//Open JDBC
		openJDBC();
		
	}
	
	//JDBC METHODS
	/**
	 * Opens a JDBC connection to the database.
	 */
	
	private void openJDBC() {
		//Construct and connect to database using JDBC
		try{// Load Driver
			ctx = new InitialContext();
			ds = (DataSource) ctx.lookup("projectRSC");
		} catch(Exception e) {
				System.out.println("Error when connecting to the database ");
				e.printStackTrace();
		}
	}

	/**
	 * Used to retrieve one user using its mail
	 * 
	 * @return The requested user or null in case it does not exist
	 */

	public User getUser(String mail) {
		//Create Entity manager
		EntityManager em = factory.createEntityManager();
		User _ret;
		try {
			//Find the element
			_ret = em.find(User.class, mail);
		} finally {
			//Close EM
			em.close();
		}
		//Return the user
		return _ret;
	}

	/**
	 * Adds one user to the database.
	 * 
	 * @param toAdd user to add.
	 * @return true if the user did not exist, false otherwise (not inserted), in case the Mail was not in use
	 * 			it logs the error to the console.
	 */

	public boolean putUser(User toAdd) {
		//Return value
		boolean _ret = true;
		//Create entity manager
		EntityManager em = factory.createEntityManager();
		try {
			//Start transaction
			em.getTransaction().begin();
			//Insert user
			em.persist(toAdd);
			//Finishes transaction
			em.getTransaction().commit();
		} catch (Exception e) {
			//Log in case the entity did not exist
			if(!(e instanceof EntityExistsException)) e.printStackTrace();
			//Return false
			_ret = false;
		} finally {
			//Close connection
			em.close();
		}
		
		return _ret;
		
	}

	/**
	 * Deletes an user from the database.
	 * 
	 * @param User the user to delete.
	 * @return true if succeed, false if some error happened.
	 */

	public boolean deleteUser(User toDelete) {
		//Return value
		boolean _ret = true;
		//Create entity manager
		EntityManager em = factory.createEntityManager();
		try {
			//Start transaction
			em.getTransaction().begin();
			if (!em.contains(toDelete)) {
			    toDelete = em.merge(toDelete);
			}
			//Remove user
			em.remove(toDelete);
			//Finishes transaction
			em.getTransaction().commit();
		} catch (Exception e) {
			//Log the error
			e.printStackTrace();
			//Return false
			_ret = false;
		} finally {
			//Close entityManager
			em.close();
		}
		
		return _ret;
	}

	/**
	 * Updates an user, returns the synchronized entity.
	 * 
	 * @param u user to insert.
	 * @return Updated user or null in case of error
	 */

	public User updateUser(User u) {
		
		User ret;
		//Create entity manager
		EntityManager em = factory.createEntityManager();
		try {
			//Start transaction
			em.getTransaction().begin();
			//Merge user
			ret = em.merge(u);
			//Finishes transaction
			em.getTransaction().commit();
		} catch (Exception e) {
			//Log the error
			e.printStackTrace();
			ret = null;
		} finally {
			//Close EM
			em.close();
		}
		
		return ret;
	}

	/**
	 * Retrieves a product from the database using its key (product name and price)
	 * 
	 * @param id  product's id
	 * @return the requested product or null of it does not exist
	 */

	public Product getProduct(int id) {
		//Create Entity manager
		EntityManager em = factory.createEntityManager();
		Product _ret;
		try {
			//Find the element
			_ret = em.find(Product.class, id);
		} finally {
			//Close EM
			em.close();
		}
		//Return the product
		return _ret;
	}

	/**
	 * Inserts a product in the database
	 * 
	 * @param toAdd element to insert
	 * @return True if it does not exist (and inserted) false otherwise
	 */

	public boolean putProduct(Product toAdd) {
		//Create Entity manager
		EntityManager em = factory.createEntityManager();
		boolean _ret = true;
		try {
			//Start transaction
			em.getTransaction().begin();
			//Insert product
			em.persist(toAdd);
			//Finishes transaction
			em.getTransaction().commit();
		} catch (Exception e) {
			//Log in case the entity did not exist
			if(!(e instanceof EntityExistsException)) e.printStackTrace();
			//Return false
			_ret = false;
		} finally {
			em.close();
		}
		
		return _ret;
	}

	/**
	 * Deletes a product from the database
	 * 
	 * @param PROD  Product TO DELETE
	 * @return true if succeed, false if some error happened.
	 */

	public boolean deleteProduct(Product prod) {
		//Create Entity manager
		EntityManager em = factory.createEntityManager();
		boolean _ret = true;
		try {
			//Start transaction
			em.getTransaction().begin();
			if (!em.contains(prod)) {
				prod = em.merge(prod);
			}
			//Remove product
			em.remove(prod);
			//Finishes transaction
			em.getTransaction().commit();
		} catch (Exception e) {
			//Log the error
			e.printStackTrace();
			//Return false
			_ret = false;
		} finally {
			em.close();
		}
		
		return _ret;
		
	}

	/**
	 * Updates a Product, returns the synchronized entity.
	 * 
	 * @param p product to update.
	 * @return Updated product or null in case of error
	 */

	public Product updateProduct(Product p) {
		//Create Entity manager
		EntityManager em = factory.createEntityManager();
		Product ret;
		try {
			//Start transaction
			em.getTransaction().begin();
			//Merge product
			ret = em.merge(p);
			//Finishes transaction
			em.getTransaction().commit();
		} catch (Exception e) {
			//Log the error
			e.printStackTrace();
			ret =  null;
		} finally {
			em.close();
		}
		
		return ret;
	}

	/**
	 * @return The user list, null if any error has happened
	 */
	//Annotation used to avoid list checking warning
	@SuppressWarnings("unchecked") 
	public List<User> getAllUsers() {
		//Create Entity manager
		EntityManager em = factory.createEntityManager();
		//Result list
		List<User> ret = null;
		try {
			//Create and execute query
			Query query = em.createNamedQuery("User.findAll",User.class);
			ret = query.getResultList();
		} finally {
			em.close();
		}
		
		//Return list
		return ret;
	}
	
	/**
	 * Private function used to obtain all the product entities from a list of category ids
	 * @param catIDs, list of categories
	 * @return List of products or null if the input list was empty
	 */
	
	@SuppressWarnings("unchecked")
	private List<Product> getProdFromCat(List<Integer> catIDs){
		List<Product> _ret;
		//In case there are no ids in the list, null is retrieved to avoid errors
		if(catIDs == null || catIDs.size() == 0) return null;
		//DO JPA Query
		String qr = "SELECT p FROM Product p WHERE";
		for(int i = 0; i < catIDs.size(); ++i) qr += (" p.categoryBean.catID = " + catIDs.get(i) + ((i != (catIDs.size() - 1)) ? " OR": ""));
		
		//Retrieve Products from JPA
		EntityManager em = factory.createEntityManager();
		try {
			Query query = em.createQuery(qr);
			_ret = query.getResultList();
		} finally {
			em.close();
		}
		
		return _ret;	
	}

	/**
	 * Gets a list of products matching category in level.
	 * 
	 * @param category The category to obtain products from
	 * @param category level dataManager.CATEGORY, dataManager.SUB_CATEGORY,
	 *                 dataManager.S_SUBCATEGORY
	 * @return The requested list or null in case there are no products
	 */
	
	public List<Product> getPCategory(Category cat, int level) {
		
		//First obtain category list
		List<Integer> catID = null;
		//Change the query depending on the level
		switch (level) {
			case S_SUBCATEGORY:
				catID = new ArrayList<Integer>();
				//The only final category is the one asked
				catID.add(cat.getCatID());
				break;
			case SUB_CATEGORY:
				//Get all catID whose parent is the selected category
				catID = getIdFromQuery("SELECT catID FROM CATEGORY WHERE ParentID = " + cat.getCatID());
				break;
			case CATEGORY:
				//Get only nodes of depth 3
				String qr = "SELECT t3.catID FROM CATEGORY AS t1 LEFT JOIN CATEGORY AS t2 ON t2.ParentID = t1.catID";
				qr += " LEFT JOIN CATEGORY AS t3 ON t3.ParentID = t2.catID";
				qr += " LEFT JOIN CATEGORY AS t4 ON t4.ParentID = t3.catID WHERE t1.catID = ";
				qr += cat.getCatID();
				catID = getIdFromQuery(qr);
		}
		
		//Return the product list
		return getProdFromCat(catID);
		
	}

	/**
	 * Get a list of all products
	 * 
	 * @return The list of products, null in error case
	 */

	//Annotation used to avoid list checking warning
	@SuppressWarnings("unchecked") 
	public List<Product> getAllProducts() {
		//Create Entity manager
		EntityManager em = factory.createEntityManager();
		//Result list
		List<Product> ret = null;
		try {
			//Create and execute query
			Query query = em.createNamedQuery("Product.findAll",Product.class);
			ret = query.getResultList();
		} finally {
			em.close();
		}
		
		//Return list
		return ret;
	}

	/**
	 * Retrieve a list of products that have a name matching the given string.
	 * 
	 * @param prod String to search.
	 * @return List of products with a name matching prod null in case of error.
	 */
	//Annotation used to avoid list checking warning
	@SuppressWarnings("unchecked") 
	public List<Product> searchPByname(String prod) {
		//Create Entity manager
		EntityManager em = factory.createEntityManager();
		List<Product> _ret = null;
		try {
			//Create and execute query
			Query query = em.createNamedQuery("Product.findByName",Product.class);
			query.setParameter("searchText","%"+prod+"%");
			_ret = query.getResultList();
		} finally {
			em.close();
		}
		return _ret;
	}
	
	/**
	 * Used to retrieve one cart using its id
	 * 
	 * @return The requested cart or null in case it does not exist
	 */

	public Cart getCart(int id) {
		//Create Entity manager
		EntityManager em = factory.createEntityManager();
		Cart _ret;
		try {
			//Find the element
			_ret = em.find(Cart.class, id);
		} finally {
			//Close EM
			em.close();
		}
		//Return the Cart
		return _ret;
	}

	/**
	 * Adds one Cart to the database.
	 * 
	 * @param toAdd Cart to add.
	 * @return true if the user did not exist, false otherwise (not inserted), in case the ID was not in use
	 * 			it logs the error to the console.
	 */

	public boolean putCart(Cart toAdd) {
		//Create Entity manager
		EntityManager em = factory.createEntityManager();
		//Return boolean value
		boolean _ret = true;
		try {
			//Start transaction
			em.getTransaction().begin();
			//Insert cart
			em.persist(toAdd);
			//Finishes transaction
			em.getTransaction().commit();
		} catch (Exception e) {
			//Log in case the entity did not exist
			if(!(e instanceof EntityExistsException)) e.printStackTrace();
			//Return false
			_ret = false;
		} finally {
			em.close();
		}
		
		return _ret;
		
	}

	/**
	 * Deletes a Cart from the database.
	 * 
	 * @param Cart the user to delete.
	 * @return true if succeed, false if some error happened.
	 */

	public boolean deleteCart(Cart toDelete) {
		//Create Entity manager
		EntityManager em = factory.createEntityManager();
		//Return boolean value
		boolean _ret = true;
		try {
			//Start transaction
			em.getTransaction().begin();
			//Remove Cart
			em.remove(toDelete);
			//Finishes transaction
			em.getTransaction().commit();
		} catch (Exception e) {
			//Log the error
			e.printStackTrace();
			//Return false
			_ret = false;
		} finally {
			em.close();
		}
		
		return _ret;
	}

	/**
	 * Updates an user, returns the synchronized entity.
	 * 
	 * @param c Cart to update.
	 * @return Updated cart or null in case of error
	 */

	public Cart updateCart(Cart c) {
		//Create Entity manager
		EntityManager em = factory.createEntityManager();
		Cart ret;
		try {
			//Start transaction
			em.getTransaction().begin();
			//Merge user
			ret = em.merge(c);
			//Finishes transaction
			em.getTransaction().commit();
		} catch (Exception e) {
			//Log the error
			e.printStackTrace();
			ret = null;
		} finally {
			em.close();
		}
		
		return ret;
	}
	
	/**
	 * Adds one Cart_product to the database.
	 * 
	 * @param toAdd Cart_product to add.
	 * @return true if the Cart_product did not exist, false otherwise (not inserted), in case the ID was not in use
	 * 			it logs the error to the console.
	 */

	public boolean putCartEntry(CartProduct toAdd) {
		//Create Entity manager
		EntityManager em = factory.createEntityManager();
		//Get Cart
		Cart acart = toAdd.getCart();
		//Return boolean value
		boolean _ret = true;
		try {
			//Start transaction
			em.getTransaction().begin();
			//Change total value
			acart.setTotalOrder(acart.getTotalOrder()+toAdd.getQuantity()*toAdd.getProductBean().getPrice());
			//Merge cart
			em.merge(acart);
			//Insert CP
			em.persist(toAdd);
			//Finishes transaction
			em.getTransaction().commit();
		} catch (Exception e) {
			//Log in case the entity did not exist
			if(!(e instanceof EntityExistsException)) e.printStackTrace();
			//Return false
			_ret = false;
		} finally {
			em.close();
		}
		
		return _ret;
		
	}

	/**
	 * Deletes a Cart_product from the database.
	 * 
	 * @param toDelete the Cart_product to delete.
	 * @return true if succeed, false if some error happened.
	 */

	public boolean deleteCartEntry(CartProduct toDelete) {
		//Create Entity manager
		EntityManager em = factory.createEntityManager();
		//Get Cart
		Cart acart = toDelete.getCart();
		//Return boolean value
		boolean _ret = true;
		try {
			//Start transaction
			em.getTransaction().begin();
			//Update Cart value
			acart.setTotalOrder(acart.getTotalOrder()-toDelete.getProductBean().getPrice()*toDelete.getQuantity());
			//Merge the cart
			em.merge(acart);
			//Remove Cart Entry
			em.remove(toDelete);
			//Finishes transaction
			em.getTransaction().commit();
		} catch (Exception e) {
			//Log the error
			e.printStackTrace();
			//Return false
			_ret = false;
		} finally {
			em.close();
		}
		
		return _ret;
	}
	
	/**
	 * Updated the quantity of elements from a cart entry
	 * @param tmdf Cart_product to update
	 * @param newQuantity new quantity value, example: tmdf.getQuantity()-1;
	 * @return The updated entity in case of success, null in case the updated quantity is <= 0 (it is deleted) or error
	 */
	
	public CartProduct updateCartEntry(CartProduct tmdf, int newQuantity) {
		
		//Safety check, if new quantity is impossible or 0, entry is deleted
		if(newQuantity <= 0) {
			deleteCartEntry(tmdf); //This updates the quantity
			return null;
		}
		//Get uds difference, negative if new < old
		int unitsDiff = newQuantity - tmdf.getQuantity();
		//Update the given tmdf
		tmdf.setQuantity(newQuantity);
		//Create Entity manager
		EntityManager em = factory.createEntityManager();
		//Return value
		CartProduct ret;
		//Get cart
		Cart acart = tmdf.getCart();
		
		//Update in DB
		try {
			//Start transaction
			em.getTransaction().begin();
			//ChangeCart
			acart.setTotalOrder(acart.getTotalOrder() + tmdf.getProductBean().getPrice()*unitsDiff);
			//Merge Cart
			em.merge(acart);
			//Merge entry
			ret = em.merge(tmdf);
			//Finishes transaction
			em.getTransaction().commit();
		} catch (Exception e) {
			//Log the error
			e.printStackTrace();
			ret = null;
		} finally {
			em.close();
		}
		
		return ret;
	}
	
	/**
	 * Private function used to obtain all the category entities from a list of category ids
	 * @param catIDs list of Category ids to obtain
	 * @return List of category entities matchng id or null in case no categry is found
	 */
	@SuppressWarnings("unchecked")
	private List<Category> getCatFromID(List<Integer> catIDs){
		List<Category> _ret;
		//In case there are no ids in the list, null is retrieved to avoid errors
		if(catIDs.size() == 0) return null;
		//DO JPA Query
		String qr = "SELECT c FROM Category c WHERE";
		for(int i = 0; i < catIDs.size(); ++i) qr += (" c.catID = " + catIDs.get(i) + ((i != (catIDs.size() - 1)) ? " OR": ""));
		
		//Retrieve categories from JPA
		EntityManager em = factory.createEntityManager();
		try {
			Query query = em.createQuery(qr);
			_ret = query.getResultList();
		} finally {
			em.close();
		}
		
		return _ret;	
	}
	
	/**
	 * Private method to query JDBC categories and obtain a list of ID
	 * @param catQr query to execute
	 * @return list of resulting Category IDS
	 */
	
	private List<Integer> getIdFromQuery(String catQr){
		//Ret array
		ArrayList<Integer> _ret = new ArrayList<>();
		//Retrieve Data
		try {
			//Create connection
			Connection con = ds.getConnection();
			if(con == null) System.out.println("Error connection to dataSource");
			else {
				//Create statement
				Statement st = con.createStatement();
				//Get Parent IDS using query
				ResultSet rs = st.executeQuery(catQr);
				//Obtain array with ids
				while(rs.next()) _ret.add(rs.getInt("catID"));
				//Close JDBC
				st.close();
				con.close();
			}
			
		} catch (Exception e) {
			System.out.print("JDBC ERROR Retrieving parent categories");
			e.printStackTrace();
		}
		
		return _ret;
	}
	
	/**
	 * Gets a list of parent categories
	 * @return List of categories with no parent, null in case no categories found
	 */
	public List<Category> getParentCategories(){
		
		//Call the method to obtain all the entities with the method to obtain the IDS
		return getCatFromID(getIdFromQuery("SELECT catID FROM CATEGORY WHERE ParentID IS NULL"));
		
	}
	
	/**
	 * Get a list of all the sub categories from a given one
	 * 
	 * @param parent Parent category to obtain childs
	 * @return Inmediate Child categories or null in case the category has no childs
	 */
	
	public List<Category> getSubCategories(Category parent){
		
		//Call the method to obtain all the entities with the method to obtain the IDS
		return getCatFromID(getIdFromQuery("SELECT catID FROM CATEGORY WHERE ParentID = " + parent.getCatID()));
		
	}

}

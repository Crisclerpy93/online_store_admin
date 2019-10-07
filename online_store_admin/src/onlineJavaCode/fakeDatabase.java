package onlineJavaCode;

import java.util.ArrayList;

/**
 * Fake database to deploy the first part of the application.
 * @author Pablo Brox, Luis Ibañez, Cristina Dominguez, Álvaro Arbe
 * @version 0.1
 */
public class fakeDatabase {
	
	//ArrayList of users
	private ArrayList<user> userList;
	//ArrayList of products
	private ArrayList<product> productList;
	
	
	/**
	 * Constructor: Initializates the database inserting some fake records for testing
	 */
	
	public fakeDatabase() {
		
		//Create arrays
		userList = new ArrayList<user>();
		productList = new ArrayList<product>();
		
		user user1 =  new user("Pedro", "Martinez", "651669897", "Calle uc3m", "pepe@mail", "1234", null, false);
		user user2 =  new user("Alicia", "Martinez", "651269897", "Avenida uc3m", "ali@mail", "1264", null, true);
		user user3 =  new user("Juan", "Martinez", "651669797", "Calle Sabatini", "juan@mail", "2234", null, false);
		user user4 =  new user("Paco", "Martinez", "651669497", "Calle ucm", "paco@mail", "1244", null, false);
		user user5 =  new user("Lucía", "Martinez", "651659897", "Calle Quevedo", "luci@mail", "1434", null, true);
		user user6 =  new user("Luis", "Martinez", "651663897", "Calle Sol", "luis@mail", "1254", null, false);
		user user7 =  new user("Álvaro", "Martinez", "651669897", "Calle Luna", "alvaro@mail", "1334", null, false);
		user user8 =  new user("Pablo", "Martinez", "651679897", "Calle Estrella", "pablo@mail", "1264", null, true);
		user user9 =  new user("Cristina", "Martinez", "651689897", "Calle Galaxia", "cris@mail", "1224", null, false);
		user user10 =  new user("Alejandra", "Martinez", "631669897", "Calle Cometa", "alex@mail", "1284", null, false);
		user user11 =  new user("Aitor", "Martinez", "651359897", "Calle Planeta", "aitor@mail", "1454", null, false);
		
		userList.add(user1);
		userList.add(user2);
		userList.add(user3);
		userList.add(user4);
		userList.add(user5);
		userList.add(user6);
		userList.add(user7);
		userList.add(user8);
		userList.add(user9);
		userList.add(user10);
		userList.add(user11);
		
		
		//Insert some products
		product product1 = new product("televisión",789 , "Tele", "Televisión",null , "tecnología", "hogar", "electrodoméstico", 3, user2);
		product product2 = new product("camiseta",50 , "cami", "Camiseta",null , "moda", "ropa", "parte de arriba", 2, user5);
		product product3 = new product("zapatillas",80 , "zapas", "Zapatillas",null , "ropa", "hogar", "calzado", 4, user8);
		product product4 = new product("tenedor",7 , "tene", "Tenedor",null , "cocina", "hogar", "cubertería", 3, user8);
		product product5 = new product("dvd",20 , "dvd", "Dvd",null , "tecnología", "hogar", "electrodoméstico", 7, user5);
		product product6 = new product("movil",1400 , "movil", "Movil",null , "tecnología", "hogar", "electrodoméstico", 20, user2);
		product product7 = new product("blueray",9 , "blue", "Blueray",null , "tecnología", "hogar", "electrodoméstico", 3, user5);
		product product8 = new product("plato", 10, "plato", "plato llano",null , "cocina", "hogar", "menaje", 10, user5);
		product product9 = new product("mantel",8 , "mantel", "mantel cuadros",null , "cocina", "hogar", "menaje", 21, user2);
		product product10 = new product("cuchara",8 , "cuchara", "cuchara de meta",null , "cocina", "hogar", "cubertería", 7, user8);
		
		productList.add(product1);
		productList.add(product2);
		productList.add(product3);
		productList.add(product4);
		productList.add(product5);
		productList.add(product6);
		productList.add(product7);
		productList.add(product8);
		productList.add(product9);
		productList.add(product10);
	}
	
	/**
	 * Gets an user from the database.
	 * @param mail Email to search
	 * @return The requested user if exist or null otherwise
	 */
	
	public user retrieveUserByMail(String mail) {
		int i = 0;
		//As the user mail must be unique, look for the first match
		while(i < userList.size() && !userList.get(i).getMail().equals(mail)) ++i;
		//Return the user in case it is found or null otherwise
		return i < userList.size() ? userList.get(i) : null;
	}
	
	/**
	 * Inserts a new user in the database.
	 * @param toInsert the new user to insert.
	 * @return true if the insertion is correct or false if the email was already registered
	 */
	
	public boolean insertUser(user toInsert) {
		//If the user already exists return false
		if(retrieveUserByMail(toInsert.getMail()) != null) return false;
		//In other case insert
		userList.add(toInsert);
		return true;
		
	}
	
	
	/**
	 * Deletes an user from the database.
	 * @param mail Primary key to delete.
	 * @return true if the record is found and removed, false otherwise
	 */
	
	public boolean deleteUserRecord(String mail) {
		int i = 0;
		//As the user mail must be unique, look for the first match
		while(i < userList.size() && !userList.get(i).getMail().equals(mail)) ++i;
		//If the record does not exist return false
		if(i == userList.size()) return false;
		//Delete the record
		userList.remove(i);
		return true;
	}
	
	/**
	 * Inserts the user and if it exists overwrites it.
	 * @param u User to insert.
	 */
	
	public void insertOrUpdateUser(user u) {
		int i = 0;
		//As the user mail must be unique, look for the first match
		while(i < userList.size() && !userList.get(i).getMail().equals(u.getMail())) ++i;
		//If the user already exists update 
		if(i < userList.size()) userList.set(i,  u);
		//Otherwise update
		else userList.add(u);
	}
	
	/**
	 * Get a list of all users (SELECT * FROM USERS)
	 * @return The user list
	 */
	public ArrayList<user> getAllUsers(){
		return userList;
	}
	
	/**
	 * Retrieves a product form the database by key (name,price)
	 * @param name Product name
	 * @param price Product price
	 * @return requested product
	 */
	
	public product retrieveProductByKey(String name, float price) {
		int i = 0;
		//As the product name and price must be unique, look for the first match
		while(i < userList.size() && !productList.get(i).getName().equals(name) && productList.get(i).getPrice() != price ) ++i;
		//Return the user in case it is found or null otherwise
		return i < productList.size() ? productList.get(i) : null;
	}
	
	/**
	 * Inserts a new product in the database.
	 * @param toInsert the new product to insert.
	 * @return true if the insertion is correct or false if the product was already registered
	 */
	
	public boolean insertProduct(product toInsert) {
		//If the user already exists return false
		if(retrieveProductByKey(toInsert.getName(), toInsert.getPrice()) != null) return false;
		//In other case insert
		productList.add(toInsert);
		return true;
		
	}
	
	/**
	 * Deletes a product from the database
	 * @param name product's name
	 * @param price product's price
	 * @return true if the record exists and it is deleted, false otherwise
	 */
	
	public boolean deleteProductRecord(String name, float price) {
		int i = 0;
		//As the product name and price must be unique, look for the first match
		while(i < productList.size() && !productList.get(i).getName().equals(name) && productList.get(i).getPrice() != price) ++i;
		//If the record does not exist return false
		if(i == productList.size()) return false;
		//Delete the record
		productList.remove(i);
		return true;
	}
	
	/**
	 * Forces a product insertion, if the key (name,price) was already used, it is overwritten.
	 * @param p product to insert.
	 */
	
	public void insertOrUpdateProduct(product p) {
		int i = 0;
		//As the product name and price must be unique, look for the first match
		while(i < productList.size() && !productList.get(i).getName().equals(p.getName()) && productList.get(i).getPrice() != p.getPrice()) ++i;
		//If the product already exists update 
		if(i < productList.size()) productList.set(i,  p);
		//Otherwise update
		else productList.add(p);
	}

	/**
	 * Gets a list of products matching category in level.
	 * @param category Category 
	 * @param category level product.CATEGORY, product.SUB_CATEGORY, product.S_SUBCATEGORY
	 * @return the list
	 */
	public ArrayList<product> getPBCategory(String category, int level){
		ArrayList<product> _ret = new ArrayList<>();
		for(int i = 0; i < productList.size(); ++i) if(productList.get(i).getCategories()[level].equals(category)) _ret.add(productList.get(i));
		return _ret;
	}
	
	/**
	 * Gets a list of products added by selected seller
	 * @param seller Selected seller.
	 * @return  List containing all selected products
	 */
	public ArrayList<product> getPBSeller(user seller){
		ArrayList<product> _ret = new ArrayList<>();
		for(int i = 0; i < productList.size(); ++i) if(productList.get(i).getSeller().equals(seller)) _ret.add(productList.get(i));
		return _ret;
	}
	
	/**
	 * Get a list of all products (SELECT * FROM PRODUCTS)
	 * @return The list of products
	 */
	
	public ArrayList<product> getAllProducts(){
		return productList;
	}
	
	
	

}


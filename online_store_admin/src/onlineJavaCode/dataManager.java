package onlineJavaCode;

import java.util.ArrayList;

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
 * @author Pablo Brox, Luis Ibañez, Cristina Dominguez, Álvaro Arbe
 * @version 0.1
 */

public class dataManager {
	
	//FakeDatabase class that implements a temporal database using ArrayLists
	private static fakeDatabase dbase;
	
	//Methods
	
	public dataManager() {
		//Connect to the database only if there is not an instance
		if(dbase == null) dbase = new fakeDatabase();
	}
	
	public administrator getAdmin(String mail){
		//Do the query -- First delivery extract admin using fakeDB method
		return dbase.retrieveAdminByMail(mail);
	}
	
	/**
	 * Used to retrieve one user using its mail
	 * @return The requested user or null in case it does not exist
	 */
	
	public user getUser(String mail){
		//Do the query -- First delivery extract user using fakeDB method
		return dbase.retrieveUserByMail(mail);
	}
	
	/**
	 * Adds one user to the database.
	 * @param toAdd user to add.
	 * @return true if the user did not exist, false otherwise (not inserted)
	 */
	
	public boolean putUser(user toAdd) {
		//Do the query -- First delivery insert user using fakeDB method
		return dbase.insertUser(toAdd);
	}
	
	/**
	 * Deletes an user from the database.
	 * @param mail The mail (primary key) of the user to delete.
	 * @return true if succeed, false if it did not exist.
	 */
	
	public boolean deleteUser(String mail) {
		//Do the query -- First delivery insert user using fakeDB method
		return dbase.deleteUserRecord(mail);
	}
	
	/**
	 * Inserts an user, if it exists it is overwrited. 
	 * @param u user to insert.
	 */
	
	public void forceUserInsert(user u) {
		//Do the query -- First delivery insert user using fakeDB method
		dbase.insertOrUpdateUser(u);
	}
	
	
	
	/**
	 * Retrieves a product from the database using its key (product name and price)
	 * @param name product's name
	 * @param price product's price
	 * @return the requested product or null of it does not exist
	 */
	
	public product getProduct(String name, float price) {
		//Do the query -- First delivery insert user using fakeDB method
		return dbase.retrieveProductByKey(name, price);
	}
	
	/**
	 * Inserts a product in the database
	 * @param toAdd element to insert
	 * @return True if it does not exist (and inserted) false otherwise
	 */
	
	public boolean putProduct(product toAdd) {
		//Do the query -- First delivery insert user using fakeDB method
		return dbase.insertProduct(toAdd);
	}
	
	/**
	 * Deletes a product from the database
	 * @param name Product name
	 * @param price Product price
	 * @return True if the product exists (and it is deleted) false otherwise.
	 */
	
	public boolean deleteProduct(String name, float price) {
		//Do the query -- First delivery insert user using fakeDB method
		return dbase.deleteProductRecord(name, price);
	}
	
	/**
	 * Inserts product, if it exists it is overwrites. 
	 * @param p product to insert
	 */
	public void forceProductInsert(product p) {
		//Do the query -- First delivery insert user using fakeDB method
		dbase.insertOrUpdateProduct(p);
	}
	
	/**
	 * @return The user list
	 */
	public ArrayList<user> getAllUsers(){
		//Do the query -- First delivery insert user using fakeDB method
		return dbase.getAllUsers();
	}
	
	/**
	 * Gets a list of products matching category in level.
	 * @param category Category 
	 * @param category level product.CATEGORY, product.SUB_CATEGORY, product.S_SUBCATEGORY
	 * @return The requested list 
	 */
	
	public ArrayList<product> getPCategory(String category, int level){
		//Do the query -- First delivery insert user using fakeDB method
		return dbase.getPBCategory(category, level);
	}
	
	/**
	 * Gets a list of products added by selected seller
	 * @param seller Selected seller.
	 * @return  List containing all selected products
	 */
	public ArrayList<product> getPSeller(user seller){
		//Do the query -- First delivery insert user using fakeDB method
		return dbase.getPBSeller(seller);
	}
	
	/**
	 * Get a list of all products 
	 * @return The list of products
	 */
	
	public ArrayList<product> getAllProducts(){
		//Do the query -- First delivery insert user using fakeDB method
		return dbase.getAllProducts(); 
	}
	
	/**
	 * Retrieve a list of products that have a name matching the given string, in lowercase.
	 * @param prod String to search.
	 * @return List of products with a name matching prod.
	 */
	
	public ArrayList<product> searchPByname(String prod){
		ArrayList<product> _ret = new ArrayList<>();
		//Get all products
		ArrayList<product> pList = dbase.getAllProducts();
		//Add products that matches the name
		for(int i = 0; i < pList.size(); ++i) if(pList.get(i).getName().toLowerCase().contains(prod.toLowerCase())) _ret.add(pList.get(i));
		return _ret;
	}
	
	
	
}

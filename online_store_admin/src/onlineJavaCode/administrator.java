package onlineJavaCode;

import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;

public class administrator {
	
	private String name;
	private String mail;
	private String passHash;
	private ArrayList<product> productlist;
	private ArrayList<user> userlist;

	administrator(String name, String mail, String pass){
		this.name = name;
		this.mail = mail;
		this.passHash = DigestUtils.sha256Hex(pass);
		this.productlist = new ArrayList<product>();
		this.userlist = new ArrayList<user>();
	}
	
	public String getName() {
		return name;
	}
	
	public String getMail() {
		return mail;
	}
	
	public boolean checkPass(String pass) {
		String hash =  DigestUtils.sha256Hex(pass);
		return passHash.equals(hash);
	}
	
	/**
	 * @param name of products list
	*/
	public void setProducts(ArrayList<product> list){
		this.productlist = list;
	} 
	
	/**
	 * @return productlist
	*/
	public ArrayList<product> getProducts(){
		//Return the set as an array (more easy to manage)
		ArrayList<product>_ret = new ArrayList<product>();
		_ret.addAll(productlist);
		return _ret;
	} 
	
	/**
	 * Adds a product to the list
	 * @param toAdd Product to add
	 * @return True if the product was not in the list (thus it is added) or false otherwise (not inserting duplicates).
	
	public boolean addProduct(product toAdd) {
		//Adds the product to the set, fails if it is already in
		return productlist.add(toAdd);
	} */
	
	/**
	 * Deletes an product from the list. 
	 * NOTE: to obtain the product using name and price look for it in the database before.
	 * @param toDelete product to delete
	 * @return true if the product was found and deleted
	*/
	public boolean deleteProduct(product toDelete) {
		//Delete from the set
		return productlist.remove(toDelete);
	} 
	
	/**
	 * @param name of users list
	*/
	public void setUsers(ArrayList<user> list){
		this.userlist = list;
	} 
	
	/**
	 * @return userlist
	*/
	public ArrayList<user> getUsers(){
		//Return the set as an array (more easy to manage)
		ArrayList<user>_ret = new ArrayList<user>();
		_ret.addAll(userlist);
		return _ret;
	} 
	
	/**
	 * Adds an user to the list
	 * @param toAdd user to add
	 * @return True if the user was not in the list (thus it is added) or false otherwise (not inserting duplicates).
	
	public boolean addUser(user toAdd) {
		//Adds the product to the set, fails if it is already in
		return userlist.add(toAdd);
	} */
	
	/**
	 * Deletes an user from the list. 
	 * NOTE: to obtain the user using mail look for it in the database before.
	 * @param toDelete user to delete
	 * @return true if the user was found and deleted
	 */
	public boolean deleteUser(user toDelete) {
		//Delete from the set
		return userlist.remove(toDelete);
	} 
}

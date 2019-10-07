package onlineJavaCode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.codec.digest.DigestUtils;

import javafx.util.Pair;


/*
 * TODO
 * ERASE THIS COMMENT BEFORE DELIVERING
 * Current version written by PABLO
 * 
 * -> Password updater method
 */

/**
 * Represents an user.
 * @author Pablo Brox, Luis Ibañez, Cristina Dominguez, Álvaro Arbe
 * @version 0.1
 */
public class user {
	
	//User fields
	private String name;
	private String surname;
	private String phone;
	private String address;
	private String mail;
	private String passHash;
	private String imagePath;
	private boolean isSeller;
	
	//WhishList and cart
	private Set<product> whishlist;
	private Pair<ArrayList<product>, ArrayList<Integer>> ShoppingCart;
	

	//Constructor 
	user(String uName, String uSurname, String uPhone, String uAddr, String uMail, String uPass, String uPath, boolean uSell) {
		
		//Set fields
		name = uName;
		surname = uSurname;
		phone = uPhone;
		address = uAddr;
		mail = uMail;
		imagePath = uPath;
		isSeller = uSell;
		
		//Compute password hash
		passHash =  DigestUtils.sha256Hex(uPass);
		
		//Create lists
		whishlist = new HashSet<product>();
		ShoppingCart = new Pair<ArrayList<product>, ArrayList<Integer>>(new ArrayList<product>(), new ArrayList<Integer>());
		
	}
	
	//Setters and getters 
	
	/**
	 * @param pass password to check
	 * @return True if the password is correct false otherwise
	 */
	public boolean checkPass(String pass) {
		
		String hash =  DigestUtils.sha256Hex(pass);
		return passHash.equals(hash);
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the surname
	 */
	public String getSurname() {
		return surname;
	}

	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		this.surname = surname;
	}

	/**
	 * @return the username
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param username the username to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the user's address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the user's mail
	 */
	public String getMail() {
		return mail;
	}

	/**
	 * @param mail the mail to set
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/**
	 * @return the user's imagePath
	 */
	public String getImagePath() {
		return imagePath;
	}

	/**
	 * @param imagePath the imagePath to set
	 */
	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	/**
	 * @return the isSeller
	 */
	public boolean isSeller() {
		return isSeller;
	}

	/**
	 * @param isSeller true to change the role to seller and false to revoke seller privileges
	 */
	public void setSeller(boolean isSeller) {
		this.isSeller = isSeller;
	}
	
	/**
	 * @return user's shopping cart, key is products and value i¡units
	 */
	public Pair<ArrayList<product>, ArrayList<Integer>> getShopping(){
		return ShoppingCart;
	}
	
	/**
	 * Adds one product to the shopping cart, if the product exists, just adds new units
	 * @param toAdd product to add
	 * @param units to add
	 */
	
	public void addProductCart(product toAdd, int units) {
		//Search for product in cart
		int i = 0;
		while(i < ShoppingCart.getKey().size() && /*Product ID*/toAdd.getName().equals(ShoppingCart.getKey().get(i).getName()) &&
				toAdd.getPrice() == ShoppingCart.getKey().get(i).getPrice()) ++i;
		//If found Add the selected units
		if(i < ShoppingCart.getKey().size()) ShoppingCart.getValue().set(i,  ShoppingCart.getValue().get(i)+units);
		else {
			//Add one element
			ShoppingCart.getKey().add(toAdd);
			//Add the selected units
			ShoppingCart.getValue().add(units);
		}
	}
	
	/**
	 * Decreases the specified units from a product ,deleting it if no units are left on the cart
	 * @param name Product name
	 * @param price Product price
	 * @param units Units to decrease
	 * @return units left, 0 if deleted or -1 if the product did not exist
	 */
	public int decreaseUnitsCart(String name, float price, int units) {
		//Search for product in cart
		int i = 0;
		while(i < ShoppingCart.getKey().size() && /*Product ID*/name.equals(ShoppingCart.getKey().get(i).getName()) &&
					price == ShoppingCart.getKey().get(i).getPrice()) ++i;
		//Case not found, return -1
		if(i == ShoppingCart.getKey().size()) return -1;
		//Case found, decrease units
		int nUnits = ShoppingCart.getValue().get(i) - units;
		//If units are less than 0 (or 0) remove 
		if(nUnits <= 0) {
			ShoppingCart.getKey().remove(i);
			ShoppingCart.getValue().remove(i);
			return 0;
		}
		//In other case add the number of units left
		return ShoppingCart.getValue().set(i, nUnits);
	}
	
	/**
	 * Deletes a product from the shipping cart.
	 * @param name Product name.
	 * @param price Product price.
	 * @return True if the product is found (and deleted) false otherwise.
	 */
	
	public boolean deleteProductCart(String name, float price) {
		//Search for product in cart
		int i = 0;
		while(i < ShoppingCart.getKey().size() && /*Product ID*/name.equals(ShoppingCart.getKey().get(i).getName()) &&
					price == ShoppingCart.getKey().get(i).getPrice()) ++i;
		//Case not found, return false
		if(i == ShoppingCart.getKey().size()) return false;
		//If found delete
		ShoppingCart.getKey().remove(i);
		ShoppingCart.getValue().remove(i);
		return true;
	}
	
	/**
	 * @return user's whishlist
	 */
	public ArrayList<product> getWhish(){
		//Return the set as an array (more easy to manage)
		ArrayList<product>_ret = new ArrayList<product>();
		_ret.addAll(whishlist);
		return _ret;
	}
	
	/**
	 * Adds a product to the user's whishlist
	 * @param toAdd Product to add
	 * @return True if the product was not in the list (thus it is added) or false otherwise (not inserting duplicates).
	 */
	public boolean addProductWhish(product toAdd) {
		//Adds the product to the set, fails if it is already in
		return whishlist.add(toAdd);
	}
	
	/**
	 * Deletes an product from the user's whishlist. 
	 * NOTE: to obtain the product using name and price look for it in the database before.
	 * @param toDelete product to delete
	 * @return true if the product was found and deleted
	 */
	public boolean deleteProductWhish(product toDelete) {
		//Delete from the set
		return whishlist.remove(toDelete);
	}
	
	
	
	
	
	
	
	
	
	
	

}

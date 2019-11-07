package onlineJavaCode;

/** 
 * Represents a product.
 * @author Pablo Brox, Luis Ibañez, Cristina Dominguez, Álvaro Arbe
 * @version 0.1
 */

public class product {
	
	/*
	 * AUXILIAR CONSTANTS FOR CATEGORY
	 */
	public final static int CATEGORY = 0;
	public final static int SUB_CATEGORY = 1;
	public final static int S_SUBCATEGORY = 2;
	
	//Product fields
	//private final String name; //Name cannot be changed TODO: Review this
	private String name;
	private float price;
	private String shortDesc;
	private String longDesc;
	private String imagePath;
	private String[] categories;
	private int stock; 
	private user seller;
	
	
	/**
	 * Constructor
	 * @param pName Product name
	 * @param pPrice Product price, in euro
	 * @param sDesc Product short description
	 * @param lDesc Product long description
	 * @param iPath Product image path
	 * @param bCat Product big category
	 * @param mCat Product sub category
	 * @param sCat Product sub sub category
	 * @param pStock Product stock, in units
	 * @param pSeller User that added the product
	 */
	product(String pName, float pPrice, String sDesc, String lDesc, String iPath, String bCat, String mCat, String sCat, int pStock, user pSeller){
		
		name = pName;
		price = pPrice;
		shortDesc = sDesc;
		longDesc = lDesc;
		imagePath = iPath;
		categories = new String[] {bCat, mCat, sCat};
		stock = pStock;
		seller = pSeller;
	}
	
	//Setters and getters
	

	/**
	 * @return the price
	 */
	public float getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(float price) {
		this.price = price;
	}

	/**
	 * @return the shortDesc
	 */
	public String getShortDesc() {
		return shortDesc;
	}

	/**
	 * @param shortDesc the shortDesc to set
	 */
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	/**
	 * @return the longDesc
	 */
	public String getLongDesc() {
		return longDesc;
	}

	/**
	 * @param longDesc the longDesc to set
	 */
	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}

	/**
	 * @return the imagePath
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
	 * @return the seller
	 */
	public user getSeller() {
		return seller;
	}

	/**
	 * @param seller the seller to set
	 */
	public void setSeller(user seller) {
		this.seller = seller;
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
	 * @return the categories
	 */
	public String[] getCategories() {
		return categories;
	}
	
	/**
	 * Sets a new category in the selected level
	 * @param level product.CATEGORY, product.SUB_CATEGORY or product.S_SUBCATEGORY respectively for each level
	 * @param newCat the new category name
	 * TODO -> Category organization
	 */
	public void setCategory(int level, String newCat) {
		//Safety check, cannot work with out of bound levels
		if(level < 0 || level > 2) return;
		categories[level] = newCat; 
	}

	/**
	 * @return the stock
	 */
	public int getStock() {
		return stock;
	}
	
	/**
	 * @param the stock to set
	 */
	public void setStock(int stock) {
		this.stock = stock;
	}
	
	/**
	 * Decreases the product units by one, in case 
	 * @return Remaining number of units or -1 if there are not units left
	 */
	
	public int doSale() {
		
		return stock!=0 ? stock-- : -1;
	}
	
	/**
	 * Adds n elements to the product stock
	 * @param units Number of units to add
	 * @return The resulting stock
	 */
	public int addStock(int units) {
		return stock += units;
	}

}


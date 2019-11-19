package entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the Products database table.
 * 
 */
@Entity
@Table(name = "Products")
@NamedQuery(name = "Product.findAll", query = "SELECT p FROM Product p")
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "Id")
	private int id;

	@Lob
	@Column(name = "Image")
	private byte[] image;

	@Lob
	private String longDesc;

	@Column(name = "Name")
	private String name;

	@Column(name = "Price")
	private float price;

	private String shortDesc;

	@Column(name = "Stock")
	private int stock;

	// bi-directional many-to-one association to Category
	@ManyToOne
	@JoinColumn(name = "Category")
	private Category categoryBean;

	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "Seller")
	private User user;

	// bi-directional many-to-many association to Cart
	@ManyToMany(mappedBy = "products")
	private List<Cart> carts;

	// bi-directional many-to-many association to User
	@ManyToMany(mappedBy = "products2")
	private List<User> users;

	public Product() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public byte[] getImage() {
		return this.image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getLongDesc() {
		return this.longDesc;
	}

	public void setLongDesc(String longDesc) {
		this.longDesc = longDesc;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public float getPrice() {
		return this.price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getShortDesc() {
		return this.shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public int getStock() {
		return this.stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public Category getCategoryBean() {
		return this.categoryBean;
	}

	public void setCategoryBean(Category categoryBean) {
		this.categoryBean = categoryBean;
	}

	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<Cart> getCarts() {
		return this.carts;
	}

	public void setCarts(List<Cart> carts) {
		this.carts = carts;
	}

	public List<User> getUsers() {
		return this.users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}

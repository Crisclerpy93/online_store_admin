package entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the Users database table.
 * 
 */
@Entity
@Table(name = "Users")
@NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
public class User implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "Mail")
	private String mail;

	@Lob
	@Column(name = "Address")
	private String address;

	@Lob
	@Column(name = "Image")
	private byte[] image;

	private Object isSeller;

	@Lob
	@Column(name = "Name")
	private String name;

	private String passHash;

	@Column(name = "Phone")
	private String phone;

	@Lob
	@Column(name = "Surname")
	private String surname;

	// bi-directional many-to-one association to Cart
	@OneToMany(mappedBy = "userBean")
	private List<Cart> carts;

	// bi-directional many-to-one association to Product
	@OneToMany(mappedBy = "user")
	private List<Product> products1;

	// bi-directional many-to-many association to Product
	@ManyToMany
	@JoinColumn(name = "Mail")
	private List<Product> products2;

	public User() {
	}

	public String getMail() {
		return this.mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public byte[] getImage() {
		return this.image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public Object getIsSeller() {
		return this.isSeller;
	}

	public void setIsSeller(Object isSeller) {
		this.isSeller = isSeller;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassHash() {
		return this.passHash;
	}

	public void setPassHash(String passHash) {
		this.passHash = passHash;
	}

	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public List<Cart> getCarts() {
		return this.carts;
	}

	public void setCarts(List<Cart> carts) {
		this.carts = carts;
	}

	public Cart addCart(Cart cart) {
		getCarts().add(cart);
		cart.setUserBean(this);

		return cart;
	}

	public Cart removeCart(Cart cart) {
		getCarts().remove(cart);
		cart.setUserBean(null);

		return cart;
	}

	public List<Product> getProducts1() {
		return this.products1;
	}

	public void setProducts1(List<Product> products1) {
		this.products1 = products1;
	}

	public Product addProducts1(Product products1) {
		getProducts1().add(products1);
		products1.setUser(this);

		return products1;
	}

	public Product removeProducts1(Product products1) {
		getProducts1().remove(products1);
		products1.setUser(null);

		return products1;
	}

	public List<Product> getProducts2() {
		return this.products2;
	}

	public void setProducts2(List<Product> products2) {
		this.products2 = products2;
	}

}

package model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the CART_PRODUCTS database table.
 * 
 */
@Entity
@Table(name="CART_PRODUCTS")
@NamedQuery(name="CartProduct.findAll", query="SELECT c FROM CartProduct c")
public class CartProduct implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="Id")
	private String id;

	@Column(name="Quantity")
	private int quantity;

	//bi-directional many-to-one association to Cart
	@ManyToOne
	@JoinColumn(name="order")
	private Cart cart;

	//bi-directional many-to-one association to Product
	@ManyToOne
	@JoinColumn(name="Product")
	private Product productBean;

	public CartProduct() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public Cart getCart() {
		return this.cart;
	}

	public void setCart(Cart cart) {
		this.cart = cart;
	}

	public Product getProductBean() {
		return this.productBean;
	}

	public void setProductBean(Product productBean) {
		this.productBean = productBean;
	}

}
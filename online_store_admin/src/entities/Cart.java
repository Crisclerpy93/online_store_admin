package entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * The persistent class for the Carts database table.
 * 
 */
@Entity
@Table(name = "Carts")
@NamedQuery(name = "Cart.findAll", query = "SELECT c FROM Cart c")
public class Cart implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int orderID;

	@Temporal(TemporalType.DATE)
	@Column(name = "creation_date")
	private Date creationDate;

	@Column(name = "Order")
	private Object order;

	@Temporal(TemporalType.DATE)
	@Column(name = "order_date")
	private Date orderDate;

	@Column(name = "total_order")
	private float totalOrder;

	// bi-directional many-to-one association to User
	@ManyToOne
	@JoinColumn(name = "User")
	private User userBean;

	// bi-directional many-to-many association to Product
	@ManyToMany
	@JoinColumn(name = "orderID")
	private List<Product> products;

	public Cart() {
	}

	public int getOrderID() {
		return this.orderID;
	}

	public void setOrderID(int orderID) {
		this.orderID = orderID;
	}

	public Date getCreationDate() {
		return this.creationDate;
	}

	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	public Object getOrder() {
		return this.order;
	}

	public void setOrder(Object order) {
		this.order = order;
	}

	public Date getOrderDate() {
		return this.orderDate;
	}

	public void setOrderDate(Date orderDate) {
		this.orderDate = orderDate;
	}

	public float getTotalOrder() {
		return this.totalOrder;
	}

	public void setTotalOrder(float totalOrder) {
		this.totalOrder = totalOrder;
	}

	public User getUserBean() {
		return this.userBean;
	}

	public void setUserBean(User userBean) {
		this.userBean = userBean;
	}

	public List<Product> getProducts() {
		return this.products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

}
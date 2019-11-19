package entities;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;

/**
 * The persistent class for the Category database table.
 * 
 */
@Entity
@NamedQuery(name = "Category.findAll", query = "SELECT c FROM Category c")
public class Category implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private int catID;

	@Column(name = "Name")
	private String name;

	@Column(name = "ParentID")
	private int parentID;

	// bi-directional many-to-one association to Product
	@OneToMany(mappedBy = "categoryBean")
	private List<Product> products;

	public Category() {
	}

	public int getCatID() {
		return this.catID;
	}

	public void setCatID(int catID) {
		this.catID = catID;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getParentID() {
		return this.parentID;
	}

	public void setParentID(int parentID) {
		this.parentID = parentID;
	}

	public List<Product> getProducts() {
		return this.products;
	}

	public void setProducts(List<Product> products) {
		this.products = products;
	}

	public Product addProduct(Product product) {
		getProducts().add(product);
		product.setCategoryBean(this);

		return product;
	}

	public Product removeProduct(Product product) {
		getProducts().remove(product);
		product.setCategoryBean(null);

		return product;
	}


}

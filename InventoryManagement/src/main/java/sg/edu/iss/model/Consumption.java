package sg.edu.iss.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Positive;


@Entity
public class Consumption {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JoinColumn(name = "transactionId")
	private Transaction transaction;
	private int productId;
	@ManyToOne
	@JoinColumn(name = "product_productId")
	private Product product;
	@Positive(message="Must be more than zero")
	private int consumedQty;
	public Consumption() {
		super();
	}
	public Consumption(Transaction transaction, Product product, int consumedQty) {
		super();
		this.transaction = transaction;
		this.product = product;
		this.consumedQty = consumedQty;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Transaction getTransaction() {
		return transaction;
	}
	public void setTransaction(Transaction usage) {
		this.transaction = usage;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getConsumedQty() {
		return consumedQty;
	}
	public void setConsumedQty(int consumedQty) {
		this.consumedQty = consumedQty;
	}
	@Override
	public String toString() {
		return "Consumption [id=" + id + ", transaction=" + transaction + ", product=" + product + ", consumedQty="
				+ consumedQty + "]";
	}
}
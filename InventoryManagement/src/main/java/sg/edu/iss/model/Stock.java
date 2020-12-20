package sg.edu.iss.model;

import javax.persistence.Entity; 
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Stock {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@OneToOne
	@JoinColumn(name = "productId")
	private Product product;
	private int units;
	private StockStatus status;
	public Stock() {
		super();
	}
	public Stock(Product product, int units, StockStatus status) {
		super();
		this.product = product;
		this.units = units;
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Product getProduct() {
		return product;
	}
	public void setProduct(Product product) {
		this.product = product;
	}
	public int getUnits() {
		return units;
	}
	public void setUnits(int units) {
		this.units = units;
	}
	public StockStatus getStatus() {
		return status;
	}
	public void setStatus(StockStatus status) {
		this.status = status;
	}
	@Override
	public String toString() {
		return "Stock [id=" + id + ", product=" + product + ", units=" + units + ", status=" + status + "]";
	}

}


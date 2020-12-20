package sg.edu.iss.model;

import java.util.ArrayList; 
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Positive;
import javax.persistence.JoinColumn;


@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int productId;
	
	@NotEmpty(message="it's null!")
	@Pattern(regexp = "[a-zA-Z0-9 ]+", message="The product name should not include special characters")
	private String name;
	
	@NotEmpty(message="Please fill in product description ")
	@Pattern(regexp = "[a-zA-Z0-9 ]+", message="The product descripton should not include special characters")
	private String description;
	
	@NotEmpty(message="Please fill in product type")
	@Pattern(regexp = "[a-zA-Z0-9 ]+", message="The product type should not include special characters")
	private String type;
	
	@NotEmpty(message="Please fill in category")
	@Pattern(regexp = "[a-zA-Z0-9 ]+", message="The category should not include special characters")
	private String category;

	@NotEmpty(message="Please fill in sub-category")
	@Pattern(regexp = "[a-zA-Z0-9 ]+", message="The sub-category should not include special characters")
	private String subCategory;
	
	@NotNull(message="Please fill in price")
	@Positive(message="The price needs to be more than 0")
	@Min(value=0, message="Please enter numbers only")
	@Max(value=999999999, message="Please enter numbers only")
	private float originalPrice;
	
	@NotNull(message="Please fill in price")
	@Positive(message="The price needs to be more than 0")
	@Min(value=0, message="Please enter numbers only")
	@Max(value=999999999, message="Please enter numbers only")
	private float wholesalePrice;
	
	@NotNull(message="Please fill in price")
	@Positive(message="The price needs to be more than 0")
	@Min(value=0, message="Please enter numbers only")
	@Max(value=999999999, message="Please enter numbers only")
	private float retailPrice;
	
	@NotNull(message="Please fill in price")
	@Positive(message="The price needs to be more than 0")
	@Min(value=0, message="Please enter numbers only")
	@Max(value=999999999, message="Please enter numbers only")
	private float partnerPrice;
	
	@NotNull(message="Please fill in MOQ")
	@Min(value=1, message="Min Order Qty has to be more than zero")
	@Digits(fraction = 0, integer = 10, message ="Please enter whole number and not less than 0")
	private int MOQ;
	
	@NotNull(message="Please fill in MOQ")
	@Min(value=1, message="Reorder Qty has to be more than zero")
	@Digits(fraction = 0, integer = 10, message ="Please enter whole number and not less than 0")
	private int reorderQty;
	
	@OneToOne(mappedBy = "product", cascade = CascadeType.REMOVE)
	private Stock stock;
	
	private int supplierId;
	
	@ManyToOne
	@JoinColumn(name = "supplier_supplierId")
	private Supplier supplier;
	
	@OneToMany(mappedBy = "product")
	private List<Consumption> consumptions;

	@OneToMany(mappedBy = "product")
	private List<Reorder> reorders;
	
	private ProductStatus status;
	
	public Product() {
		super();
	}
	
	public Product(String name, String description, String type, String category, String subCategory,
			float originalPrice, float retailPrice, float wholesalePrice, float partnerPrice, int reorderQty, int mOQ,
			Supplier supplier) {
		super();
		this.name = name;
		this.description = description;
		this.type = type;
		this.category = category;
		this.subCategory = subCategory;
		this.originalPrice = originalPrice;
		this.retailPrice = retailPrice;
		this.wholesalePrice = wholesalePrice;
		this.partnerPrice = partnerPrice;
		this.reorderQty = reorderQty;
		MOQ = mOQ;
		this.supplier = supplier;
		consumptions = new ArrayList<Consumption>();
		reorders= new ArrayList<Reorder>();
		status = ProductStatus.IN_USE;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	public float getOriginalPrice() {
		return originalPrice;
	}

	public void setOriginalPrice(float originalPrice) {
		this.originalPrice = originalPrice;
	}

	public float getWholesalePrice() {
		return wholesalePrice;
	}

	public void setWholesalePrice(float wholesalePrice) {
		this.wholesalePrice = wholesalePrice;
	}

	public float getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(float retailPrice) {
		this.retailPrice = retailPrice;
	}

	public float getPartnerPrice() {
		return partnerPrice;
	}

	public void setPartnerPrice(float partnerPrice) {
		this.partnerPrice = partnerPrice;
	}

	public int getMOQ() {
		return MOQ;
	}

	public void setMOQ(int mOQ) {
		MOQ = mOQ;
	}

	public int getReorderQty() {
		return reorderQty;
	}

	public void setReorderQty(int reorderQty) {
		this.reorderQty = reorderQty;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

	public int getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(int supplierId) {
		this.supplierId = supplierId;
	}

	public Supplier getSupplier() {
		return supplier;
	}

	public void setSupplier(Supplier supplier) {
		this.supplier = supplier;
	}

	public List<Consumption> getConsumptions() {
		return consumptions;
	}

	public void setConsumptions(List<Consumption> consumptions) {
		this.consumptions = consumptions;
	}
	
	public void addConsumption(Consumption consumption) {
		consumptions.add(consumption);
	}

	public List<Reorder> getReorders() {
		return reorders;
	}

	public void setReorders(List<Reorder> reorders) {
		this.reorders = reorders;
	}
	
	public void addReorder(Reorder reorder) {
		reorders.add(reorder);
	}

	public ProductStatus getStatus() {
		return status;
	}

	public void setStatus(ProductStatus status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "Product [productId=" + productId + ", name=" + name + ", description=" + description + ", type=" + type
				+ ", category=" + category + ", subCategory=" + subCategory + ", originalPrice=" + originalPrice
				+ ", wholesalePrice=" + wholesalePrice + ", retailPrice=" + retailPrice + ", partnerPrice="
				+ partnerPrice + ", MOQ=" + MOQ + ", reorderQty=" + reorderQty + ", stock=" + stock + ", supplier=" 
				+ supplier + ", consumptions=" + consumptions + ", reorders=" + reorders
				+ ", status=" + status + "]";
	}

}

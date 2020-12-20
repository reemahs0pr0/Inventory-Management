package sg.edu.iss.model;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Car {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String brand;
	private String model;
	private String ownerName;
	private int ownerPhone;
	private String ownerEmail;
	public Car() {
		super();
	}
	public Car(String brand, String model, String ownerName, int ownerPhone, String ownerEmail) {
		super();
		this.brand = brand;
		this.model = model;
		this.ownerName = ownerName;
		this.ownerPhone = ownerPhone;
		this.ownerEmail = ownerEmail;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getOwnerName() {
		return ownerName;
	}
	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}
	public int getOwnerPhone() {
		return ownerPhone;
	}
	public void setOwnerPhone(int ownerPhone) {
		this.ownerPhone = ownerPhone;
	}
	public String getOwnerEmail() {
		return ownerEmail;
	}
	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}
	@Override
	public String toString() {
		return "Car: " + brand + " " + model + ", Owner: " + ownerName + ", Phone: "
				+ ownerPhone + ", Email: " + ownerEmail;
	}
}

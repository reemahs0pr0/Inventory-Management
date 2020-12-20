package sg.edu.iss.model;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.format.annotation.DateTimeFormat;

@Entity
public class Transaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private int carId;
	@ManyToOne
	@JoinColumn(name = "car_carId")
	private Car car;
	@DateTimeFormat (pattern = "dd/MM/yyyy")
	private LocalDate date;
	public Transaction() {
		super();
	}
	public Transaction(Car car, LocalDate date) {
		super();
		this.car = car;
		this.date = date;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCarId() {
		return carId;
	}
	public void setCarId(int carId) {
		this.carId = carId;
	}
	public Car getCar() {
		return car;
	}
	public void setCar(Car car) {
		this.car = car;
	}
	public LocalDate getDate() {
		return date;
	}
	public void setDate(LocalDate date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "Transaction [id=" + id + ", car=" + car + ", date=" + date + "]";
	}

}

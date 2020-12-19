package sg.edu.iss.service;

import java.time.LocalDate; 
import java.util.List;

import sg.edu.iss.model.Car;
import sg.edu.iss.model.Consumption;

public interface ConsumptionService {

	public List<Consumption> listConsumptionsbyProductId(int productid);
	List<Consumption> findConsumptionByTransactionIdwithDate(Integer id, LocalDate start, LocalDate end);
	List<Consumption> findConsumptionByTranAndProId(Integer tid, Integer pid);
	List<Car> findallCars ();
	public void saveConsumption(Consumption consumption);
	public void updateStock(Consumption consumption);
	public Consumption getConsumptionById(int id);
	public void manageStockStatus(Consumption dbConsumption, int currentStock, int consumedQty);
	public Consumption findLatestConsumption();
	public Boolean isExceedingStock(Consumption consumption);
}
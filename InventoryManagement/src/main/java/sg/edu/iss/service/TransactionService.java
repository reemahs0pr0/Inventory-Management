package sg.edu.iss.service;

import sg.edu.iss.model.Car;
import sg.edu.iss.model.Transaction;

public interface TransactionService {
	
	public boolean saveTransaction (Transaction transaction);
	public Car findCarbyId(int Id);
	
}

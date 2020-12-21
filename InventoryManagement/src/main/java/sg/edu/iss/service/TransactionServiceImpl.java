package sg.edu.iss.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.iss.model.Car;
import sg.edu.iss.model.Transaction;
import sg.edu.iss.repo.CarRepository;
import sg.edu.iss.repo.TransactionRepository;

@Service
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	TransactionRepository tranrepo;
	
	@Autowired
	CarRepository carepo;

	@Override
	public boolean saveTransaction(Transaction transaction) {
		if(tranrepo.save(transaction)!=null) {
			return true;
		}
		else {
			return false;
		}
	}

	@Override
	public Car findCarbyId(int Id) {
		return carepo.findById(Id).get();	
	}
	
}
package sg.edu.iss.service;

import java.time.LocalDate;  
import java.util.ArrayList;
import java.util.List;

import sg.edu.iss.model.Transaction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import sg.edu.iss.model.Car;
import sg.edu.iss.model.Consumption;
import sg.edu.iss.model.OrderStatus;
import sg.edu.iss.model.Product;
import sg.edu.iss.model.Reorder;
import sg.edu.iss.model.Stock;
import sg.edu.iss.model.StockStatus;
import sg.edu.iss.repo.CarRepository;
import sg.edu.iss.repo.ConsumptionRepository;
import sg.edu.iss.repo.ProductRepository;
import sg.edu.iss.repo.ReorderRepository;
import sg.edu.iss.repo.StockRepository;
import sg.edu.iss.repo.TransactionRepository;
import sg.edu.iss.repo.UserRepository;

@Service
public class ConsumptionServiceImpl implements ConsumptionService{
   
	 @Autowired
	 ConsumptionRepository conrepo;
	 
	 @Autowired
	 TransactionRepository tranrepo;
	 
	 @Autowired
	 CarRepository carrepo;
	 
	 @Autowired 
	 StockRepository strepo;
	 
	 @Autowired 
	 ProductRepository prepo;
	 
	 @Autowired 
	 UserRepository urepo;
	 
	 @Autowired 
	 ReorderRepository rrepo;
	 
	 @Autowired
	 private JavaMailSender emailSender;
	 
	@Override
	public List<Consumption> listConsumptionsbyProductId(int productid) {
		return conrepo.findConsumptionByProductId(productid);
	}

	@Override
	public List<Consumption> findConsumptionByTransactionIdwithDate(Integer id, 
			@DateTimeFormat(iso=ISO.DATE)LocalDate start, @DateTimeFormat(iso=ISO.DATE)LocalDate end) {
		List<Transaction> transactionsfound = tranrepo.findTransactionByDateRange(start, end);
		List<Consumption> consumptionsfound = new ArrayList<Consumption>();
		for(Transaction t:transactionsfound) {
			for(Consumption c: conrepo.findConsumptionByTranAndProId(t.getId(), id)) {
				consumptionsfound.add(c);
			}
		}
		return consumptionsfound;
	}

	@Override
	public List<Car> findallCars() {
		return carrepo.findAll();
	}

	@Override
	public void saveConsumption(Consumption consumption) {
		conrepo.save(consumption);
	}
	
	@Override
	public Consumption getConsumptionById(int id) {
		return conrepo.findById(id).get();
	}

	@Override
	public void updateStock(Consumption consumption) {
		Consumption dbConsumption = findLatestConsumption();
		int currentStock = dbConsumption.getProduct().getStock().getUnits();
		int consumedQty = consumption.getConsumedQty();
		Stock stock = dbConsumption.getProduct().getStock();
		stock.setUnits(currentStock-consumedQty);
		strepo.saveAndFlush(stock);
		
		manageStockStatus(dbConsumption, currentStock, consumedQty);
	}
	
	@Override
	public void manageStockStatus(Consumption dbConsumption, int currentStock, int consumedQty) {
		int newStock = currentStock - consumedQty;
		int reorderQty = dbConsumption.getProduct().getReorderQty();
		if(newStock <= reorderQty) {
			if(dbConsumption.getProduct().getStock().getStatus() == StockStatus.IN_STOCK) {
				dbConsumption.getProduct().getStock().setStatus(StockStatus.BELOW_REORDER_LEVEL);
				strepo.flush();
				//system order mail reminder to all admins
				String[] recipients = urepo.findAllAdminsEmail().toArray(new String[0]);
				SimpleMailMessage message = new SimpleMailMessage();
				//comment next line when testing
		        message.setTo(recipients);
//				--------------------------------------TESTING------------------------------------
//		        List<String> testRecipientsList = new ArrayList<>();
//		        testRecipientsList.add(<RECIPIENT EMAIL ADDRESS 1>);
//		        testRecipientsList.add(<RECIPIENT EMAIL ADDRESS 2>);
//		        testRecipientsList.add(<RECIPIENT EMAIL ADDRESS 3>);
//		        String[] testRecipientsListArray = testRecipientsList.toArray(new String[0]);
//				message.setTo(testRecipientsListArray);
//				---------------------------------------------------------------------------------
		        message.setSubject("Notification: Product Low In Stock");
		        message.setText("[Product Id: " + dbConsumption.getProduct().getProductId() + "] " 
		        		+ dbConsumption.getProduct().getName() + " is low in stock. Please reorder additional qty.");
		        emailSender.send(message);
		        //add new reorder record
		        rrepo.save(new Reorder(dbConsumption.getProduct(), OrderStatus.PENDING_ORDER, newStock, 
		        		dbConsumption.getProduct().getMOQ()+dbConsumption.getProduct().getReorderQty()-newStock));
			}
			
		}
	}
	
	@Override
	public Consumption findLatestConsumption() {
		List<Consumption> consumptions = conrepo.findLatestConsumption();
		Consumption consumption = consumptions.get(0);
		return consumption;
	}
	
	@Override
	public Boolean isExceedingStock(Consumption consumption) {
		Product product = prepo.findById(consumption.getProductId()).get();
		int stockUnits = product.getStock().getUnits();
		if(consumption.getConsumedQty() > stockUnits) {
			return true;
		}
		return false;
	}
	
}


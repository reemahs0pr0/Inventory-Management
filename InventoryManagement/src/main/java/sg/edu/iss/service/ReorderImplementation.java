package sg.edu.iss.service;

<<<<<<< Updated upstream
import java.time.LocalDate;     
=======
import java.time.LocalDate;
import java.util.ArrayList;
>>>>>>> Stashed changes
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Service;

import sg.edu.iss.model.Consumption;
import sg.edu.iss.model.OrderStatus;
import sg.edu.iss.model.Product;
import sg.edu.iss.model.Reorder;
import sg.edu.iss.model.StockStatus;
import sg.edu.iss.model.Transaction;
import sg.edu.iss.repo.ProductRepository;
import sg.edu.iss.repo.ReorderRepository;
import sg.edu.iss.repo.StockRepository;

@Service
public class ReorderImplementation implements ReorderInterface {
	
	@Autowired 
	ReorderRepository rrepo;
	
	@Autowired 
	StockRepository strepo;
	
	@Autowired
	ProductRepository prepo;
	
	@Override
	public List<Reorder> list(String keyword) {
		if (keyword != null) {
			return rrepo.search(keyword);
		}
		return rrepo.findAll();
	}

	@Override
	public Reorder getReorderById(int id) {
		return rrepo.findById(id).get();
	}
	
	@Override
	public void updateOrderQty(Reorder reorder) {
		Reorder dbReorder = getReorderById(reorder.getId());
		dbReorder.setOrderQty(reorder.getOrderQty());
		dbReorder.setStockUnits(dbReorder.getProduct().getStock().getUnits());
		dbReorder.getProduct().getStock().setStatus(StockStatus.REORDER_PLACED);
		rrepo.flush();
		strepo.flush();
	}

	@Override
	public void updateOrderStatus(Reorder reorder) {
		Reorder dbReorder = getReorderById(reorder.getId());
		if (dbReorder.getStatus() == OrderStatus.PENDING_ORDER) {
			dbReorder.setStatus(OrderStatus.REORDERED);
			Product dbProduct = prepo.findbyName(reorder.getProduct().getName());
			dbProduct.addReorder(reorder);
			prepo.flush();
		}
		else if (dbReorder.getStatus() == OrderStatus.REORDERED) {
			dbReorder.setStatus(OrderStatus.RECEIVED);
		}
		else if (dbReorder.getStatus() == OrderStatus.RECEIVED) {
			dbReorder.setStatus(OrderStatus.ADDED_TO_INVENTORY);
		}
		rrepo.flush();
	}

	@Override
	public void addDate(Reorder reorder) {
		Reorder dbReorder = getReorderById(reorder.getId());
		LocalDate dateOrdered = LocalDate.now();
		dbReorder.setDate(dateOrdered);
		rrepo.flush();
	}
	
	@Override
	public void updateDateReceived(Reorder reorder) {
		Reorder dbReorder = getReorderById(reorder.getId());
		LocalDate dateReceived = LocalDate.now();
		dbReorder.setDateReceived(dateReceived);
		rrepo.flush();
	}

	@Override
	public void updateDamagedQty(Reorder reorder) {
		Reorder dbReorder = getReorderById(reorder.getId());
		dbReorder.setDamagedQty(reorder.getDamagedQty());
		rrepo.flush();
	}

	@Override
	public void updateDamagedDescription(Reorder reorder) {
		Reorder dbReorder = getReorderById(reorder.getId());
		dbReorder.setDamagedDescription(reorder.getDamagedDescription());
		rrepo.flush();
	}
	
	@Override
	public boolean isOrderable(Reorder reorder) {
		Reorder dbReorder = getReorderById(reorder.getId()); 
		if (reorder.getOrderQty() >= dbReorder.getProduct().getMOQ()) {
			return true; 
		}
		else {
			return false;
		}
	}
	
	@Override
	public void updateStock(Reorder reorder) {
		Reorder dbReorder = getReorderById(reorder.getId());
		int currentStock = dbReorder.getProduct().getStock().getUnits();
		int orderQty = reorder.getOrderQty();
		int damagedQty = reorder.getDamagedQty();
		dbReorder.getProduct().getStock().setUnits(currentStock+orderQty-damagedQty);
		dbReorder.getProduct().getStock().setStatus(StockStatus.IN_STOCK);
		strepo.flush();
	}
	
	@Override
	public List<Reorder> findReorderByDate(@DateTimeFormat(iso=ISO.DATE)LocalDate start, 
			@DateTimeFormat(iso=ISO.DATE)LocalDate end) {
		
		List<Reorder> reorders = rrepo.findReorderByDateRange(start, end);
		return reorders;
	}
	
}

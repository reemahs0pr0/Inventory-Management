package sg.edu.iss.service; 

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import sg.edu.iss.model.Reorder;

public interface ReorderInterface {
	
	public List<Reorder> list();
	public Reorder getReorderById(int id);
	public void updateOrderQty(Reorder reorder); 
	public void updateOrderStatus(Reorder reorder); 
	public void addDate(Reorder reorder);
	public void updateDamagedQty(Reorder reorder);
	public void updateDamagedDescription(Reorder reorder); 
	public boolean isOrderable(Reorder reorder);
	public void updateDateReceived(Reorder reorder);
	public void updateStock(Reorder reorder);
	public List<Reorder> findReorderByDate(@DateTimeFormat(iso=ISO.DATE)LocalDate start, 
			@DateTimeFormat(iso=ISO.DATE)LocalDate end);

}

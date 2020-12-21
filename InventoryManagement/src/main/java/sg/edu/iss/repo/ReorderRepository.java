package sg.edu.iss.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import sg.edu.iss.model.Reorder;

public interface ReorderRepository extends JpaRepository<Reorder, Integer>{
	
	@Query("SELECT r FROM Reorder r WHERE r.product.supplier.companyName LIKE %?1% AND r.product.supplier.status = 0")
	public List<Reorder> search(String keyword);

}

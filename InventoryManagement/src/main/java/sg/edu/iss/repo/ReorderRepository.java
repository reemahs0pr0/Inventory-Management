package sg.edu.iss.repo;

<<<<<<< Updated upstream
=======
import java.time.LocalDate;
>>>>>>> Stashed changes
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
<<<<<<< Updated upstream
=======
import org.springframework.data.repository.query.Param;
>>>>>>> Stashed changes

import sg.edu.iss.model.Reorder;

public interface ReorderRepository extends JpaRepository<Reorder, Integer>{
	
<<<<<<< Updated upstream
	@Query("SELECT r FROM Reorder r WHERE r.product.supplier.companyName LIKE %?1% AND r.product.supplier.status = 0")
	public List<Reorder> search(String keyword);
=======
	@Query("Select r from Reorder r where r.date BETWEEN :start AND :end")
	List<Reorder> findReorderByDateRange(@Param("start")LocalDate start, @Param("end")LocalDate end);
>>>>>>> Stashed changes

}

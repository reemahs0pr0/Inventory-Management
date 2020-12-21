package sg.edu.iss.repo;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import sg.edu.iss.model.Reorder;

public interface ReorderRepository extends JpaRepository<Reorder, Integer>{

	@Query("Select r from Reorder r where r.date BETWEEN :start AND :end")
	List<Reorder> findReorderByDateRange(@Param("start")LocalDate start, @Param("end")LocalDate end);

}

package sg.edu.iss.service;
  
import java.util.List; 

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import sg.edu.iss.model.Product;
import sg.edu.iss.model.ProductStatus;
import sg.edu.iss.model.Stock;

public interface ProductService {
	
	 public boolean saveProduct(Product product);
	 public void deleteProduct(Product product);
	 public Product findProductbyId(Integer id);
	 public Product findProductbyName(String name);
	 public List<Product> findProductsByStatus(ProductStatus status);
	 public List<Product> listAll(String keyword);
	 public void deleteStock(Stock stock);
	 public Page<Product> findPaginated(List<Product> products, Pageable pageable);
	 
}

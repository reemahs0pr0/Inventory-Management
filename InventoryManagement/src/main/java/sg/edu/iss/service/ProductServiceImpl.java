package sg.edu.iss.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.edu.iss.model.ProductStatus;
import sg.edu.iss.model.Stock;
import sg.edu.iss.model.Product;
import sg.edu.iss.repo.ProductRepository;
import sg.edu.iss.repo.StockRepository;

@Service
public class ProductServiceImpl implements ProductService {
	
	@Autowired
	ProductRepository prorepo;
	
	@Autowired
	StockRepository strepo;
	
	@Override
	@Transactional
	public boolean saveProduct(Product product) {
		if(prorepo.save(product)!=null) { 
			return true; 
		}
		return false;
	}

	@Override
	@Transactional
	public void deleteProduct(Product product) {
		prorepo.delete(product);
	}

	@Override
	public Product findProductbyId(Integer id) {
		return prorepo.findById(id).get();
	}

	@Override
	public Product findProductbyName(String name) {
		return prorepo.findbyName(name);
	}

	@Override
	public List<Product> findProductsByStatus(ProductStatus status) {
		List<Product> products = prorepo.findAll();
		List<Product> productsByStatus = new ArrayList<Product>();
		for(Product product : products) {
			if(product.getStatus() == status) {
				productsByStatus.add(product);
			}
		}
		return productsByStatus;
	}

	@Override
	public List<Product> listAll(String keyword) {
		if (keyword != null) {
			return (List<Product>)prorepo.search(keyword);
		}
		return (List<Product>)findProductsByStatus(ProductStatus.IN_USE);
	}
	
	@Override
	public void deleteStock(Stock stock) {
		strepo.delete(stock);
	}

}

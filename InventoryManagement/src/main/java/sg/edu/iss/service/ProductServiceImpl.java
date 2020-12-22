package sg.edu.iss.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
	
	public Page<Product> findPaginated(List<Product> products, Pageable pageable) {
		int pageSize = pageable.getPageSize();
		int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<Product> list;
        
        if (products.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, products.size());
            list = products.subList(startItem, toIndex);
        }
        
        Page<Product> productPage = new PageImpl<Product>(list, PageRequest.of(currentPage, pageSize), products.size());

        return productPage;
	}

}

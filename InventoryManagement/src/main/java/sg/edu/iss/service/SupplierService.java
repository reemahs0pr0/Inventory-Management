package sg.edu.iss.service;

import java.util.List;

import sg.edu.iss.model.Supplier;
import sg.edu.iss.model.SupplierStatus;

public interface SupplierService {
	
	 public boolean saveSupplier(Supplier supplier);
	 public void deleteSupplier(Supplier supplier);
	 public Supplier findSupplierbyId(Integer id);
	 public Supplier findSupplierbyBrandName(String brandname);
	 public Supplier findSupplierbyCompanyName(String companyname);
	 public Supplier findSupplierbyContactNumber(String contactnumber);
	 public Supplier findSupplierbyAddress (String address);
	 public Supplier findSupplierbyEmailAddress(String emailaddress);
	 public List<Supplier> findSuppliersByStatus(SupplierStatus status);
	 
}

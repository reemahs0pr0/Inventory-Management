package sg.edu.iss.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sg.edu.iss.model.Supplier;
import sg.edu.iss.model.SupplierStatus;
import sg.edu.iss.repo.SupplierRepository;

@Service
public class SupplierServiceImpl implements SupplierService {
	
	@Autowired
	SupplierRepository suprepo;
	
	@Override
	@Transactional
	public boolean saveSupplier(Supplier supplier) {
		if(suprepo.save(supplier)!=null) 
			return true; 
		else return false;
	}

	@Override
	@Transactional
	public void deleteSupplier(Supplier supplier) {
		suprepo.delete(supplier);
	}

	@Override
	@Transactional
	public Supplier findSupplierbyId(Integer id) {
		return suprepo.findById(id).get();
	}

	@Override
	public Supplier findSupplierbyBrandName(String brandname) {
		return suprepo.findByBrandName(brandname);
	}

	@Override
	public Supplier findSupplierbyCompanyName(String companyname) {
		return suprepo.findByCompanyName(companyname);
	}

	@Override
	public Supplier findSupplierbyContactNumber(String contactnumber) {
		return suprepo.findByContactNumber(contactnumber);
	}

	@Override
	public Supplier findSupplierbyAddress(String address) {
		return suprepo.findByAddress(address);
	}

	@Override
	public Supplier findSupplierbyEmailAddress(String emailaddress) {
		return suprepo.findByEmailAddress(emailaddress);
	}
	
	@Override
	public List<Supplier> findSuppliersByStatus(SupplierStatus status) {
		List<Supplier> suppliers = suprepo.findAll();
		List<Supplier> suppliersByStatus = new ArrayList<Supplier>();
		for(Supplier supplier : suppliers) {
			if(supplier.getStatus() == status) {
				suppliersByStatus.add(supplier);
			}
		}
		return suppliersByStatus;
	}

}

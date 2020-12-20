package sg.edu.iss.controller;

import java.time.LocalDate;  
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import sg.edu.iss.model.Car;
import sg.edu.iss.model.Consumption;
import sg.edu.iss.model.Product;
import sg.edu.iss.model.RoleType;
import sg.edu.iss.model.Transaction;
import sg.edu.iss.service.ConsumptionService;
import sg.edu.iss.service.ConsumptionServiceImpl;
import sg.edu.iss.service.ProductService;
import sg.edu.iss.service.ProductServiceImpl;
import sg.edu.iss.service.TransactionService;
import sg.edu.iss.service.TransactionServiceImpl;

@Controller
@RequestMapping("/usage")
public class UsageController {

	@Autowired
	private ProductService proservice;
	
	@Autowired
	private ConsumptionService conservice;
	
	@Autowired
	private TransactionService transservice;
	
	@Autowired
	public void setProductService(ProductServiceImpl productServiceImpl) {
		this.proservice =productServiceImpl;
	}
	
	@Autowired
	public void setConsumptionService(ConsumptionServiceImpl conServiceImpl) {
		this.conservice=conServiceImpl;
	}
	
	@Autowired
	public void setTransservice(TransactionServiceImpl tranServiceImpl) {
		this.transservice = tranServiceImpl;
	}
	
	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addtransaction(Model model) {
		Transaction transaction = new Transaction();
		List<Car> cars = conservice.findallCars();
		model.addAttribute("transaction", transaction);
		model.addAttribute("cars", cars);
		return "selectcustomer";
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public String saveUsage(@ModelAttribute("transaction") @Valid Transaction transaction,BindingResult bindingResult, 
			Model model, HttpSession session) {    
		if (bindingResult.hasErrors()) {	
			System.out.println(bindingResult);
			List<Car> cars = conservice.findallCars();
			model.addAttribute("transaction", transaction);
			model.addAttribute("cars", cars);
			return "selectcustomer";
		}
		else {
			Car car= transservice.findCarbyId(transaction.getCarId());
			transaction.setCar(car);
			transaction.setDate(LocalDate.now());
			session.setAttribute("transaction", transaction);
			return "forward:/usage/addConsumption";
		}
		 
	}
	
	@RequestMapping(value = "/addConsumption")
	public String addConsumption(Model model, HttpSession session) {
		session.setAttribute("consumptions", new ArrayList<Consumption>());
		Consumption consumption = new Consumption();
		List<Product> products = proservice.listAll(null);
		model.addAttribute("consumption", consumption);
		model.addAttribute("products", products);
		return "stockusageform";
	}
	
	@RequestMapping(value = "/removepart")
	public String removePart(Model model, HttpSession session) {
		List<Consumption> consumptions = (List<Consumption>) session.getAttribute("consumptions");
		consumptions.remove(consumptions.size()-1);
		session.setAttribute("consumptions", consumptions);
		Consumption consumption = new Consumption();
		List<Product> products = proservice.listAll(null);
		model.addAttribute("consumption", consumption);
		model.addAttribute("products", products);
		return "stockusageform";
	}
	
	@RequestMapping(value = "/saveConsumption", params="addpart", method = RequestMethod.POST)
	public String addPart(@ModelAttribute("consumption") @Valid Consumption consumption,BindingResult bindingResult, 
			Model model, HttpSession session) {	    
		if (bindingResult.hasErrors()) {	
			List<Product> products = proservice.listAll(null);
			model.addAttribute("consumption",consumption);
			model.addAttribute("products",products);
			return "stockusageform";
		}
		else if (conservice.isExceedingStock(consumption)) {
			List<Product> products = proservice.listAll(null);
			model.addAttribute("consumption",consumption);
			model.addAttribute("products",products);
			Product product= proservice.findProductbyId(consumption.getProductId());
			int stock = product.getStock().getUnits();
			String errmessage = "Only " + stock + " units left in stock";
			model.addAttribute("exceedStock", errmessage);
			return "stockusageform";
		}
		else {
			Product product= proservice.findProductbyId(consumption.getProductId());
			consumption.setTransaction((Transaction)session.getAttribute("transaction"));
			consumption.setProduct(product);
			
			List<Consumption> consumptions = (List<Consumption>) session.getAttribute("consumptions");
			consumptions.add(consumption);
			session.setAttribute("consumptions", consumptions);

			Consumption newConsumption = new Consumption();
			List<Product> products = proservice.listAll(null);
			model.addAttribute("consumption", newConsumption);
			model.addAttribute("products", products);
		
			return "stockusageform";
		}	
		 
	}
	
	@RequestMapping(value = "/saveConsumption", params="commit", method = RequestMethod.POST)
	public String commitUsage(@ModelAttribute("consumption") @Valid Consumption consumption, BindingResult bindingResult, 
			Model model, HttpSession session) {
	
	    
		if (bindingResult.hasErrors()) 
		{	
			List<Product> products = proservice.listAll(null);
			model.addAttribute("consumption",consumption);
			model.addAttribute("products",products);
			return "stockusageform";
		}
		else if (conservice.isExceedingStock(consumption)) {
			List<Product> products = proservice.listAll(null);
			model.addAttribute("consumption",consumption);
			model.addAttribute("products",products);
			Product product= proservice.findProductbyId(consumption.getProductId());
			int stock = product.getStock().getUnits();
			String errmessage = "Only " + stock + " units left in stock";
			model.addAttribute("exceedStock", errmessage);
			return "stockusageform";
		}
		else {
			Product product= proservice.findProductbyId(consumption.getProductId());
			consumption.setTransaction((Transaction)session.getAttribute("transaction"));
			consumption.setProduct(product);
			
			List<Consumption> consumptions = (List<Consumption>) session.getAttribute("consumptions");
			consumptions.add(consumption);
			session.setAttribute("consumptions", consumptions);
			
			Transaction t = (Transaction)session.getAttribute("transaction");
			transservice.saveTransaction(t);
			
			List<Consumption> finalConsumptions = (List<Consumption>) session.getAttribute("consumptions");
			for(Consumption c : finalConsumptions) {
				Product p = proservice.findProductbyId(c.getProductId());
				product.addConsumption(c);
				conservice.saveConsumption(c);
				proservice.saveProduct(p);
				conservice.updateStock(c);
			}
			
			if(session.getAttribute("session") == RoleType.ADMIN) {
				return "adminmain";
			}
		
			return "mechanicmain";
		}
		 
	}
	
}
	


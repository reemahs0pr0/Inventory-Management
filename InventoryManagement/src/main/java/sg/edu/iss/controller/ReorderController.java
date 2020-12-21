package sg.edu.iss.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import sg.edu.iss.model.Consumption;
import sg.edu.iss.model.Product;
import sg.edu.iss.model.Reorder;
import sg.edu.iss.model.SupplierStatus;
import sg.edu.iss.model.Supplier;
import sg.edu.iss.service.ProductService;
import sg.edu.iss.service.ProductServiceImpl;
import sg.edu.iss.service.ReorderImplementation;
import sg.edu.iss.service.ReorderInterface;
import sg.edu.iss.service.SupplierService;
import sg.edu.iss.service.SupplierServiceImpl;

@Controller
@RequestMapping("/reorders")
public class ReorderController {

	@Autowired
	private ReorderInterface rservice;
	
	@Autowired
	private SupplierService supservice;

	@Autowired
	public void setReorderImplementation(ReorderImplementation rimpl) {
		this.rservice = rimpl;
	}
	
	@Autowired
	public void setSupplierService(SupplierServiceImpl supserviceimpl) {
		this.supservice = supserviceimpl;
	}
	
	// User requests to view the list of orders made
	@RequestMapping(value = "/list")
	public String list(Model model) {
		List<Reorder> rlist = rservice.list();
		model.addAttribute("rlist", rlist);
		model.addAttribute("today", LocalDate.now().toString());
		model.addAttribute("suppliers", supservice.findSuppliersByStatus(SupplierStatus.SUPPLYING));
		return "reorderslist";
	}
	
	@RequestMapping(value = "/list/filter") 
	public String catalog(Model model, @Param("start")@DateTimeFormat(iso=ISO.DATE) LocalDate start,
							@Param("end")@DateTimeFormat(iso=ISO.DATE) LocalDate end) {
		
		List<Reorder> reorders = rservice.findReorderByDate(start, end);
		model.addAttribute("rlist", reorders);
		model.addAttribute("today", LocalDate.now().toString());
		model.addAttribute("start",start);
		model.addAttribute("end",end);

		return "reorderslist";
	}

	// show reorder form for specific record id
	@RequestMapping(value = "/viewform/{id}", method = RequestMethod.GET)
	public String viewReorderForm(@PathVariable("id") Integer id, Model model) {
		Reorder reorder = rservice.getReorderById(id);
		model.addAttribute("reorder", reorder);
		return "reorderform";
	}

	// place order in reorder form
	@RequestMapping(value = "/placeorder/{id}", method = RequestMethod.POST)
	public String placeOrder(@PathVariable("id") Integer id, @Valid Reorder reorder, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "reorderform";
		}
		// validation check that when orderQty < MOQ
		else if (!rservice.isOrderable(reorder)) {
			model.addAttribute("belowMOQ", "Order quantity has to be equal or greater than MOQ");
			return "reorderform";
		}
		else {
			// update order quantity of reorder object
			rservice.updateOrderQty(reorder); 
			// update order status or reorder object 
			rservice.updateOrderStatus(reorder);
			// update 
			rservice.addDate(reorder);
			// return reorders list
			return "forward:/reorders/list";
		}
	}
	
	// show order for changing status to received for specific record id
	@RequestMapping(value = "/orderreceived/{id}", method = RequestMethod.POST)
	public String orderReceived(@PathVariable("id") Integer id, Reorder reorder, Model model) {
		// update order status or reorder object 
		rservice.updateOrderStatus(reorder);
		// update date received of reorder object 
		rservice.updateDateReceived(reorder);
		// return reorders list
		return "forward:/reorders/list";
	}

	// show order for specific record id
	@RequestMapping(value = "/vieworder/{id}", method = RequestMethod.GET)
	public String viewOrder(@PathVariable("id") Integer id, Model model) {
		Reorder reorder = rservice.getReorderById(id);
		model.addAttribute("reorder", reorder);
		return "vieworder";
	}

	// get stock entry form to view
	@RequestMapping(value = "/viewseform/{id}", method = RequestMethod.GET)
	public String viewSEForm(@PathVariable("id") Integer id, Model model) {
		Reorder reorder = rservice.getReorderById(id); 
		model.addAttribute("reorder", reorder);
		return "seformview"; 
	}

	// get stock entry form to edit
	@RequestMapping(value = "/editseform/{id}", method = RequestMethod.GET)
	public String editSEForm(@PathVariable("id") Integer id, Model model) {
		Reorder reorder = rservice.getReorderById(id); 
		model.addAttribute("reorder", reorder);
		return "seformedit"; 
	}
	
	// save stock entry form 
	@RequestMapping(value= "/processform/{id}", params="saveSEForm", method = RequestMethod.POST)
	public String saveSEForm(@PathVariable("id") Integer id, @ModelAttribute("reorder") @Valid Reorder reorder, 
			BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "seformedit";
		}
		else {
			// update damaged quantity of reorder object
			rservice.updateDamagedQty(reorder); 
			// update damaged description of reorder object
			rservice.updateDamagedDescription(reorder); 
			// return reorders list
			return "forward:/reorders/list";
		}
	}
	
	// add to inventory 
	@RequestMapping(value= "/processform/{id}", params="addToInventory", method = RequestMethod.POST)
	public String addToInventory(@PathVariable("id") Integer id, @ModelAttribute("reorder") @Valid Reorder reorder, 
			BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "seformedit";
		}
		else {
			// update damaged quantity of reorder object
			rservice.updateDamagedQty(reorder); 
			// update damaged description of reorder object
			rservice.updateDamagedDescription(reorder); 
			// update reorder object 
			rservice.updateOrderStatus(reorder);
			//update new stock
			rservice.updateStock(reorder);
			// return reorders list
			return "forward:/reorders/list";
		}
	}
	
	// Creates padding to center a string
	public static String centerString (int width, String s) {
	    return String.format("%-" + width  + "s", String.format("%" + (s.length() + (width - s.length()) / 2) + "s", s));
	}
	
	//generates report based on consumptions list passed in
	public static void genReport(Supplier supplier, List<Reorder> reorders, LocalDate start, LocalDate end, 
			String filepath) {
		// for testing
		System.out.println("Triggered");
		
        try {
        	File myObj = new File(filepath);
            if (myObj.createNewFile()) {
              System.out.println("File created: " + myObj.getName());
            } else {
              System.out.println("File already exists.");
            }
        } 
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        
        try {
            FileWriter myWriter = new FileWriter(filepath);
            String spacer = "-";
            String spacer2 = "=";
            String nl = "\r\n"; //carriage return
            //Heading
            String title = "Inventory Reorder Report for products from Supplier " + supplier.getCompanyName();
            myWriter.write(centerString(92, title + "\n"));
            myWriter.write(spacer.repeat(title.length()) + nl);
            //Table headings
            myWriter.write(spacer2.repeat(92) + nl);
            myWriter.write("Product Id" + " ".repeat(5));
            myWriter.write("Unit Price" + " ".repeat(5));
            myWriter.write("Qty" + " ".repeat(5));
            myWriter.write("Reorder Qty" + " ".repeat(5));
            myWriter.write("Min Order Qty" + " ".repeat(5));
            myWriter.write("Order Qty" + " ".repeat(5));
            myWriter.write("Price" + nl);
            myWriter.write(spacer2.repeat(92) + nl);
            //data
            float total = 0;
    		for (Reorder r : reorders) {
                myWriter.write("0".repeat(4-String.valueOf(r.getProduct().getProductId()).length()) + r.getProduct().getProductId() + " ".repeat(11));
                myWriter.write(r.getProduct().getOriginalPrice() + "0" + " ".repeat(10));
                myWriter.write(r.getStockUnits() + " ".repeat(8));
                myWriter.write(r.getProduct().getReorderQty() + " ".repeat(15));
                myWriter.write(r.getProduct().getMOQ() + " ".repeat(17));
                myWriter.write(r.getOrderQty() + " ".repeat(9));
                myWriter.write( (r.getProduct().getOriginalPrice() * r.getOrderQty()) + nl);
                total += r.getProduct().getOriginalPrice() * r.getOrderQty();                
    		}
    		//total
    		myWriter.write(spacer2.repeat(92) + nl);
    		myWriter.write(" ".repeat(70) + "TOTAL" + " ".repeat(10) + total + "0" + nl);
    		myWriter.write(spacer2.repeat(92) + nl);
    		//end
    		myWriter.write(centerString(92, "End of Report"));
    		
            myWriter.flush();
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } 
        catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
	}
	
	@RequestMapping(value = "/list/filter/report") 
	public String GenerateReport(Model model, @Param("start")@DateTimeFormat(iso=ISO.DATE) LocalDate start,
							@Param("end")@DateTimeFormat(iso=ISO.DATE) LocalDate end,
							HttpServletResponse response) throws IOException {
		
		model.addAttribute("today", LocalDate.now().toString());
		System.out.println(start);
		System.out.println(end);
		//if no date is input it will generate report for the whole list
		if(start == null && end == null) {
			List<Supplier> suppliers = supservice.findSuppliersByStatus(SupplierStatus.SUPPLYING);
			for(Supplier supplier : suppliers) {
				List<Reorder> rlist = new ArrayList<Reorder>();
				List<Product> products = supplier.getProducts();
				for(Product product : products) {
					List<Reorder> reorders = product.getReorders();
					for(Reorder reorder : reorders) {
						rlist.add(reorder);
					}
				}
				String filepath = "C:\\forCa\\" + supplier.getCompanyName() + "_report.dat";
				genReport(supplier, rlist, start, end, filepath);
				model.addAttribute("reorders", rservice.list());
				
				// Triggers download
				response.setContentType("application/octet-stream");
				String filename = supplier.getCompanyName() + "_report.dat";
				response.setHeader("Content-Disposition",
				"attachment;filename=" + filename);
				File file = new File(filepath);
				FileInputStream fileIn = new FileInputStream(file);
				ServletOutputStream out = response.getOutputStream();

				byte[] outputByte = new byte[4096];
				//copy binary content to output stream
				while(fileIn.read(outputByte, 0, 4096) != -1) {
				    out.write(outputByte, 0, 4096);
				}
				fileIn.close();
				out.flush();
				out.close();
			}			
		}
		else {
			List<Supplier> suppliers = supservice.findSuppliersByStatus(SupplierStatus.SUPPLYING);
			for(Supplier supplier : suppliers) {
				List<Reorder> rlist = new ArrayList<Reorder>();
				List<Product> products = supplier.getProducts();
				for(Product product : products) {
					List<Reorder> reorders = product.getReorders();
					for(Reorder reorder : reorders) {
						if((reorder.getDate().isAfter(start) || reorder.getDate().isEqual(start)) && 
								(reorder.getDate().isBefore(end) || reorder.getDate().isEqual(end))) {
							rlist.add(reorder);
						}
					}
				}
				String filepath = "C:\\forCa\\" + supplier.getCompanyName() + "_report.dat";
				genReport(supplier, rlist, start, end, filepath);
				model.addAttribute("reorders", rservice.findReorderByDate(start, end));
				
				// Triggers download
				String filename = supplier.getCompanyName() + "_report.dat";
				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition",
				"attachment;filename=" + filename);
				File file = new File(filepath);
				FileInputStream fileIn = new FileInputStream(file);
				ServletOutputStream out = response.getOutputStream();

				byte[] outputByte = new byte[4096];
				//copy binary content to output stream
				while(fileIn.read(outputByte, 0, 4096) != -1) {
				    out.write(outputByte, 0, 4096);
				}
				fileIn.close();
				out.flush();
				out.close();
			}			
			
		}
		return "reorderslist";
	}
		
}
	



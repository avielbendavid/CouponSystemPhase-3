package coupon.system.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coupon.system.core.entities.Company;
import coupon.system.core.entities.Customer;
import coupon.system.core.exceptions.CouponSystemException;
import coupon.system.core.services.AdminService;
import coupon.system.core.session.SessionContext;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {

	@Autowired
	private SessionContext sessionContext;

	@PostMapping(path = "/add-company")
	public ResponseEntity<?> addCompany(@RequestBody Company company, @RequestHeader String token) {
		AdminService adminService = (AdminService) sessionContext.getSession(token).getAttributes("service");
		try {
			adminService.addCompany(company);
			return ResponseEntity.ok("Adding company [" + company.getName() + "] was successfull");
		} catch (CouponSystemException e) {

			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Sorry, can not add the company --->>> " + e.getMessage());
		}

	}

	@PutMapping(path = "/update-company")
	public ResponseEntity<?> updateCompany(@RequestBody Company company, @RequestHeader String token) {
		AdminService adminService = (AdminService) sessionContext.getSession(token).getAttributes("service");
		try {
			adminService.updateCompany(company);
			return ResponseEntity.ok("Update company successfully");
		} catch (CouponSystemException e) {

			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Sorry, can not update the company --->>> " + e.getMessage());
		}
	}

	@DeleteMapping(path = "/delete-company/{companyId}")
	public ResponseEntity<?> deleteCompany(@PathVariable Integer companyId, @RequestHeader String token) {
		AdminService adminService = (AdminService) sessionContext.getSession(token).getAttributes("service");
		try {
			adminService.deleteCompany(companyId);
			return ResponseEntity.ok("The Company [id: "+companyId+"] was deleted successfully");
		} catch (CouponSystemException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Sorry, can not delete the company -->> " + e.getMessage());
		}
	}

	@GetMapping(path = "/get-all-companies", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllCompanies(@RequestHeader String token) {
		AdminService adminService = (AdminService) sessionContext.getSession(token).getAttributes("service");
		try {
			return ResponseEntity.ok(adminService.getAllCompanies());
		} catch (CouponSystemException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Sorry, can not get all companies -->> " + e.getMessage());
		}
	}

	@GetMapping(path = "/get-one-company/{id}")
	public ResponseEntity<?> getOneCompany(@PathVariable int id, @RequestHeader String token) {
		AdminService adminService = (AdminService) sessionContext.getSession(token).getAttributes("service");
		try {
			return ResponseEntity.ok(adminService.getOneCompany(id));
		} catch (CouponSystemException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Sorry, can not get the company -->> " + e.getMessage());
		}
	}

	@PostMapping(path = "/add-customer")
	public ResponseEntity<?> addCustomer(@RequestBody Customer customer, @RequestHeader String token) {
		AdminService adminService = (AdminService) sessionContext.getSession(token).getAttributes("service");
		try {
			adminService.addCustomer(customer);
			return ResponseEntity.ok("The company ["+ customer.getFirstName() +"]  was added successfully");
		} catch (CouponSystemException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Sorry can not add the customer -->> " + e.getMessage());
		}
	}

	@PutMapping(path = "/update-customer")
	public ResponseEntity<?> updateCustomer(@RequestBody Customer customer, @RequestHeader String token) {
		AdminService adminService = (AdminService) sessionContext.getSession(token).getAttributes("service");
		try {
			adminService.updateCustomer(customer);
			return ResponseEntity.ok("The customer ["+customer.getFirstName()+"] was updated successfully");
		} catch (CouponSystemException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Updating the customer was failed -->> " + e.getMessage());
		}
	}

	@DeleteMapping(path = "/delete-one-customer/{customerId}")
	public ResponseEntity<?> deleteCustomer(@PathVariable Integer customerId, @RequestHeader String token) {
		AdminService adminService = (AdminService) sessionContext.getSession(token).getAttributes("service");
		try {
			adminService.deleteCustomer(customerId);
			return ResponseEntity.ok("The customer [id: "+customerId+"] was deleted successfully");
		} catch (CouponSystemException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Deleting the customer was failed -->> " + e.getMessage());
		}
	}

	@GetMapping(path = "/get-all-customers")
	public ResponseEntity<?> getAllCustomers(@RequestHeader String token) {
		AdminService adminService = (AdminService) sessionContext.getSession(token).getAttributes("service");
		try {
			return ResponseEntity.ok(adminService.getAllCustomers());
		} catch (CouponSystemException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Sorry, can not get all customers -->> " + e.getMessage());
		}
	}

	@GetMapping(path = "/get-one-customer/{customerId}")
	public ResponseEntity<?> getOneCustomer(@PathVariable Integer customerId, @RequestHeader String token) {
		AdminService adminService = (AdminService) sessionContext.getSession(token).getAttributes("service");
		try {
			Customer c1 = adminService.getOneCustomer(customerId);
			return ResponseEntity.ok(c1);
		} catch (CouponSystemException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Sorry, can not get the customer >>> " + e.getMessage());
		}
	}
}

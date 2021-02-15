package coupon.system.core.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coupon.system.core.enums.ClientType;
import coupon.system.core.exceptions.CouponSystemException;
import coupon.system.core.login.manager.LoginManager;
import coupon.system.core.services.ClientService;
import coupon.system.core.session.Session;
import coupon.system.core.session.SessionContext;

@RestController
@RequestMapping("/login")
@CrossOrigin
public class LoginController {

	private SessionContext sessionContext;
	private LoginManager loginManager;

	public LoginController(SessionContext sessionContext, LoginManager loginManager) {
		super();
		this.sessionContext = sessionContext;
		this.loginManager = loginManager;
	}
	
	@PostMapping(path = "/login/{emailAddress}/{password}/{clientType}")
	private ResponseEntity<?> login(@PathVariable String emailAddress, @PathVariable String password,@PathVariable ClientType clientType) {

		ClientService clientService ;
		try {
			clientService = this.loginManager.login(clientType, emailAddress, password);
		} catch (CouponSystemException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Sorry, there was an error while trying to login -->> " + e.getMessage());
		}
		if(clientService!=null) {
			Session session = this.sessionContext.createSession();
			session.setAttributes("service", clientService);
			return ResponseEntity.ok(session.token);
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
				.body("Sorry, 'EmailAddress' OR 'Password' are incorrect.");
	}
	

	@PostMapping(path = "/log-out/{token}")
	private ResponseEntity<?> logOut(@PathVariable String token) {
		Session session = this.sessionContext.getSession(token);
		if (session != null) {
			this.sessionContext.sessionMap.remove(token);
			return ResponseEntity.ok("User logged out successfully");
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body("Sorry, there was a problem while trying to log out the user");
	}
	@GetMapping(path = "/is-active/{token}")
	private ResponseEntity<?> isActive(@PathVariable String token) {
		Session session = this.sessionContext.getSession(token);
		if (session != null) {
			return ResponseEntity.ok("This session is active");
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sorry, You are not logged-in");
	}
}
	

















// ****************************   OLD CODE - not relevant !!!  ****************************************************************
//
//	@PostMapping(path = "/administrator-login/{emailAddress}/{password}")
//	private ResponseEntity<?> loginAdmin(@PathVariable String emailAddress, @PathVariable String password) {
//		AdminService adminService;
//		try {
//			adminService = (AdminService) this.loginManager.login(ClientType.ADMIN, emailAddress, password);
//		} catch (CouponSystemException e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body("Sorry, there was an error while trying to login -->> " + e.getMessage());
//		}
//		if (adminService != null) {
//			Session session = this.sessionContext.createSession();
//			session.setAttributes("service", adminService);
//			return ResponseEntity.ok(session.token);
//		}
//		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//				.body("Sorry, 'EmailAddress' OR 'Password' are incorrect.");
//	}
//
//	@PostMapping(path = "/company-login/{emailAddress}/{password}")
//	private ResponseEntity<?> logincompany(@PathVariable String emailAddress, @PathVariable String password) {
//		CompanyService companyService;
//		try {
//			companyService = (CompanyService) this.loginManager.login(ClientType.COMPANY, emailAddress, password);
//		} catch (CouponSystemException e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body("Sorry, there was an error while trying to login -->> " + e.getMessage());
//		}
//		if (companyService != null) {
//			Session session = this.sessionContext.createSession();
//			session.setAttributes("service", companyService);
//			return ResponseEntity.ok(session.token);
//		}
//		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//				.body("Sorry, 'EmailAddress' OR 'Password' are incorrect.");
//	}
//
//	@PostMapping(path = "/customer-login/{emailAddress}/{password}")
//	private ResponseEntity<?> logincustomer(@PathVariable String emailAddress, @PathVariable String password) {
//		CustomerService customerService;
//		try {
//			customerService = (CustomerService) this.loginManager.login(ClientType.CUSTOMER, emailAddress, password);
//		} catch (CouponSystemException e) {
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//					.body("Sorry, there was an error while trying to login -->> " + e.getMessage());
//		}
//		if (customerService != null) {
//			Session session = this.sessionContext.createSession();
//			session.setAttributes("service", customerService);
//			return ResponseEntity.ok(session.token);
//		}
//		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//				.body("Sorry, 'EmailAddress' OR 'Password' are incorrect.");
//	}
//	
	
	
	
	
	
	
	
	
	


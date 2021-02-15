package coupon.system.core.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coupon.system.core.entities.Coupon;
import coupon.system.core.enums.Category;
import coupon.system.core.exceptions.CouponSystemException;
import coupon.system.core.services.CustomerService;
import coupon.system.core.session.SessionContext;

@RestController
@RequestMapping("api/customer")
@CrossOrigin
public class CustomerController {

	private SessionContext sessionContext;

	public CustomerController(SessionContext sessionContext) {
		super();
		this.sessionContext = sessionContext;
	}

	@PostMapping(path = "/purchase-coupon")
	public ResponseEntity<?> purchaseCoupon(@RequestBody Coupon coupon, @RequestHeader String token) {
		CustomerService custromerService = (CustomerService) this.sessionContext.getSession(token)
				.getAttributes("service");
		try {
			custromerService.purchaseCoupon(coupon);
			return ResponseEntity.ok("The customer successfully purchased the coupon");
		} catch (CouponSystemException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Sorry, can not purchase this coupon --->>> " + e.getMessage());
		}
	}

	@GetMapping(path = "/get-customer-coupons")
	public ResponseEntity<?> getCustomerCoupons(@RequestHeader String token) {
		CustomerService custromerService = (CustomerService) this.sessionContext.getSession(token)
				.getAttributes("service");
		try {
			return ResponseEntity.ok(custromerService.getCustomerCoupons());
		} catch (CouponSystemException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Sorry, can not get customer coupons -->> " + e.getMessage());
		}
	}

	@GetMapping(path = "/get-customer-coupons-by-category/{category}")
	public ResponseEntity<?> getCustomerCouponsByCategory(@PathVariable Category category,
			@RequestHeader String token) {
		CustomerService custromerService = (CustomerService) this.sessionContext.getSession(token)
				.getAttributes("service");

		try {
			return ResponseEntity.ok(custromerService.getCustomerCoupons(category));
		} catch (CouponSystemException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Sorry, can not get customer coupons by the Category -->> " + e.getMessage());

		}
	}

	@GetMapping(path = "/get-customer-coupons-by-max-price/{maxPrice}")
	public ResponseEntity<?> getCustomerCouponsByMaxPrice(@PathVariable Double maxPrice, @RequestHeader String token) {
		CustomerService custromerService = (CustomerService) this.sessionContext.getSession(token)
				.getAttributes("service");
		try {
			return ResponseEntity.ok(custromerService.getCustomerCoupons(maxPrice));
		} catch (CouponSystemException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Sorry, can not get customer coupons by the Max-Price -->> " + e.getMessage());

		}
	}

	@GetMapping(path = "/get-customer-details")
	public ResponseEntity<?> getCustomerDetails(@RequestHeader String token) {
		CustomerService custromerService = (CustomerService) this.sessionContext.getSession(token)
				.getAttributes("service");
		try {
			return ResponseEntity.ok(custromerService.getCustomerDetails());
		} catch (CouponSystemException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Sorry, can not get customer details -->> " + e.getMessage());

		}
	}

}

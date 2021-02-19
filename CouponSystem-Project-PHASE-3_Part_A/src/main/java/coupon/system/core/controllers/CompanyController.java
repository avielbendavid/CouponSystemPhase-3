package coupon.system.core.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import coupon.system.core.entities.Coupon;
import coupon.system.core.enums.Category;
import coupon.system.core.exceptions.CouponSystemException;
import coupon.system.core.services.CompanyService;
import coupon.system.core.session.SessionContext;

@RestController
@RequestMapping("/api/company")
@CrossOrigin
public class CompanyController {

	@Autowired
	private SessionContext sessionContext;

	@PostMapping("/add-coupon")
	public ResponseEntity<?> addCoupon(@RequestBody Coupon coupon, @RequestHeader String token) {
		CompanyService companyService = (CompanyService) sessionContext.getSession(token).getAttributes("service");
		try {
			companyService.addCoupon(coupon);
			return ResponseEntity.ok("Coupon added successfully");
		} catch (CouponSystemException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Sorry, can not add the coupon --->>> " + e.getMessage());
		}
	}

	@PutMapping("/update-coupon")
	
	
	
	
	
	public ResponseEntity<?> updateCoupon(@RequestBody Coupon coupon, @RequestHeader String token) {
		CompanyService companyService = (CompanyService) sessionContext.getSession(token).getAttributes("service");
		try {
			companyService.updateCoupon(coupon);
			return ResponseEntity.ok("Coupon updated successfully");
		} catch (CouponSystemException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sorry, can not update the coupon --->>> " + e.getMessage());
		}
	}

	@DeleteMapping("/delete-coupon/{couponId}")
	public ResponseEntity<?> deleteCoupon(@PathVariable Integer couponId, @RequestHeader String token) {
		CompanyService companyService = (CompanyService) sessionContext.getSession(token).getAttributes("service");
		try {
			companyService.deleteCoupon(couponId);
			return ResponseEntity.ok("coupon deleted successfully");
		} catch (CouponSystemException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sorry, can not delete the coupon [" + couponId + "] --->>> " + e.getMessage());
		}
	}

	@GetMapping("/get-company-coupons")
	public ResponseEntity<?> getCompanyCoupons(@RequestHeader String token) {
		CompanyService companyService = (CompanyService) sessionContext.getSession(token).getAttributes("service");
		List<Coupon> companyCoupons = new ArrayList<>();
		try {
			companyCoupons = companyService.getCompanyCoupons();
			return ResponseEntity.ok(companyCoupons);
		} catch (CouponSystemException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sorry, can not get company coupons --->>> "+e.getMessage());
		}
	}

	@GetMapping("/get-company-coupons-by-category/{category}")
	public ResponseEntity<?> getCompanyCouponsByCategory(@PathVariable Category category, @RequestHeader String token) {
		CompanyService companyService = (CompanyService) sessionContext.getSession(token).getAttributes("service");
		List<Coupon> companyCouponsByCategory = new ArrayList<>();
		try {
			companyCouponsByCategory = companyService.getCompanyCoupons(category);
			return ResponseEntity.ok(companyCouponsByCategory);
		} catch (CouponSystemException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sorry, can not get company coupons by the Category --->>> "+e.getMessage());
		}
	}

	@GetMapping("/get-company-coupons-by-max-price/{price}")
	public ResponseEntity<?> getCompanyCouponsByMaxPrice(@PathVariable Double price, @RequestHeader String token) {
		CompanyService companyService = (CompanyService) sessionContext.getSession(token).getAttributes("service");
		List<Coupon> companyCouponsByMaxPrice = new ArrayList<>();
		try {
			companyCouponsByMaxPrice = companyService.getCompanyCoupons(price);
			return ResponseEntity.ok(companyCouponsByMaxPrice);
		} catch (CouponSystemException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sorry, can not get company coupons by the MaxPrice --->>> "+e.getMessage());
		}
	}

	@GetMapping("/get-company")
	public ResponseEntity<?> getCompanyDetails(@RequestHeader String token) {
		CompanyService companyService = (CompanyService) sessionContext.getSession(token).getAttributes("service");
		Company company;
		try {
			company = companyService.getCompanyDetails();
			return ResponseEntity.ok(company);
		} catch (CouponSystemException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Sorry, can not get company details --->>> "+e.getMessage());
		}
	}

}

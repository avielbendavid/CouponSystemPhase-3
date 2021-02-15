



package coupon.system.core.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import coupon.system.core.entities.Coupon;
import coupon.system.core.exceptions.CouponSystemException;
import coupon.system.core.services.HomeService;

@RestController
@RequestMapping("/home")
@CrossOrigin
public class HomeController {

	private HomeService homeService;

	public HomeController(HomeService homeService) {
		super();
		this.homeService = homeService;
	}

	@GetMapping(path = "/get-all-coupons")
	private ResponseEntity<?> getAllCoupons() {
		try {
			List<Coupon> coupons = this.homeService.getAllCoupons();
			return ResponseEntity.ok(coupons);
		} catch (CouponSystemException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body("Sorry there was a problem while trying to get all coupons --- >>> " + e.getMessage());
		}

	}

}

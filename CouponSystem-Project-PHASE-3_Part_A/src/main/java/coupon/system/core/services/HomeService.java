package coupon.system.core.services;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import coupon.system.core.entities.Coupon;
import coupon.system.core.exceptions.CouponSystemException;
import coupon.system.core.repositories.CouponRepository;

@Service
@Transactional
public class HomeService {

	private CouponRepository couponRepository;

	public HomeService(CouponRepository couponRepository) {
		super();
		this.couponRepository = couponRepository;
	}

	public List<Coupon> getAllCoupons() throws CouponSystemException {
		List<Coupon> coupons = new ArrayList<>();
		try {
			coupons = this.couponRepository.findAll();
			return coupons;
		} catch (Exception e) {
			throw new CouponSystemException("There was a problem while trying to get all coupons --->>> " + e.getMessage(), e);
		}
	}

}

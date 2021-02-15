package coupon.system.core.repositories;

import java.time.LocalDate;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;

import coupon.system.core.entities.Coupon;
import coupon.system.core.enums.Category;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {

	public abstract List<Coupon> findByCompanyId(Integer companyId);

	public abstract List<Coupon> findByCompanyIdAndCategory(Integer companyId, Category category);

	public abstract List<Coupon> findByCompanyIdAndPriceLessThanEqual(Integer companyId, Double maxPrice);

	public abstract List<Coupon> findByCustomersId(Integer customerId);

	public abstract List<Coupon> findByCustomersIdAndCategory(Integer customerId, Category category);

	public abstract List<Coupon> findByCustomersIdAndPriceLessThanEqual(Integer customerId, Double maxPrice);

	public abstract boolean existsByIdAndCompanyId(Integer couponId, Integer companyId);

	@Transactional
	public abstract void deleteByEndDateBefore(LocalDate localDate);
}

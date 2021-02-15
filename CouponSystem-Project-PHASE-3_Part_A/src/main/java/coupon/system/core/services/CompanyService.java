package coupon.system.core.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import coupon.system.core.entities.Company;
import coupon.system.core.entities.Coupon;
import coupon.system.core.enums.Category;
import coupon.system.core.exceptions.CouponSystemException;
import coupon.system.core.repositories.CompanyRepository;
import coupon.system.core.repositories.CouponRepository;

@Service
@Transactional
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class CompanyService extends ClientService {

	/**
	 * This primitive instance variable - 'companyId'(int)- is the Id of the
	 * 'COMPANY' client that Logged-in to 'CouponSystem'. if 'COMPANY' client
	 * performed Log-in successfully the variable [companyId] will initialize to the
	 * company Id of the company that logged-in to the 'CouponSystem'.
	 */
	private int companyId;

	private CompanyRepository companyRepository;
	private CouponRepository couponRepository;

	public CompanyService(CompanyRepository companyRepository, CouponRepository couponRepository) {
		super();
		this.companyRepository = companyRepository;
		this.couponRepository = couponRepository;
	}

	/**
	 * This method will check the company details - if entered email address and
	 * entered password match to the values in DB. if the details correct - this
	 * method will return 'true' and will initialize the companyId parameter of this
	 * class to the current company Id. Otherwise return 'false'.
	 * 
	 * @param email    - the email address of the company.
	 * @param password - the password of the company.
	 * @return boolean - 'true' if entered values are correct . Otherwise return
	 *         'false'.
	 * @throws CouponSystemException
	 */
	public boolean login(String email, String password) throws CouponSystemException {
		try {
			Optional<Company> opt = companyRepository.findByEmailAndPassword(email, password);
			if (opt.isPresent()) {
				Company companyFromDB = opt.get();
				this.companyId = companyFromDB.getId();
				return true;
			}
			return false;
		} catch (Exception e) {
			throw new CouponSystemException("There was an error while trying to login --->>> " + e.getMessage(), e);
		}
	}

	/**
	 * @param coupon
	 * @throws CouponSystemException
	 */
	public void addCoupon(Coupon coupon) throws CouponSystemException {
		try {
			if (!companyRepository.existsByIdAndCouponsTitle(companyId, coupon.getTitle())) {
				if (coupon.getAmount() <= 0) {
					throw new CouponSystemException(
							"Can not add coupon ['" + coupon.getTitle() + "'] because the amount is [< or = to 0].");
				}
				if (coupon.getEndDate().isBefore(LocalDate.now())) {
					throw new CouponSystemException(
							"Can not add coupon ['" + coupon.getTitle() + "'] because the coupon is expired.");
				}
				if (coupon.getStartDate().isAfter(coupon.getEndDate())) {
					throw new CouponSystemException("Can not add coupon ['" + coupon.getTitle()
							+ "'] because the 'coupon startDate' is after 'coupon endDate'.");
				}
				Company currCompany = companyRepository.findById(companyId).get();
				currCompany.addCoupon(coupon);
				return;
			}
			throw new CouponSystemException("Can not add coupon ['" + coupon.getTitle() + "'] because company [ id: "
					+ companyId + "] has already a coupon with the same coupon Title.");
		} catch (Exception e) {
			throw new CouponSystemException(
					"There was an error while trying to add the coupon --->>> " + e.getMessage(), e);

		}
	}

	public void updateCoupon(Coupon coupon) throws CouponSystemException {
		try {

			if (companyRepository.existsByIdAndCouponsId(companyId, coupon.getId())) {

				Coupon couponFromDB = couponRepository.findById(coupon.getId()).get();

				if (coupon.getAmount() <= 0) {
					throw new CouponSystemException("Can not add coupon ['" + coupon.getTitle() + "',id: "
							+ coupon.getId() + "] because the amount is [< or = to 0].");
				}
				if (coupon.getEndDate().isBefore(LocalDate.now())) {
					throw new CouponSystemException("Can not add coupon ['" + coupon.getTitle() + "',id: "
							+ coupon.getId() + "] because the coupon is expired.");
				}
				if (coupon.getStartDate().isAfter(coupon.getEndDate())) {
					throw new CouponSystemException("Can not add coupon ['" + coupon.getTitle() + "',id: "
							+ coupon.getId() + "] because the startDate is after endDate.");
				}
				couponFromDB.setAmount(coupon.getAmount());
				couponFromDB.setCategory(coupon.getCategory());
				couponFromDB.setDescription(coupon.getDescription());
				couponFromDB.setEndDate(coupon.getEndDate());
				couponFromDB.setStartDate(coupon.getStartDate());
				couponFromDB.setImage(coupon.getImage());
				couponFromDB.setPrice(coupon.getPrice());
				couponFromDB.setTitle(coupon.getTitle());
				return;
			}
			throw new CouponSystemException("Can not update coupon '" + coupon.getTitle() + "' [id:" + coupon.getId()
					+ "] becaus coupon does not belongs to company " + companyId + ".");
		} catch (Exception e) {
			throw new CouponSystemException(
					"There was an error while trying to update the coupon --->>> " + e.getMessage(), e);

		}
	}

	/**
	 * @param couponId
	 * @throws CouponSystemException
	 */
	public void deleteCoupon(Integer couponId) throws CouponSystemException {
		try {
			if (couponRepository.existsByIdAndCompanyId(couponId, this.companyId)) {
				couponRepository.deleteById(couponId);
				return;
			}
			throw new CouponSystemException("Can not delete coupon [ id : " + couponId
					+ " ] because the coupon does not belong to the company [ id: " + companyId + " ].");
		} catch (Exception e) {
			throw new CouponSystemException(
					"There was an error while trying to delete the coupon --->>> " + e.getMessage(), e);

		}
	}

	/**
	 * @return
	 * @throws CouponSystemException
	 */
	public List<Coupon> getCompanyCoupons() throws CouponSystemException {
		try {
			return couponRepository.findByCompanyId(this.companyId);
		} catch (Exception e) {
			throw new CouponSystemException(
					"There was an error while trying to get company coupons --->>> " + e.getMessage(), e);

		}
	}

	/**
	 * This method will return a list of all Company Coupons with specific
	 * 'Category'. if the list is empty- this method will return an empty List.
	 * 
	 * @param category - the desired 'Category'.
	 * @return List< Coupon> - return a List of all Company Coupons with specific
	 *         'Category'.
	 * @throws CouponSystemException
	 */
	public List<Coupon> getCompanyCoupons(Category category) throws CouponSystemException {
		try {
			return couponRepository.findByCompanyIdAndCategory(this.companyId, category);
		} catch (Exception e) {
			throw new CouponSystemException(
					"There was an error while trying to get company coupons by Category --->>> " + e.getMessage(), e);
		}
	}

	/**
	 * This method will return a list of all Company Coupons with specific
	 * 'MaxPrice'. if the list is empty- this method will return an empty List.
	 * 
	 * @param category - the desired 'Category'.
	 * @return List< Coupon> - return a List of all Company Coupons with specific
	 *         'Category'.
	 * @throws CouponSystemException
	 */
	public List<Coupon> getCompanyCoupons(Double maxPrice) throws CouponSystemException {
		try {
			return couponRepository.findByCompanyIdAndPriceLessThanEqual(this.companyId, maxPrice);
		} catch (Exception e) {
			throw new CouponSystemException(
					"There was an error while trying to get company coupons by MaxPrice --->>> " + e.getMessage(), e);

		}
	}

	/**
	 * @return
	 * @throws CouponSystemException
	 */
	public Company getCompanyDetails() throws CouponSystemException {
		try {
			return companyRepository.findById(companyId).get();
		} catch (Exception e) {
			throw new CouponSystemException(
					"There was an error while trying to get company details --->>> " + e.getMessage(), e);
		}
	}

}

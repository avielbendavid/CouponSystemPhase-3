package coupon.system.core.services;

import coupon.system.core.exceptions.CouponSystemException;

public abstract class ClientService {

	public abstract boolean login(String email, String password) throws CouponSystemException;

}

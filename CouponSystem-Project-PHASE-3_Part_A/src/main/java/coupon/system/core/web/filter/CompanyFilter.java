package coupon.system.core.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;

import coupon.system.core.services.CompanyService;
import coupon.system.core.session.Session;
import coupon.system.core.session.SessionContext;

public class CompanyFilter implements Filter {

	private SessionContext sessionContext;

	public CompanyFilter(SessionContext sessionContext) {
		super();
		this.sessionContext = sessionContext;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse resp = (HttpServletResponse) response;

		String token = req.getHeader("token");
		if (token != null) {
			System.out.println("there is a token on the header");
			Session session = sessionContext.getSession(token);
			if (session != null) {
				Object companyService = session.getAttributes("service");

				if (companyService instanceof CompanyService) {
					chain.doFilter(request, response);
					return;
				} else {
					resp.sendError(HttpStatus.UNAUTHORIZED.value(), "You are not Company - Blocking the request");
					return;
				}
			}
		}
		if (req.getMethod().equalsIgnoreCase("OPTIONS")) {
			System.out.println("this is preflight : " + req.getMethod());
			chain.doFilter(request, response);
		} else {
			System.out.println("no session - blocking the request");
			resp.setHeader("Access-Control-Allow-Origin", "http://localhost:4200");
			resp.sendError(HttpStatus.UNAUTHORIZED.value(), "You are not logged in");
		}
	}
}

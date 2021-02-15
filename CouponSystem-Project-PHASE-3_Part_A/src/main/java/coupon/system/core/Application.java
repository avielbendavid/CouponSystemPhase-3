package coupon.system.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import coupon.system.core.session.SessionContext;
import coupon.system.core.web.filter.AdminFilter;
import coupon.system.core.web.filter.CompanyFilter;
import coupon.system.core.web.filter.CustomerFilter;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication(scanBasePackages = { "coupon.system.core", "coupon.system.operations.after.system.boot" })
@PropertySource({ "classpath:application.properties", "classpath:adminClient.properties",
		"classpath:session.properties" })
@EnableSwagger2
public class Application {

	public static void main(String[] args) {

		SpringApplication.run(Application.class, args);

//		ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
//			Thread.sleep(20_000); // I will give the thread a sleep for 20 seconds to give the daily job run
		// several cycles.

//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	@Bean
	public FilterRegistrationBean<AdminFilter> adminFilterRegistrationBean(SessionContext sessionContext) {
		FilterRegistrationBean<AdminFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		AdminFilter adminFilter = new AdminFilter(sessionContext);
		filterRegistrationBean.setFilter(adminFilter);
		filterRegistrationBean.addUrlPatterns("/api/admin/*");
		return filterRegistrationBean;
	}
	@Bean
	public FilterRegistrationBean<CompanyFilter> companyFilterRegistrationBean(SessionContext sessionContext) {
		FilterRegistrationBean<CompanyFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		CompanyFilter companyFilter = new CompanyFilter(sessionContext);
		filterRegistrationBean.setFilter(companyFilter);
		filterRegistrationBean.addUrlPatterns("/api/company/*");
		return filterRegistrationBean;
	}
	@Bean
	public FilterRegistrationBean<CustomerFilter> customerFilterRegistrationBean(SessionContext sessionContext) {
		FilterRegistrationBean<CustomerFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		CustomerFilter customerFilter = new CustomerFilter(sessionContext);
		filterRegistrationBean.setFilter(customerFilter);
		filterRegistrationBean.addUrlPatterns("/api/customer/*");
		return filterRegistrationBean;
	}

	
	@Bean
	public Docket docket() {
		return new Docket(DocumentationType.SWAGGER_2).select().apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any()).build();
	}
}

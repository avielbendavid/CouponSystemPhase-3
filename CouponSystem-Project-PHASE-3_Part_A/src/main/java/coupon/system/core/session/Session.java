package coupon.system.core.session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class Session {

	public final String token = UUID.randomUUID().toString().replace("-", "").substring(0, MAX_TOKEN_LENGHT);
	private Map<String, Object> attributes = new HashMap<>();
	private long lastAccesses;
	private static final int MAX_TOKEN_LENGHT = 15;
	@Value("${session.max.inactive.interval:5}")
	private long maxInactiveInterval;

	{
		this.resetLastAccessed();
	}

	public void resetLastAccessed() {
		this.lastAccesses = System.currentTimeMillis();
	}

	@PostConstruct
	public void init() {
		this.maxInactiveInterval = TimeUnit.MINUTES.toMillis(maxInactiveInterval);
	}

	public void setAttributes(String name, Object object) {
		this.attributes.put(name, object);
	}

	public Object getAttributes(String name) {
		return this.attributes.get(name);
	}

	public long getLastAccessed() {
		return lastAccesses;
	}

	public void setLastAccessed(long lastAccesses) {
		this.lastAccesses = lastAccesses;
	}

	public long getMaxInactiveInterval() {
		return maxInactiveInterval;
	}

	public void setMaxInactiveInterval(long maxInactiveInterval) {
		this.maxInactiveInterval = maxInactiveInterval;
	}


}

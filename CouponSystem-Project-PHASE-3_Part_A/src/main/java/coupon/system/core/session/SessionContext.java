package coupon.system.core.session;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class SessionContext {
	@Autowired
	private ApplicationContext ctx;
	public Map<String, Session> sessionMap = new ConcurrentHashMap<>();
	private Timer timer = new Timer();
	@Value("${session.remove.expired.period:60}")
	private int removeExpiredPeriod;

	private boolean isSessionExpired(Session session) {
		return System.currentTimeMillis() - session.getLastAccessed() > session.getMaxInactiveInterval();
	}

	@PostConstruct
	public void init() {

		TimerTask task = new TimerTask() {

			@Override
			public void run() {
				System.out.println("removing expired sessions");
				for (Session session : sessionMap.values()) {
					if (isSessionExpired(session)) {
						invalidate(session);
					}
				}
			}
		};
		timer.schedule(task, 20_000, TimeUnit.SECONDS.toMillis(removeExpiredPeriod));
	}

	@PreDestroy
	public void destroy() {
		this.timer.cancel();
	}

	public Session createSession() {
		Session session = ctx.getBean(Session.class);
		this.sessionMap.put(session.token, session);
		return session;
	}

	public void invalidate(Session session) {
		this.sessionMap.remove(session.token);
	}

	public Session getSession(String token) {
		Session session = sessionMap.get(token);
		if (session != null) {
			if (isSessionExpired(session)) {
				invalidate(session);
				return null;
			}
			session.resetLastAccessed();
			return session;
		}
		return null;
	}
}

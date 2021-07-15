package cn.cmd.opensso.server.config;

import cn.cmd.opensso.client.SsoContainer;
import cn.cmd.opensso.client.filter.LoginFilter;
import cn.cmd.opensso.client.filter.LogoutFilter;
import cn.cmd.opensso.client.listener.LogoutListener;
import cn.cmd.opensso.client.session.SessionMappingStorage;
import cn.cmd.opensso.server.session.redis.RedisSessionMappingStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.events.AbstractSessionEvent;
import org.springframework.session.web.http.SessionEventHttpSessionListenerAdapter;

import javax.servlet.http.HttpSessionListener;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ConditionalOnProperty(name = "sso.server.session.manager", havingValue = "redis")
public class SsoRedisConfig {

	@Value("${sso.server.url}")
	private String serverUrl;
	@Value("${sso.server.appId}")
	private String appId;
	@Value("${sso.server.appSecret}")
	private String appSecret;
	@Value("${sso.server.excludeUrls}")
	private String excludeUrls;

	@Autowired
	private SessionMappingStorage sessionMappingStorage;

	@Bean
	public SessionMappingStorage sessionMappingStorage() {
		return new RedisSessionMappingStorage();
	}

	@Bean
	public ApplicationListener<AbstractSessionEvent> LogoutListener() {
		List<HttpSessionListener> httpSessionListeners = new ArrayList<>();
		LogoutListener logoutListener = new LogoutListener();
		logoutListener.setSessionMappingStorage(sessionMappingStorage);
		httpSessionListeners.add(logoutListener);
		return new SessionEventHttpSessionListenerAdapter(httpSessionListeners);
	}

	/**
	 * Web支持单点登录Filter容器
	 */
	@Bean
	@SuppressWarnings("all")
	public FilterRegistrationBean<SsoContainer> ssoContainer() {
		SsoContainer container = new SsoContainer();
		container.setServerUrl(serverUrl);
		container.setAppId(appId);
		container.setAppSecret(appSecret);
		// 忽略拦截URL,多个逗号分隔
		container.setExcludeUrls(excludeUrls);
		container.setFilters(new LogoutFilter(), new LoginFilter());

		FilterRegistrationBean<SsoContainer> registration = new FilterRegistrationBean<>();
		registration.setFilter(container);
		registration.addUrlPatterns("/*");
		registration.setOrder(1);
		registration.setName("ssoContainer");
		return registration;
	}

}

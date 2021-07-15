package cn.cmd.opensso.server.config;

import cn.cmd.opensso.client.SsoContainer;
import cn.cmd.opensso.client.filter.LoginFilter;
import cn.cmd.opensso.client.filter.LogoutFilter;
import cn.cmd.opensso.client.listener.LogoutListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.http.HttpSessionListener;

@Configuration
@ConditionalOnProperty(name = "sso.server.session.manager", havingValue = "local")
public class SsoLocalConfig {

	@Value("${sso.server.url}")
	private String serverUrl;
	@Value("${sso.server.appId}")
	private String appId;
	@Value("${sso.server.appSecret}")
	private String appSecret;
	@Value("${sso.server.excludeUrls}")
	private String excludeUrls;

	/**
	 * 单实例方式单点登出Listener
	 * 
	 * @return
	 */
	@Bean
	public ServletListenerRegistrationBean<HttpSessionListener> LogoutListener() {
		ServletListenerRegistrationBean<HttpSessionListener> listenerRegBean = new ServletListenerRegistrationBean<>();
		LogoutListener logoutListener = new LogoutListener();
		listenerRegBean.setListener(logoutListener);
		return listenerRegBean;
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

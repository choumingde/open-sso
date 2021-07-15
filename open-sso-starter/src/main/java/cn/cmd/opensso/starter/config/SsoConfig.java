package cn.cmd.opensso.starter.config;

import cn.cmd.opensso.client.SsoContainer;
import cn.cmd.opensso.client.filter.LoginFilter;
import cn.cmd.opensso.client.filter.LogoutFilter;
import cn.cmd.opensso.starter.properties.SsoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(SsoProperties.class)
public class SsoConfig {

    @Autowired
	private SsoProperties ssoProperties;
    
    /**
     * Web支持单点登录Filter容器
     */
    @Bean
	@SuppressWarnings("all")
    public FilterRegistrationBean<SsoContainer> ssoContainer() {
		SsoContainer container = new SsoContainer();
		container.setServerUrl(ssoProperties.getUrl());
		container.setAppId(ssoProperties.getAppId());
		container.setAppSecret(ssoProperties.getAppSecret());
        // 忽略拦截URL,多个逗号分隔
		container.setExcludeUrls(ssoProperties.getExcludeUrls());
		container.setFilters(new LogoutFilter(), new LoginFilter());

        FilterRegistrationBean<SsoContainer> registration = new FilterRegistrationBean<>();
        registration.setFilter(container);
        registration.addUrlPatterns("/*");
        registration.setOrder(1);
        registration.setName("ssoContainer");
        return registration;
    }
    
}

package cn.cmd.opensso.server.session.redis;

import java.util.concurrent.TimeUnit;

import cn.cmd.opensso.server.common.RefreshTokenContent;
import cn.cmd.opensso.server.session.RefreshTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 分布式刷新凭证管理
 * 
 */
@Component
@ConditionalOnProperty(name = "sso.server.session.manager", havingValue = "redis")
public class RedisRefreshTokenManager implements RefreshTokenManager {
	
	@Value("${sso.server.timeout:7200}")
    private int timeout;
	@Autowired
	private StringRedisTemplate redisTemplate;

	@Override
	public void create(String refreshToken, RefreshTokenContent refreshTokenContent) {
		redisTemplate.opsForValue().set(refreshToken, JSON.toJSONString(refreshTokenContent), getExpiresIn(),
				TimeUnit.SECONDS);
	}

	@Override
	public RefreshTokenContent validate(String refreshToken) {
		String rtc = redisTemplate.opsForValue().get(refreshToken);
		if (!StringUtils.isEmpty(rtc)) {
			redisTemplate.delete(refreshToken);
		}
		return JSONObject.parseObject(rtc, RefreshTokenContent.class);
	}
	
	/*
	 * refreshToken时效和登录session时效一致
	 */
	@Override
	public int getExpiresIn() {
		return timeout;
	}
}

package cn.cmd.opensso.server.session.redis;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import cn.cmd.opensso.server.common.AccessTokenContent;
import cn.cmd.opensso.server.session.AccessTokenManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 分布式调用凭证管理
 * 
 */
@Component
@ConditionalOnProperty(name = "sso.server.session.manager", havingValue = "redis")
public class RedisAccessTokenManager implements AccessTokenManager {
	
	@Value("${sso.server.timeout:7200}")
    private int timeout;
	@Autowired
	private StringRedisTemplate redisTemplate;

	@Override
	public void create(String accessToken, AccessTokenContent accessTokenContent) {
		redisTemplate.opsForValue().set(accessToken, JSON.toJSONString(accessTokenContent), getExpiresIn(),
				TimeUnit.SECONDS);

		redisTemplate.opsForSet().add(getKey(accessTokenContent.getCodeContent().getTgt()), accessToken);
	}
	
	@Override
	public AccessTokenContent get(String accessToken) {
		String atcStr = redisTemplate.opsForValue().get(accessToken);
		if (StringUtils.isEmpty(atcStr)) {
			return null;
		}
		return JSONObject.parseObject(atcStr, AccessTokenContent.class);
	}

	@Override
	public boolean refresh(String accessToken) {
		if (redisTemplate.opsForValue().get(accessToken) == null) {
			return false;
		}
		redisTemplate.expire(accessToken, timeout, TimeUnit.SECONDS);
        return true;
	}

	@Override
	public void remove(String tgt) {
		Set<String> accessTokenSet = redisTemplate.opsForSet().members(getKey(tgt));
		if (CollectionUtils.isEmpty(accessTokenSet)) {
			return;
		}
		redisTemplate.delete(getKey(tgt));
		
		accessTokenSet.forEach(accessToken -> {
			String atcStr = redisTemplate.opsForValue().get(accessToken);
			if (StringUtils.isEmpty(atcStr)) {
				return;
			}
			AccessTokenContent accessTokenContent = JSONObject.parseObject(atcStr, AccessTokenContent.class);
			if (accessTokenContent == null || !accessTokenContent.getCodeContent().isSendLogoutRequest()) {
				return;
			}
			sendLogoutRequest(accessTokenContent.getCodeContent().getRedirectUri(), accessToken);
		});
	}
	
	private String getKey(String tgt) {
		return tgt + "_access_token";
	}
	
	/**
	 * accessToken时效为登录session时效的1/2
	 */
	@Override
	public int getExpiresIn() {
		return timeout / 2;
	}
}

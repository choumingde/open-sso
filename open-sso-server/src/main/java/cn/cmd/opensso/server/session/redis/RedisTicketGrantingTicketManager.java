package cn.cmd.opensso.server.session.redis;

import java.util.concurrent.TimeUnit;

import cn.cmd.opensso.server.session.TicketGrantingTicketManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cn.cmd.opensso.client.rpc.SsoUser;

/**
 * 分布式登录凭证管理
 * 
 */
@Component
@ConditionalOnProperty(name = "sso.server.session.manager", havingValue = "redis")
public class RedisTicketGrantingTicketManager implements TicketGrantingTicketManager {
	
	@Value("${sso.server.timeout:7200}")
    private int timeout;
	@Autowired
	private StringRedisTemplate redisTemplate;

	@Override
	public void create(String tgt, SsoUser user) {
		redisTemplate.opsForValue().set(tgt, JSON.toJSONString(user), getExpiresIn(),
				TimeUnit.SECONDS);
	}

	@Override
	public SsoUser getAndRefresh(String tgt) {
		String user = redisTemplate.opsForValue().get(tgt);
		if (StringUtils.isEmpty(user)) {
			return null;
		}
		redisTemplate.expire(tgt, timeout, TimeUnit.SECONDS);
		return JSONObject.parseObject(user, SsoUser.class);
	}
	
	@Override
	public void set(String tgt, SsoUser user) {
		create(tgt, user);
	}

	@Override
	public void remove(String tgt) {
		redisTemplate.delete(tgt);
	}

	@Override
	public int getExpiresIn() {
		return timeout;
	}
}
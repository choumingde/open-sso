package cn.cmd.opensso.client.session;

import cn.cmd.opensso.client.rpc.RpcAccessToken;
import cn.cmd.opensso.client.rpc.SsoUser;

/**
 * 存Session中AccessToken
 */
public class SessionAccessToken extends RpcAccessToken {

	private static final long serialVersionUID = 4507869346123296527L;

	/**
	 * AccessToken过期时间
	 */
	private long expirationTime;

	public SessionAccessToken(String accessToken, int expiresIn, String refreshToken, SsoUser user,
			long expirationTime) {
		super(accessToken, expiresIn, refreshToken, user);
		this.expirationTime = expirationTime;
	}

	public long getExpirationTime() {
		return expirationTime;
	}

	public void setExpirationTime(long expirationTime) {
		this.expirationTime = expirationTime;
	}

	public boolean isExpired() {
		return System.currentTimeMillis() > expirationTime;
	}
}
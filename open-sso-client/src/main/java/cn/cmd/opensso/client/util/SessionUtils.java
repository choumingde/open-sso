package cn.cmd.opensso.client.util;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import cn.cmd.opensso.client.rpc.RpcAccessToken;
import cn.cmd.opensso.client.rpc.SsoUser;
import cn.cmd.opensso.client.constant.SsoConstant;
import cn.cmd.opensso.client.session.SessionAccessToken;

/**
 * Session工具
 */
public class SessionUtils {
    
    public static SessionAccessToken getAccessToken(HttpServletRequest request) {
        return (SessionAccessToken) request.getSession().getAttribute(SsoConstant.SESSION_ACCESS_TOKEN);
    }
    
	public static SsoUser getUser(HttpServletRequest request) {
	    return Optional.ofNullable(getAccessToken(request)).map(u -> u.getUser()).orElse(null);
	}
	
	public static Integer getUserId(HttpServletRequest request) {
        return Optional.ofNullable(getUser(request)).map(u -> u.getId()).orElse(null);
    }

	public static void setAccessToken(HttpServletRequest request, RpcAccessToken rpcAccessToken) {
		SessionAccessToken sessionAccessToken = null;
		if (rpcAccessToken != null) {
			sessionAccessToken = createSessionAccessToken(rpcAccessToken);
		}
		request.getSession().setAttribute(SsoConstant.SESSION_ACCESS_TOKEN, sessionAccessToken);
	}

	private static SessionAccessToken createSessionAccessToken(RpcAccessToken accessToken) {
		long expirationTime = System.currentTimeMillis() + accessToken.getExpiresIn() * 1000;
		return new SessionAccessToken(accessToken.getAccessToken(), accessToken.getExpiresIn(),
				accessToken.getRefreshToken(), accessToken.getUser(), expirationTime);
	}

	public static void invalidate(HttpServletRequest request) {
		setAccessToken(request, null);
		request.getSession().invalidate();
	}
}
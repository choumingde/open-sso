package cn.cmd.opensso.client.filter;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.cmd.opensso.client.util.SessionUtils;
import cn.cmd.opensso.client.constant.SsoConstant;
import cn.cmd.opensso.client.session.SessionAccessToken;

/**
 * APP单点登录Filter
 */
public class AppLoginFilter extends LoginFilter {

	@Override
	public boolean isAccessAllowed(HttpServletRequest request, HttpServletResponse response) throws IOException {
		SessionAccessToken sessionAccessToken = SessionUtils.getAccessToken(request);
		// 本地Session中已存在，且accessToken没过期或者refreshToken成功，直接返回
		if (sessionAccessToken != null && (!sessionAccessToken.isExpired()
				|| refreshToken(sessionAccessToken.getRefreshToken(), request))) {
			return true;
		}
		responseJson(response, SsoConstant.NO_LOGIN, "未登录或已超时");
		return false;
	}
}
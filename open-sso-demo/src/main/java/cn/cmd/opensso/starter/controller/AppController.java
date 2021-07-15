package cn.cmd.opensso.starter.controller;

import javax.servlet.http.HttpServletRequest;

import cn.cmd.opensso.starter.properties.SsoProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cn.cmd.opensso.client.constant.Oauth2Constant;
import cn.cmd.opensso.client.rpc.Result;
import cn.cmd.opensso.client.rpc.RpcAccessToken;
import cn.cmd.opensso.client.rpc.SsoUser;
import cn.cmd.opensso.client.util.Oauth2Utils;
import cn.cmd.opensso.client.util.SessionUtils;

/**
 *
 */
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/app")
public class AppController {
	
	@Autowired
	private SsoProperties ssoProperties;

	/**
	 * 初始页
	 * @param request
	 * @return
	 */
    @RequestMapping
	public Result index(HttpServletRequest request) {
		SsoUser user = SessionUtils.getUser(request);
		return Result.createSuccess(user);
	}
	
	/**
	 * 登录提交
	 * 
	 * @param username
	 * @param password
	 * @param request
	 * @return
	 */
	@RequestMapping("/login")
	public Result login(
			@RequestParam(value = Oauth2Constant.USERNAME, required = true) String username,
			@RequestParam(value = Oauth2Constant.PASSWORD, required = true) String password,
			HttpServletRequest request) {
		Result<RpcAccessToken> result = Oauth2Utils.getAccessToken(ssoProperties.getUrl(), ssoProperties.getAppId(), ssoProperties.getAppSecret(), username, password);
		if (!result.isSuccess()) {
			return result;
		}
		SessionUtils.setAccessToken(request, result.getData());
		return Result.createSuccess().setMessage("登录成功");
	}
}

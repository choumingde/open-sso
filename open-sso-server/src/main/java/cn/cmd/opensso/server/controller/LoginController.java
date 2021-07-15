package cn.cmd.opensso.server.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.cmd.opensso.server.constant.AppConstant;
import cn.cmd.opensso.server.service.AppService;
import cn.cmd.opensso.server.service.UserService;
import cn.cmd.opensso.server.session.CodeManager;
import cn.cmd.opensso.server.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.cmd.opensso.client.constant.Oauth2Constant;
import cn.cmd.opensso.client.constant.SsoConstant;
import cn.cmd.opensso.client.rpc.Result;
import cn.cmd.opensso.client.rpc.SsoUser;

/**
 * 单点登录管理
 *
 */
@Controller
@RequestMapping("/login")
public class LoginController{

	@Autowired
	private CodeManager codeManager;
	@Autowired
	private SessionManager sessionManager;
	@Autowired
	private UserService userService;
	@Autowired
	private AppService appService;

	/**
	 * 登录页
	 * 
	 * @param redirectUri
	 * @param appId
	 * @param request
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String login(
			@RequestParam(value = SsoConstant.REDIRECT_URI, required = true) String redirectUri,
			@RequestParam(value = Oauth2Constant.APP_ID, required = true) String appId,
			HttpServletRequest request) throws UnsupportedEncodingException {
		String tgt = sessionManager.getTgt(request);
		if (StringUtils.isEmpty(tgt)) {
			return goLoginPath(redirectUri, appId, request);
		}
		return generateCodeAndRedirect(redirectUri, tgt);
	}
	
	/**
	 * 登录提交
	 * 
	 * @param redirectUri
	 * @param appId
	 * @param username
	 * @param password
	 * @param request
	 * @param response
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	@RequestMapping(method = RequestMethod.POST)
	public String login(
			@RequestParam(value = SsoConstant.REDIRECT_URI, required = true) String redirectUri,
			@RequestParam(value = Oauth2Constant.APP_ID, required = true) String appId,
			@RequestParam String username, 
			@RequestParam String password,
			HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

		if(!appService.exists(appId)) {
			request.setAttribute("errorMessage", "非法应用");
			return goLoginPath(redirectUri, appId, request);
		}
		
		Result<SsoUser> result = userService.login(username, password);
		if (!result.isSuccess()) {
			request.setAttribute("errorMessage", result.getMessage());
			return goLoginPath(redirectUri, appId, request);
		}

		String tgt = sessionManager.setUser(result.getData(), request, response);
		return generateCodeAndRedirect(redirectUri, tgt);
	}

	/**
	 * 设置request的redirectUri和appId参数，跳转到登录页
	 * 
	 * @param redirectUri
	 * @param request
	 * @return
	 */
	private String goLoginPath(String redirectUri, String appId, HttpServletRequest request) {
		request.setAttribute(SsoConstant.REDIRECT_URI, redirectUri);
		request.setAttribute(Oauth2Constant.APP_ID, appId);
		return AppConstant.LOGIN_PATH;
	}
	
	/**
	 * 生成授权码，跳转到redirectUri
	 * 
	 * @param redirectUri
	 * @param tgt
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String generateCodeAndRedirect(String redirectUri, String tgt) throws UnsupportedEncodingException {
		// 生成授权码
		String code = codeManager.generate(tgt, true, redirectUri);
		return "redirect:" + authRedirectUri(redirectUri, code);
	}

	/**
	 * 将授权码拼接到回调redirectUri中
	 * 
	 * @param redirectUri
	 * @param code
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	private String authRedirectUri(String redirectUri, String code) throws UnsupportedEncodingException {
		StringBuilder sbf = new StringBuilder(redirectUri);
		if (redirectUri.indexOf("?") > -1) {
			sbf.append("&");
		}
		else {
			sbf.append("?");
		}
		sbf.append(Oauth2Constant.AUTH_CODE).append("=").append(code);
		return URLDecoder.decode(sbf.toString(), "utf-8");
	}

}
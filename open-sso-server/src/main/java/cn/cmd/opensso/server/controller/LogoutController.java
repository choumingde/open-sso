package cn.cmd.opensso.server.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.cmd.opensso.server.session.SessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import cn.cmd.opensso.client.constant.SsoConstant;

/**
 * 单点登出
 * 
 */
@Controller
@RequestMapping("/logout")
public class LogoutController {

	@Autowired
	private SessionManager sessionManager;

	/**
	 * 登出
	 * 
	 * @param redirectUri
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String logout(
			@RequestParam(value = SsoConstant.REDIRECT_URI, required = true) String redirectUri,
	        HttpServletRequest request, HttpServletResponse response) {
		sessionManager.invalidate(request, response);
        return "redirect:" + redirectUri;
	}
}
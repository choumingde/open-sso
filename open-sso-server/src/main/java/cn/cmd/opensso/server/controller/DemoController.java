package cn.cmd.opensso.server.controller;

import cn.cmd.opensso.client.rpc.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 *
 */
@SuppressWarnings("rawtypes")
@RestController
@RequestMapping("/demo")
public class DemoController {
	/**
	 * 初始页
	 * @param request
	 * @return
	 */
    @RequestMapping
	public Result demo(HttpServletRequest request) {
		return Result.createSuccess(true);
	}

}

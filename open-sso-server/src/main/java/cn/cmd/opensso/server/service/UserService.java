package cn.cmd.opensso.server.service;

import cn.cmd.opensso.client.rpc.Result;
import cn.cmd.opensso.client.rpc.SsoUser;

/**
 * 用户服务接口
 * 
 */
public interface UserService {
	
	/**
	 * 登录
	 * 
	 * @param username
	 *            登录名
	 * @param password
	 *            密码
	 * @return
	 */
	public Result<SsoUser> login(String username, String password);
}

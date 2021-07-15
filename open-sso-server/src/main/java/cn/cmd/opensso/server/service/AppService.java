package cn.cmd.opensso.server.service;

import cn.cmd.opensso.client.rpc.Result;

/**
 * 应用服务接口
 * 
 */
public interface AppService {

	boolean exists(String appId);
	
	Result<Void> validate(String appId, String appSecret);
}

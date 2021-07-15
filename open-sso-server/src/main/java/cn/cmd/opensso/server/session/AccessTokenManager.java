package cn.cmd.opensso.server.session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import cn.cmd.opensso.server.common.AccessTokenContent;
import cn.cmd.opensso.server.common.Expiration;
import cn.cmd.opensso.client.constant.SsoConstant;
import cn.cmd.opensso.client.util.HttpUtils;

/**
 * 调用凭证AccessToken管理抽象
 * 
 */
public interface AccessTokenManager extends Expiration {

	/**
	 * 生成AccessToken
	 * 
	 * @param accessTokenContent
	 * @return
	 */
	default String generate(AccessTokenContent accessTokenContent) {
		String accessToken = "AT-" + UUID.randomUUID().toString().replaceAll("-", "");
		create(accessToken, accessTokenContent);
		return accessToken;
	}

	/**
	 * 生成AccessToken
	 * 
	 * @param accessToken
	 * @param accessTokenContent
	 */
	void create(String accessToken, AccessTokenContent accessTokenContent);
	
	/**
     * 延长AccessToken生命周期
     * 
	 * @param accessToken
	 * @return
	 */
	boolean refresh(String accessToken);
	
	/**
     * 查询
     * 
	 * @param accessToken
	 * @return
	 */
	AccessTokenContent get(String accessToken);
	
	/**
	 * 根据TGT删除AccessToken
	 * 
	 * @param tgt
	 */
	void remove(String tgt);
	
	/**
	 * 发起客户端登出请求
	 * 
	 * @param redirectUri
	 * @param accessToken
	 */
	default void sendLogoutRequest(String redirectUri, String accessToken) {
		Map<String, String> headerMap = new HashMap<>();
		headerMap.put(SsoConstant.LOGOUT_PARAMETER_NAME, accessToken);
		HttpUtils.postHeader(redirectUri, headerMap);
	}
}

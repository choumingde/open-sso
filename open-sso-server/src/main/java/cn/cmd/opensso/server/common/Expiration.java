package cn.cmd.opensso.server.common;

/**
 * 含时效
 */
public interface Expiration {
	
	/**
	 * 时效（秒）
	 * @return
	 */
	int getExpiresIn();
}

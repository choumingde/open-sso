package cn.cmd.opensso.server.common;

import java.io.Serializable;

public class RefreshTokenContent implements Serializable {

	private static final long serialVersionUID = -1332598459045608781L;

	private AccessTokenContent accessTokenContent;

	private String accessToken;

	public RefreshTokenContent(AccessTokenContent accessTokenContent, String accessToken) {
		this.accessTokenContent = accessTokenContent;
		this.accessToken = accessToken;
	}

	public AccessTokenContent getAccessTokenContent() {
		return accessTokenContent;
	}

	public void setAccessTokenContent(AccessTokenContent accessTokenContent) {
		this.accessTokenContent = accessTokenContent;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}
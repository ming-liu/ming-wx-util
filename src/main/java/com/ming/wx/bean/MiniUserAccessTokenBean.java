package com.ming.wx.bean;

public class MiniUserAccessTokenBean {

	/**
	 * 
	 */
	private String errcode;
	private String errmsg;
	/**
	 * 会话密钥
	 */
	private String session_key;
	/**
	 * 用户唯一标识
	 */
	private String openid;
	/**
	 * 用户在开放平台的唯一标识符
	 */
	private String unionid;

	public String getErrcode() {
		return errcode;
	}

	public void setErrcode(String errcode) {
		this.errcode = errcode;
	}

	public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}

	public String getSession_key() {
		return session_key;
	}

	public void setSession_key(String session_key) {
		this.session_key = session_key;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

}

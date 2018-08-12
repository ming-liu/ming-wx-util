package com.ming.wx.service;

import java.net.URI;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import com.ming.util.JsonUtil;
import com.ming.wx.bean.AccessTokenBean;
import com.ming.wx.bean.TicketBean;
import com.ming.wx.bean.UserAccessTokenBean;
import com.ming.wx.bean.UserInfoBean;

public class WechatRemoteService {

	private static Logger logger = Logger.getLogger(WechatRemoteService.class);

	private static final String TokenUrl = "https://api.weixin.qq.com/cgi-bin/token";
	private static final String OauthTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
	private static final String SnsUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo";
	private static final String TicketUrl = "https://api.weixin.qq.com/cgi-bin/ticket/getticket";

	public static final String TicketType_JSAPI = "jsapi";

	private HttpClientProvider httpClientProvider = HttpClientProvider.create();

	public AccessTokenBean refreshAccessToken(String appId, String appSecret) {
		try {
			URI uri = new URIBuilder(TokenUrl).setParameter("grant_type", "client_credential").setParameter("appid", appId).setParameter("secret", appSecret)
					.build();
			HttpGet get = new HttpGet(uri);
			HttpResponse response = httpClientProvider.get().execute(get);
			HttpEntity entity = response.getEntity();
			String string = EntityUtils.toString(entity);
			logger.info("refreshAccessTokenResponse=" + string);
			AccessTokenBean token = JsonUtil.fromJson(string, AccessTokenBean.class);
			return token;
		} catch (Exception e) {
			logger.error(e, e);
		}
		return null;
	}

	public TicketBean refreshTicket(String accessToken, String type) {
		try {
			URI uri = new URIBuilder(TicketUrl).setParameter("access_token", accessToken).setParameter("type", type).build();
			HttpGet get = new HttpGet(uri);
			HttpResponse response = httpClientProvider.get().execute(get);
			HttpEntity entity = response.getEntity();
			String string = EntityUtils.toString(entity);
			logger.info("refreshTicket=" + string);
			TicketBean token = JsonUtil.fromJson(string, TicketBean.class);
			return token;
		} catch (Exception e) {
			logger.error(e, e);
		}
		return null;
	}

	public UserAccessTokenBean getOauthAccessToken(String appId, String appSecret, String code) {
		try {
			URI uri = new URIBuilder(OauthTokenUrl).setParameter("appid", appId).setParameter("secret", appSecret).setParameter("code", code)
					.setParameter("grant_type", "authorization_code").build();
			HttpGet get = new HttpGet(uri);
			HttpResponse response = httpClientProvider.get().execute(get);
			HttpEntity entity = response.getEntity();
			String string = EntityUtils.toString(entity);
			logger.info("getOauthAccessTokenResponse=" + string);
			UserAccessTokenBean token = JsonUtil.fromJson(string, UserAccessTokenBean.class);
			return token;
		} catch (Exception e) {
			logger.error(e, e);
		}
		return null;
	}

	/**
	 * @param access_token
	 * @param openid
	 * @param lang
	 *            返回国家地区语言版本，zh_CN 简体，zh_TW 繁体，en 英语,null时为zh_CN
	 * @return
	 */
	public UserInfoBean getSnsUserInfo(String access_token, String openid, String lang) {
		try {
			URI uri = new URIBuilder(SnsUserInfoUrl).setParameter("access_token", access_token).setParameter("openid", openid)
					.setParameter("lang", lang == null ? "zh_CN" : lang).build();
			HttpGet get = new HttpGet(uri);
			HttpResponse response = httpClientProvider.get().execute(get);
			HttpEntity entity = response.getEntity();
			String string = EntityUtils.toString(entity, "UTF-8");
			logger.info("getSnsUserInfoResponse=" + string);
			UserInfoBean userInfo = JsonUtil.fromJson(string, UserInfoBean.class);
			return userInfo;
		} catch (Exception e) {
			logger.error(e, e);
		}
		return null;
	}

}

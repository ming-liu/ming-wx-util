package com.ming.wx.service;

import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;

import org.apache.http.Consts;
import org.apache.http.client.HttpClient;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.log4j.Logger;

public class HttpClientProvider {

	private static final Logger logger = Logger.getLogger(HttpClientProvider.class);

	private HttpClient httpClient = null;

	private int maxTotal = 500;
	private int maxPerRoute = 500;
	private boolean initialed = false;

	private HttpClientProvider() {
	}

	private static Registry<ConnectionSocketFactory> getDefaultRegistry() {
		SSLContext sslContext = null;
		try {
			sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
				@Override
				public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
					return true;
				}
			}).build();
		} catch (Exception e) {
			logger.error("ssl context exception", e);
		}

		SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
		return RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslsf)
				.build();
	}

	protected HttpClientProvider init() {
		if (!initialed) {
			SocketConfig socketConfig = SocketConfig.custom().setTcpNoDelay(true).setSoKeepAlive(true).setSoReuseAddress(true).setSoTimeout(getTimeout())
					.build();
			ConnectionConfig connectionConfig = ConnectionConfig.custom().setCharset(Consts.UTF_8).build();
			PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager(getDefaultRegistry());

			connManager.setMaxTotal(maxTotal);
			connManager.setDefaultMaxPerRoute(maxPerRoute);
			connManager.setDefaultSocketConfig(socketConfig);
			connManager.setDefaultConnectionConfig(connectionConfig);

			try {
				httpClient = HttpClients.custom().setConnectionManager(connManager).build();
			} catch (Exception e) {
				logger.error("SSLContext init failed", e);
			}
			logger.info("init over");
			initialed = true;
		}
		return this;
	}

	protected int getTimeout() {
		return 2 * 1000;
	}

	public static HttpClientProvider create() {
		return new HttpClientProvider().init();
	}

	public HttpClient get() {
		return httpClient;
	}
}

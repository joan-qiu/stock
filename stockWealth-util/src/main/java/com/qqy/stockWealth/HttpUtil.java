package com.qqy.stockWealth;

import javax.net.ssl.*;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;


/**
 * <p>
 * Title: HttpUtil
 * </p>
 * <p>
 * Description:
 * </p>
 * 
 * @author joan.qiu
 * @date 2019年7月19日
 */
public class HttpUtil {


	private static SSLSocketFactory ssf;

	private static int httpConnTimeout = 5000;
	private static int httpReadTimeout = 5000;

	private static String GET_STRING = "GET";
	private static String HTTPS_STRING = "HTTPS";

	public static int getHttpConnTimeout() {
		return httpConnTimeout / 1000;
	}

	public static void setHttpConnTimeout(int httpConnTimeout) {
		HttpUtil.httpConnTimeout = httpConnTimeout * 1000;
	}

	public static int getHttpReadTimeout() {
		return httpReadTimeout / 1000;
	}

	public static void setHttpReadTimeout(int httpReadTimeout) {
		HttpUtil.httpReadTimeout = httpReadTimeout * 1000;
	}

	private static class TrustAnyTrustManager implements X509TrustManager {
		@Override
		public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		@Override
		public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
		}

		@Override
		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}
	}

	static {
		SSLContext sslContext = null;
		try {
			sslContext = SSLContext.getInstance("SSL", "SunJSSE");
		} catch (NoSuchAlgorithmException e) {
 		} catch (NoSuchProviderException e) {
		}
		try {
			sslContext.init(null, new TrustManager[] { new TrustAnyTrustManager() }, new java.security.SecureRandom());
		} catch (KeyManagementException e) {
		}

		ssf = sslContext.getSocketFactory();
	}

	public static String httpsRequest(String requestUrl, String requestMethod, String outputStr, String charset)
			throws IOException {
		StringBuffer buffer = new StringBuffer();
		HttpsURLConnection httpUrlConn = null;
		OutputStream outputStream = null;
		InputStream inputStream = null;

		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;
		try {
			URL url = new URL(requestUrl);
			httpUrlConn = (HttpsURLConnection) url.openConnection();
			httpUrlConn.setSSLSocketFactory(ssf);
			httpUrlConn.setConnectTimeout(httpConnTimeout);
			httpUrlConn.setReadTimeout(httpReadTimeout);
			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			httpUrlConn.setRequestProperty("Content-Type", "text/json");

			httpUrlConn.setRequestMethod(requestMethod);

			if (GET_STRING.equalsIgnoreCase(requestMethod)) {
				httpUrlConn.connect();
			}

			if (null != outputStr) {
				outputStream = httpUrlConn.getOutputStream();
				outputStream.write(outputStr.getBytes(charset));
				outputStream.close();
			}

			inputStream = httpUrlConn.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream, charset);
			bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;
			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			bufferedReader.close();
			inputStreamReader.close();

			inputStream.close();
			inputStream = null;

			httpUrlConn.disconnect();

		} catch (ConnectException ce) {
		} catch (Exception e) {
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (inputStreamReader != null) {
				inputStreamReader.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
			if (httpUrlConn != null) {
				httpUrlConn.disconnect();
			}
		}
		return buffer.toString();
	}

	public static String httpRequest(String requestUrl, String requestMethod, String outputStr, String charset)
			throws IOException {
		StringBuffer buffer = new StringBuffer();

		HttpURLConnection httpUrlConn = null;
		OutputStream outputStream = null;
		InputStream inputStream = null;
		InputStreamReader inputStreamReader = null;
		BufferedReader bufferedReader = null;

		try {
			URL url = new URL(requestUrl);
			httpUrlConn = (HttpURLConnection) url.openConnection();
			httpUrlConn.setConnectTimeout(httpConnTimeout);
			httpUrlConn.setReadTimeout(httpReadTimeout);

			httpUrlConn.setDoOutput(true);
			httpUrlConn.setDoInput(true);
			httpUrlConn.setUseCaches(false);
			httpUrlConn.setRequestProperty("Content-Type", "text/json");
			httpUrlConn.setRequestMethod(requestMethod);

			if (GET_STRING.equalsIgnoreCase(requestMethod)) {
				httpUrlConn.connect();
			}

			if (null != outputStr) {

				outputStream = httpUrlConn.getOutputStream();

				outputStream.write(outputStr.getBytes(charset));
				outputStream.close();
			}

			inputStream = httpUrlConn.getInputStream();
			inputStreamReader = new InputStreamReader(inputStream, charset);
			bufferedReader = new BufferedReader(inputStreamReader);

			String str = null;

			while ((str = bufferedReader.readLine()) != null) {
				buffer.append(str);
			}

			bufferedReader.close();
			inputStreamReader.close();

			inputStream.close();
			inputStream = null;

			httpUrlConn.disconnect();

		} catch (ConnectException ce) {
		} catch (Exception e) {
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
			if (bufferedReader != null) {
				bufferedReader.close();
			}
			if (inputStreamReader != null) {
				inputStreamReader.close();
			}
			if (inputStream != null) {
				inputStream.close();
			}
			if (httpUrlConn != null) {
				httpUrlConn.disconnect();
			}
		}

		return buffer.toString();
	}

	public static String postRequest(String url, String jsonStr) {
		String result = null;
		try {
			if (url.toUpperCase().startsWith(HTTPS_STRING)) {
				result = HttpUtil.httpsRequest(url, "POST", jsonStr, "UTF-8");
			} else {
				result = HttpUtil.httpRequest(url, "POST", jsonStr, "UTF-8");
			}
		} catch (Exception e) {
		}
		return result;
	}

}
package cn.mutils.app.core.net;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.cookie.Cookie;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultRedirectHandler;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import cn.mutils.app.core.IClearable;
import cn.mutils.app.core.annotation.net.Head;
import cn.mutils.app.core.beans.BeanField;
import cn.mutils.app.core.event.Listener;
import cn.mutils.app.core.json.JsonUtil;
import cn.mutils.app.core.reflect.ReflectUtil;
import cn.mutils.app.core.text.StringUtil;
import cn.mutils.app.core.time.MillisFormat;

/**
 * Template of request and response for net API at Java application level
 */
@SuppressWarnings({ "deprecation", "serial", "unchecked" })
public class NetClient<REQUEST, RESPONSE> {

	/**
	 * Listener for {@link NetClient}
	 */
	public static abstract class NetClientListener<REQUEST, RESPONSE> implements Listener {

		/**
		 * Get request entity class
		 * 
		 * @return
		 */
		public abstract Class<?> requestRawType();

		/**
		 * Get generic type info of request
		 * 
		 * @return
		 */
		public Type requestGenericType() {
			return null;
		}

		/**
		 * Get response entity class
		 * 
		 * @return
		 */
		public abstract Class<?> responseRawType();

		/**
		 * Get generic type info of response
		 * 
		 * @return
		 */
		public Type responseGenericType() {
			return null;
		}

		/**
		 * Convert some properties who are come form UI to REQUEST
		 * 
		 * @return
		 */
		public REQUEST convertToRequest() {
			return null;
		}

		/**
		 * Convert RESPONSE to object who are defined by UI
		 * 
		 * @param response
		 * @return
		 */
		public Object convertFromResponse(RESPONSE response) {
			return null;
		}

		/**
		 * Sign post JSON request content
		 * 
		 * @param request
		 * @return
		 * @throws Exception
		 */
		public Object signPostJson(REQUEST request) throws Exception {
			return request;
		}

		/**
		 * Give cookie to request
		 * 
		 * @return cookie
		 */
		public abstract String requestCookie(URL url);

		/**
		 * Receive cookie from response
		 */
		public abstract void responseCookie(URL url, String cookie);

		/**
		 * Verify error code of RESPONSE
		 * 
		 * @param response
		 * @throws Exception
		 */
		public void errorCodeVerify(RESPONSE response) throws Exception {
			debugging(EVENT_ERROR_CODE, "errorCodeVerify");
		}

		/**
		 * Debugging
		 * 
		 * @param event
		 * @param message
		 */
		public abstract void debugging(String event, String message);
	}

	public static final String REQUEST_METHOD_GET = "GET";
	public static final String REQUEST_METHOD_POST = "POST";

	public static final String EVENT_URL = "event.url";
	public static final String EVENT_REQUEST_METHOD = "event.request.method";
	public static final String EVENT_PARAMS = "event.params";
	public static final String EVENT_ERROR_CODE = "event.error.code";
	public static final String EVENT_EXCEPTION = "event.exception";
	public static final String EVENT_RESPONSE = "event.response";
	public static final String EVENT_REQUEST_COOKIE = "event.request.cookie";
	public static final String EVENT_RESPONSE_COOKIE = "event.response.cookie";
	public static final String EVENT_ELASE_TIME = "event.elapse.time";
	public static final String EVENT_REQUEST_HEADERS = "event.request.headers";

	protected REQUEST mRequest;

	protected RESPONSE mResponse;

	protected boolean mRequestConvertible;

	protected boolean mResponseConvertible;

	/**
	 * Response object converted by {@link #convertFromResponse(Object)}
	 * 
	 * It will be null if {@link #mResponseConverted} is null.
	 */
	protected Object mResponseConverted;

	protected String mUrl;

	protected String mRequestMethod = REQUEST_METHOD_GET;

	/** Value of HTTP head Refer */
	protected String mReferer;

	/** Whether to request by split array parameters: ids=1,2,3&name=wavinsun */
	protected boolean mSplitArrayParams = true;

	/**
	 * Whether to request by restful URL: http://www.xxx.cn/detail/{id}/{name}/
	 */
	protected boolean mRestUrl;

	/** Whether to request by post parameter:id=1&name=wavinsun */
	protected boolean mPostParams;

	/** Whether to request by post JSON: {"id":1,name:"wavinsun"} */
	protected boolean mPostJson;

	/**
	 * Whether to request by signed JSON:
	 * {"key":"key","sign_type":"MD5","sign":"sign_result","data":"data"}
	 */
	protected boolean mPostJsonSigned;

	/** Whether to use cookie cached to make request */
	protected boolean mCookieWithRequest;

	/** Whether to cache cookie responded */
	protected boolean mCookieWithResponse;

	/** Cookie identity */
	protected String mCookieCacheId = "JSESSIONID";

	/**
	 * Regular expression for HTTP 500
	 * 
	 * Apache Tomcat 7.0.47 HTTP Status 500
	 */
	protected String mHttp500HtmlRegex = "(.*?)<pre>(.*?)</pre>(.*?)";

	/**
	 * Server stack trace index of regular expression for HTTP 500
	 * 
	 * 2 is server stack trace index of Apache Tomcat 7.0.47 HTTP Status 500
	 */
	protected int mHttp500HtmlRegexGroup = 2;

	protected long mResponseTime;

	protected NetClientListener<REQUEST, RESPONSE> mListener;

	public NetClientListener<REQUEST, RESPONSE> getListener() {
		return mListener;
	}

	public void setListener(NetClientListener<REQUEST, RESPONSE> listener) {
		mListener = listener;
	}

	public long getResponseTime() {
		return mResponseTime;
	}

	public void setResponseTime(long responseTime) {
		mResponseTime = responseTime;
	}

	public boolean isSplitArrayParams() {
		return mSplitArrayParams;
	}

	public void setSplitArrayParams(boolean splitArrayParams) {
		mSplitArrayParams = splitArrayParams;
	}

	public boolean isRestUrl() {
		return mRestUrl;
	}

	public void setRestUrl(boolean restUrl) {
		mRestUrl = restUrl;
	}

	public boolean isPostParams() {
		return mPostParams;
	}

	public void setPostParams(boolean postParams) {
		mPostParams = postParams;
	}

	public boolean isPostJson() {
		return mPostJson;
	}

	public void setPostJson(boolean postJson) {
		mPostJson = postJson;
	}

	public void setPostJsonSigned(boolean postJsonSigned) {
		mPostJsonSigned = postJsonSigned;
	}

	public void setCookieWithRequest(boolean cookieWithRequest) {
		mCookieWithRequest = cookieWithRequest;
	}

	public void setCookieWithResponse(boolean cookieWithResponse) {
		mCookieWithResponse = cookieWithResponse;
	}

	public void setCookieCachedId(String cookieCachedId) {
		mCookieCacheId = cookieCachedId;
	}

	public boolean isRequestConvertible() {
		return mRequestConvertible;
	}

	public void setRequestConvertible(boolean requestConvertible) {
		mRequestConvertible = requestConvertible;
	}

	public boolean isResponseConvertible() {
		return mResponseConvertible;
	}

	public void setResponseConvertible(boolean responseConvertible) {
		mResponseConvertible = responseConvertible;
	}

	public Object getResponseConverted() {
		return mResponseConverted;
	}

	public void setResponseConverted(Object responseConverted) {
		mResponseConverted = responseConverted;
	}

	public String getUrl() {
		return this.mUrl;
	}

	public void setUrl(String url) {
		this.mUrl = url;
	}

	public String getRequestMethod() {
		return this.mRequestMethod;
	}

	public void setRequestMethod(String requestMethod) {
		this.mRequestMethod = requestMethod;
	}

	public REQUEST getRequest() {
		return this.mRequest;
	}

	public void setRequest(REQUEST request) {
		this.mRequest = request;
	}

	public RESPONSE getResponse() {
		return this.mResponse;
	}

	public void setResponse(RESPONSE response) {
		this.mResponse = response;
	}

	/**
	 * Execute client
	 * 
	 * @return RESPONSE or exception
	 */
	public Object execute() {
		Type resJsonGenericType = null;
		long time = System.currentTimeMillis();
		DefaultHttpClient client = new DefaultHttpClient();
		client.setRedirectHandler(new NetRedirectHandler());
		HttpParams clientParams = client.getParams();
		clientParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
		clientParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
		try {
			if (mRequestConvertible && mListener != null) {
				mRequest = mListener.convertToRequest();
			}
			String params = null, spec = mRestUrl ? convertToRestUrl(mRequest, mUrl) : mUrl;
			URL url = new URL(spec);
			HttpUriRequest httpRequest = null;
			if (REQUEST_METHOD_POST.equals(mRequestMethod)) {
				httpRequest = new HttpPost(url.toURI());
				params = mPostParams ? convertToParameters(mRequest, mSplitArrayParams) : null;
				if (mPostJson) {
					if (mPostJsonSigned && mListener != null) {
						params = JsonUtil.convert(mListener.signPostJson(mRequest));
					} else {
						params = JsonUtil.convert(mRequest);
					}
				}
				HttpEntity entity = convertToEntity(params != null ? params : mRequest);
				if (mPostJson && entity != null) {
					((StringEntity) entity).setContentType("application/json");
				}
				((HttpPost) httpRequest).setEntity(entity);
			} else {
				params = convertToParameters(mRequest, mSplitArrayParams);
				httpRequest = new HttpGet(spec + (spec.contains("?") ? "&" : "?") + params);
			}
			String headers = convertToHeaders(mRequest, httpRequest);
			if (mListener != null) {
				mListener.debugging(EVENT_URL, spec);
				mListener.debugging(EVENT_REQUEST_METHOD, mRequestMethod);
				if (params != null && !params.isEmpty()) {
					mListener.debugging(EVENT_PARAMS, params);
				}
				if (headers != null && !headers.isEmpty()) {
					mListener.debugging(EVENT_REQUEST_HEADERS, headers);
				}
			}
			if (mCookieWithRequest) {
				String cookie = (mCookieWithRequest && mListener != null) ? mListener.requestCookie(url)
						: (mCookieCacheId + "=Cookie");
				httpRequest.setHeader("Cookie", cookie);
				if (mListener != null) {
					mListener.debugging(EVENT_REQUEST_COOKIE, cookie);
				}
			}
			if (mReferer != null) {
				httpRequest.setHeader("Refer", mReferer);
			}
			httpRequest.setHeader("Cache-Control", "no-cache");
			HttpParams requestParams = httpRequest.getParams();
			requestParams.setParameter(HTTP.CONTENT_ENCODING, "UTF-8");
			requestParams.setParameter(HTTP.CHARSET_PARAM, "UTF-8");
			requestParams.setParameter(HTTP.DEFAULT_PROTOCOL_CHARSET, "UTF-8");
			HttpResponse httpResponse = client.execute(httpRequest);
			int statusCode = httpResponse.getStatusLine().getStatusCode();
			if (statusCode != 200 && statusCode != 500) {
				throw new HttpStatusException(statusCode);
			}
			String response = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
			mResponseTime = System.currentTimeMillis();
			if (statusCode == 500) {
				throw new HttpStatusException(statusCode,
						getStackTrace(response, mHttp500HtmlRegex, mHttp500HtmlRegexGroup));
			}
			if (mCookieWithResponse) {
				List<Cookie> cookies = client.getCookieStore().getCookies();
				if (cookies != null) {
					for (Cookie c : cookies) {
						String cookieName = c.getName();
						if (cookieName.contains(mCookieCacheId)) {
							String cookie = cookieName + "=" + c.getValue();
							if (mListener != null) {
								mListener.responseCookie(url, cookie);
								mListener.debugging(EVENT_RESPONSE_COOKIE, cookie);
							}
							break;
						}
					}
				}
			}
			if (mListener != null) {
				mListener.debugging(EVENT_RESPONSE, response);
				mListener.debugging(EVENT_ELASE_TIME, MillisFormat.formatAll(mResponseTime - time));
			}
			Class<RESPONSE> resJsonClass = (Class<RESPONSE>) (mListener != null ? mListener.responseRawType()
					: ReflectUtil.getParameterizedRawType(getClass(), 1));
			resJsonGenericType = mListener != null ? mListener.responseGenericType() : null;
			RESPONSE resJson = JsonUtil.convert(response, resJsonClass, resJsonGenericType);
			if (mListener != null) {
				mListener.errorCodeVerify(resJson);
			}
			mResponse = resJson;
			if (mResponseConvertible && mListener != null) {
				mResponseConverted = mListener.convertFromResponse(mResponse);
			}
			return resJson;
		} catch (Exception e) {
			if (mListener != null) {
				mListener.debugging(EVENT_EXCEPTION, StringUtil.printStackTrace(e));
			}
			return e;
		} finally {
			client.getConnectionManager().shutdown();
			if (resJsonGenericType != null && (resJsonGenericType instanceof IClearable)) {
				((IClearable) resJsonGenericType).clear();
			}
		}
	}

	/**
	 * Convert object whose properties have {@link Head} to headers
	 * 
	 * @param object
	 * @return log message
	 */
	public static String convertToHeaders(Object object, HttpUriRequest httpRequest) {
		StringBuilder sb = new StringBuilder();
		if (object == null) {
			return sb.toString();
		}
		if (object instanceof Map || object instanceof List) {
			return sb.toString();
		} else {
			for (BeanField field : BeanField.getFields(object.getClass())) {
				try {
					Object value = field.get(object);
					if (value == null) {
						continue;
					}
					if (field.getAnnotation(Head.class) == null) {
						continue;
					}
					String name = field.getName();
					String hValue = JsonUtil.convert(value);
					httpRequest.setHeader(name, hValue);
					if (sb.length() != 0) {
						sb.append("\n");
					}
					sb.append(name);
					sb.append(":");
					sb.append(hValue);
				} catch (Exception e) {

				}
			}
		}
		return sb.toString();
	}

	/**
	 * Convert restful URL expression to exactly URL
	 * 
	 * @param object
	 * @param url
	 * @return
	 */
	public static String convertToRestUrl(Object object, String url) {
		if (url == null) {
			return url;
		}
		if (object == null) {
			return url;
		}
		if (object instanceof Map) {
			for (Entry<?, ?> entry : ((Map<?, ?>) object).entrySet()) {
				Object key = entry.getKey();
				Object value = entry.getValue();
				if (key == null || value == null) {
					continue;
				}
				try {
					String restKey = "{" + key.toString() + "}";
					if (!url.contains(restKey)) {
						continue;
					}
					String restValue = URLEncoder.encode(JsonUtil.convert(value), "UTF-8");
					url = url.replace(restKey, restValue);
				} catch (Exception e) {

				}
			}
		} else {
			for (BeanField field : BeanField.getFields(object.getClass())) {
				try {
					Object value = field.get(object);
					if (value == null) {
						continue;
					}
					String restKey = "{" + field.getName() + "}";
					if (!url.contains(restKey)) {
						continue;
					}
					String restValue = URLEncoder.encode(JsonUtil.convert(value), "UTF-8");
					url = url.replace(restKey, restValue);
				} catch (Exception e) {

				}
			}
		}
		return url;
	}

	/**
	 * Convert object to URL parameters like "key=value"
	 * 
	 * @param object
	 * @param splitArrayParams
	 * @return
	 */
	public static String convertToParameters(Object object, boolean splitArrayParams) {
		if (object == null) {
			return "";
		}
		if (object instanceof String) {
			return (String) object;
		}
		boolean firstParam = true;
		StringBuilder sb = new StringBuilder();
		if (object instanceof Map) {
			for (Entry<?, ?> entry : ((Map<?, ?>) object).entrySet()) {
				Object key = entry.getKey();
				Object value = entry.getValue();
				if (key == null || value == null) {
					continue;
				}
				if (firstParam) {
					firstParam = false;
				} else {
					sb.append("&");
				}
				sb.append(key.toString());
				sb.append("=");
				try {
					if ((value instanceof List) && splitArrayParams) {
						sb.append(convertToSplitArrayParam((List<?>) value));
					} else {
						sb.append(URLEncoder.encode(JsonUtil.convert(value), "UTF-8"));
					}
				} catch (Exception e) {

				}
			}
		} else {
			for (BeanField field : BeanField.getFields(object.getClass())) {
				try {
					Object value = field.get(object);
					if (value == null) {
						continue;
					}
					if (firstParam) {
						firstParam = false;
					} else {
						sb.append("&");
					}
					sb.append(field.getName());
					sb.append("=");
					if ((value instanceof List) && splitArrayParams) {
						sb.append(convertToSplitArrayParam((List<?>) value));
					} else {
						sb.append(URLEncoder.encode(JsonUtil.convert(value), "UTF-8"));
					}
				} catch (Exception e) {

				}
			}
		}
		return sb.toString();
	}

	/**
	 * Convert array to URL parameter
	 * 
	 * @param value
	 * @return
	 */
	public static String convertToSplitArrayParam(List<?> value) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0, size = value.size(); i < size; i++) {
			try {
				if (i != 0) {
					sb.append(",");
				}
				sb.append(URLEncoder.encode(JsonUtil.convert(value.get(i)), "UTF-8"));
			} catch (Exception e) {

			}
		}
		return sb.toString();
	}

	/**
	 * Convert object to HttpEntity
	 * 
	 * @param object
	 * @return
	 */
	public static HttpEntity convertToEntity(Object object) {
		if (object == null) {
			return null;
		}
		if (object instanceof String) {
			try {
				return new StringEntity((String) object, "UTF-8");
			} catch (Exception e) {
				return null;
			}
		} else {
			MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
			if (object instanceof Map) {
				for (Entry<?, ?> entry : ((Map<?, ?>) object).entrySet()) {
					Object key = entry.getKey();
					Object value = entry.getValue();
					if (key == null || value == null) {
						continue;
					}
					if (value instanceof File) {
						entity.addPart(key.toString(), new FileBody((File) value));
					} else {
						try {
							entity.addPart(key.toString(),
									new StringBody(JsonUtil.convert(value), Charset.forName("UTF-8")));
						} catch (Exception e) {

						}
					}
				}
			} else {
				for (BeanField field : BeanField.getFields(object.getClass())) {
					try {
						Object value = field.get(object);
						if (value == null) {
							continue;
						}
						if (value instanceof File) {
							entity.addPart(field.getName(), new FileBody((File) value));
						} else {
							entity.addPart(field.getName(),
									new StringBody(JsonUtil.convert(value), Charset.forName("UTF-8")));
						}
					} catch (Exception e) {

					}
				}
			}
			return entity;
		}
	}

	/**
	 * Parse response of HTTP 500 to get server stack trace
	 * 
	 * @param html500
	 * @param regex
	 * @param regexGroup
	 * @return
	 */
	public static String getStackTrace(String html500, String regex, int regexGroup) {
		try {
			if (html500 == null) {
				return null;
			}
			Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE | Pattern.DOTALL);
			Matcher m = p.matcher(html500);
			if (m.find()) {
				String stackTrace = m.group(regexGroup);
				if (!stackTrace.isEmpty()) {
					return stackTrace;
				}
			}
			return StringUtil.htmlText(html500);
		} catch (Exception e) {
			return StringUtil.htmlText(html500);
		}
	}

	public static StringBuilder getLog(String event, String message) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(event);
		sb.append("]");
		message = message != null ? message : "null";
		if (message.contains("\n")) {
			sb.append("\n");
		} else {
			sb.append(" ");
		}
		sb.append(message);
		return sb;
	}

	public static class NetRedirectHandler extends DefaultRedirectHandler {

		@Override
		public boolean isRedirectRequested(HttpResponse response, HttpContext context) {
			boolean isRedirect = super.isRedirectRequested(response, context);
			if (!isRedirect) {
				int responseCode = response.getStatusLine().getStatusCode();
				if (responseCode == 301 || responseCode == 302) {
					return true;
				}
			}
			return isRedirect;
		}

	}

	public static class ConnectNotFoundException extends Exception {

	}

	public static class CookieExpiredException extends Exception {

	}

	public static class CodeException extends Exception {

		protected int mCode;

		public CodeException() {

		}

		public CodeException(int code) {
			mCode = code;
		}

		public CodeException(int code, String message) {
			super(message);
			mCode = code;
		}

		public int getCode() {
			return mCode;
		}

		public void setCode(int code) {
			mCode = code;
		}

		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append(getClass().getName());
			sb.append(" ");
			sb.append(mCode);
			String msg = getMessage();
			if (msg != null) {
				sb.append("\n{\n");
				sb.append(msg);
				sb.append("\n}");
			}
			return sb.toString();
		}
	}

	public static class ErrorCodeException extends CodeException {

		public ErrorCodeException() {

		}

		public ErrorCodeException(int code) {
			super(code);
		}

		public ErrorCodeException(int code, String message) {
			super(code, message);
		}

	}

	public static class HttpStatusException extends CodeException {

		public HttpStatusException() {
			mCode = 200;
		}

		public HttpStatusException(int code) {
			super(code);
		}

		public HttpStatusException(int code, String message) {
			super(code, message);
		}

	}

}

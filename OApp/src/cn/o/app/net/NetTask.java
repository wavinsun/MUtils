package cn.o.app.net;

import java.io.File;
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
import org.apache.http.HttpStatus;
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
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.text.Html;
import cn.o.app.BuildConfig;
import cn.o.app.LogCat;
import cn.o.app.OUtil;
import cn.o.app.json.JsonUtil;
import cn.o.app.queue.QueueItem;
import cn.o.app.runtime.OField;
import cn.o.app.runtime.ReflectUtil;

@SuppressWarnings({ "deprecation", "serial", "unchecked" })
public class NetTask<REQUEST, RESPONSE> extends QueueItem<INetTask<REQUEST, RESPONSE>>
		implements INetTask<REQUEST, RESPONSE> {

	protected static final String EVENT_URL = "event.url";
	protected static final String EVENT_REQUEST_METHOD = "event.request.method";
	protected static final String EVENT_PARAMS = "event.params";
	protected static final String EVENT_ERROR_CODE = "event.error.code";
	protected static final String EVENT_EXCEPTION = "event.exception";
	protected static final String EVENT_RESPONSE = "event.response";
	protected static final String EVENT_REQUEST_COOKIE = "event.request.cookie";
	protected static final String EVENT_RESPONSE_COOKIE = "event.response.cookie";

	public static final String METHOD_GET = HttpGet.METHOD_NAME;

	public static final String METHOD_POST = HttpPost.METHOD_NAME;

	protected REQUEST mRequest;

	protected RESPONSE mResponse;

	protected String mUrl;

	protected String mRequestMethod;

	protected NetAsyncTask mTask;

	protected boolean mRequestConvertible;

	protected boolean mResponseConvertible;

	protected Object mResponseConverted;

	protected String mReferer;

	/** Whether to request by split array parameters: ids=1,2,3&name=lounien */
	protected boolean mSplitArrayParams;

	/**
	 * Whether to request by restfull url: http://www.xxx.cn/detail/{id}/{name}/
	 */
	protected boolean mRestUrl;

	/** Whether to request by post parameter:id=1&name=lounien */
	protected boolean mPostParams;

	/** Whether to request by post JSON: {"id":1,name:"lounien"} */
	protected boolean mPostJson;

	/**
	 * Whether to request by signed JSON:
	 * {"key":"key","sign_type":"MD5","sign":"sign_result","data":"data"}
	 */
	protected boolean mSignPostJson;

	/** Whether to cache cookie responded */
	protected boolean mCookieCacheable;

	/** Whether to use cookie cached to make request */
	protected boolean mCookieCached;

	/** Cookie identity */
	protected String mCookieCacheId;

	/** Regular expression for HTTP 500 */
	protected String mHttp500HtmlRegex;

	/** Server stack trace index of regular expression for HTTP 500 */
	protected int mHttp500HtmlRegexGroup;

	protected long mResponseTime;

	public NetTask() {
		super();
		mRequestMethod = METHOD_GET;
		mSplitArrayParams = true;

		// Apache Tomcat 7.0.47 HTTP Status 500
		mHttp500HtmlRegex = "(.*?)<pre>(.*?)</pre>(.*?)";
		mHttp500HtmlRegexGroup = 2;
	}

	@Override
	public long getResponseTime() {
		return mResponseTime;
	}

	public void addListener(NetTaskListener<REQUEST, RESPONSE> listener) {
		super.addListener(listener);
	}

	public boolean isSplitArrayParams() {
		return mSplitArrayParams;
	}

	public void setSplitArrayParams(boolean splitArrayParams) {
		if (mStarted || mStoped) {
			return;
		}
		mSplitArrayParams = splitArrayParams;
	}

	public boolean isRestUrl() {
		return mRestUrl;
	}

	public void setRestUrl(boolean restUrl) {
		if (mStarted || mStoped) {
			return;
		}
		mRestUrl = restUrl;
	}

	public boolean isPostParams() {
		return mPostParams;
	}

	public void setPostParams(boolean postParams) {
		if (mStarted || mStoped) {
			return;
		}
		mPostParams = postParams;
	}

	public boolean isPostJson() {
		return mPostJson;
	}

	public void setPostJson(boolean postJson) {
		if (mStarted || mStoped) {
			return;
		}
		mPostJson = postJson;
	}

	protected void debuging(String event, String message) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(event);
		sb.append("]");
		if (message.contains("\n")) {
			sb.append("\n");
		} else {
			sb.append(" ");
		}
		sb.append(message);
		if (EVENT_EXCEPTION.equals(event)) {
			LogCat.e(this.getClass().getSimpleName(), sb.toString());
		} else {
			LogCat.i(this.getClass().getSimpleName(), sb.toString());
		}
	}

	public boolean isRequestConvertible() {
		return mRequestConvertible;
	}

	public void setRequestConvertible(boolean requestConvertible) {
		if (mStarted || mStoped) {
			return;
		}
		mRequestConvertible = requestConvertible;
	}

	public boolean isResponseConvertible() {
		return mResponseConvertible;
	}

	public void setResponseConvertible(boolean responseConvertible) {
		if (mStarted || mStoped) {
			return;
		}
		mResponseConvertible = responseConvertible;
	}

	protected REQUEST convertToRequest() {
		return null;
	}

	protected Object signPostJson(REQUEST request) throws Exception {
		return request;
	}

	protected void errorCodeVerify(RESPONSE response) throws Exception {
		if (BuildConfig.DEBUG) {
			debuging(EVENT_ERROR_CODE, "errorCodeVerify");
		}
	}

	protected Object convertFromResponse(RESPONSE response) {
		return null;
	}

	public Object getResponseConverted() {
		return mResponseConverted;
	}

	@Override
	public String getUrl() {
		return this.mUrl;
	}

	@Override
	public void setUrl(String url) {
		if (this.mStarted || this.mStoped) {
			return;
		}
		this.mUrl = url;
	}

	@Override
	public String getRequestMethod() {
		return this.mRequestMethod;
	}

	@Override
	public void setRequestMethod(String requestMethod) {
		if (this.mStarted || this.mStoped) {
			return;
		}
		this.mRequestMethod = requestMethod;
	}

	@Override
	public REQUEST getRequest() {
		return this.mRequest;
	}

	@Override
	public void setRequest(REQUEST request) {
		if (this.mStarted || this.mStoped) {
			return;
		}
		this.mRequest = request;
	}

	@Override
	public RESPONSE getResponse() {
		return this.mResponse;
	}

	@Override
	public void setResponse(RESPONSE response) {
		if (this.mStoped) {
			return;
		}
		this.mResponse = response;
	}

	@Override
	public boolean start() {
		boolean result = super.start();
		if (result) {
			this.mTask = new NetAsyncTask();
			this.mTask.execute();
		}
		return result;
	}

	@Override
	public boolean stop() {
		boolean result = super.stop();
		if (result) {
			if (this.mResponse == null) {
				if (this.mTask != null) {
					this.mTask.cancel(true);
				}
			}
		}
		mContext = null;
		mRequest = null;
		mResponse = null;
		mResponseConverted = null;
		mTask = null;
		return result;
	}

	class NetAsyncTask extends AsyncTask<String, Integer, Object> {

		protected Object doInBackground(String... paramters) {
			try {
				ConnectivityManager connMgr = (ConnectivityManager) mContext
						.getSystemService(Context.CONNECTIVITY_SERVICE);
				NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
				if (networkInfo == null || !networkInfo.isConnected()) {
					return new ConnectNotFoundException();
				}
				if (mRequestConvertible) {
					mRequest = convertToRequest();
				}
				String params = null, spec = mRestUrl ? convertToRestUrl(mRequest, mUrl) : mUrl;
				URL url = new URL(spec);
				HttpUriRequest httpRequest = null;
				if (METHOD_POST.equals(mRequestMethod)) {
					httpRequest = new HttpPost(url.toURI());
					params = mPostParams ? convertToParameters(mRequest, mSplitArrayParams) : null;
					params = mPostJson ? JsonUtil.convert(mSignPostJson ? (signPostJson(mRequest)) : mRequest) : null;
					HttpEntity entity = convertToEntity(params != null ? params : mRequest);
					if (mPostJson && entity != null) {
						((StringEntity) entity).setContentType("application/json");
					}
					((HttpPost) httpRequest).setEntity(entity);
				} else {
					params = convertToParameters(mRequest, mSplitArrayParams);
					httpRequest = new HttpGet(spec + (spec.contains("?") ? "&" : "?") + params);
				}
				if (BuildConfig.DEBUG) {
					debuging(EVENT_URL, spec);
					debuging(EVENT_REQUEST_METHOD, mRequestMethod);
					debuging(EVENT_PARAMS, params);
				}
				if (mCookieCached) {
					String cookie = mCookieCached ? NetCookieCache.getCookie(mContext, url)
							: (mCookieCacheId + "=Cookie");
					httpRequest.setHeader("Cookie", cookie);
					if (BuildConfig.DEBUG) {
						debuging(EVENT_REQUEST_COOKIE, cookie);
					}
				}
				if (mReferer != null) {
					httpRequest.setHeader("Refer", mReferer);
				}
				httpRequest.setHeader("Cache-Control", "no-cache");
				HttpParams requestParams = httpRequest.getParams();
				requestParams.setParameter(HTTP.CONTENT_ENCODING, HTTP.UTF_8);
				requestParams.setParameter(HTTP.CHARSET_PARAM, HTTP.UTF_8);
				requestParams.setParameter(HTTP.DEFAULT_PROTOCOL_CHARSET, HTTP.UTF_8);
				DefaultHttpClient client = new DefaultHttpClient();
				HttpParams clientParams = client.getParams();
				clientParams.setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
				clientParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
				HttpResponse httpResponse = client.execute(httpRequest);
				int statusCode = httpResponse.getStatusLine().getStatusCode();
				if (statusCode != HttpStatus.SC_OK && statusCode != HttpStatus.SC_INTERNAL_SERVER_ERROR) {
					throw new HttpStatusException(statusCode);
				}
				mResponseTime = System.currentTimeMillis();
				String response = EntityUtils.toString(httpResponse.getEntity(), HTTP.UTF_8);
				if (statusCode == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
					throw new HttpStatusException(statusCode,
							getStackTrace(response, mHttp500HtmlRegex, mHttp500HtmlRegexGroup));
				}
				if (mCookieCacheable) {
					List<Cookie> cookies = client.getCookieStore().getCookies();
					if (cookies != null) {
						for (Cookie c : cookies) {
							String cookieName = c.getName();
							if (cookieName.contains(mCookieCacheId)) {
								String cookie = cookieName + "=" + c.getValue();
								NetCookieCache.setCookie(mContext, url, cookie);
								if (BuildConfig.DEBUG) {
									debuging(EVENT_RESPONSE_COOKIE, cookie);
								}
								break;
							}
						}
					}
				}
				if (BuildConfig.DEBUG) {
					debuging(EVENT_RESPONSE, response);
				}
				Class<RESPONSE> resJsonClass = (Class<RESPONSE>) ReflectUtil
						.getParameterizedClass(NetTask.this.getClass(), 1);
				RESPONSE resJson = JsonUtil.convert(response, resJsonClass);
				errorCodeVerify(resJson);
				mResponse = resJson;
				if (mResponseConvertible) {
					mResponseConverted = convertFromResponse(mResponse);
				}
				return resJson;
			} catch (Exception e) {
				if (BuildConfig.DEBUG) {
					debuging(EVENT_EXCEPTION, OUtil.printStackTrace(e));
				}
				return e;
			}
		}

		protected void onPostExecute(Object object) {
			for (INetTaskListener<REQUEST, RESPONSE> listener : getListeners(INetTaskListener.class)) {
				if (object instanceof Exception) {
					listener.onException(NetTask.this, (Exception) object);
				} else {
					listener.onComplete(NetTask.this, NetTask.this.getResponse());
				}
			}
			stop();
		}
	}

	/**
	 * Convert restful url expression to exactly url
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
					String restValue = URLEncoder.encode(JsonUtil.convert(value), HTTP.UTF_8);
					url = url.replace(restKey, restValue);
				} catch (Exception e) {

				}
			}
		} else {
			for (OField field : OField.getFields(object.getClass())) {
				try {
					Object value = field.get(object);
					if (value == null) {
						continue;
					}
					String restKey = "{" + field.getName() + "}";
					if (!url.contains(restKey)) {
						continue;
					}
					String restValue = URLEncoder.encode(JsonUtil.convert(value), HTTP.UTF_8);
					url = url.replace(restKey, restValue);
				} catch (Exception e) {

				}
			}
		}
		return url;
	}

	/**
	 * Convert object to url parameters like "key=value"
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
						sb.append(URLEncoder.encode(JsonUtil.convert(value), HTTP.UTF_8));
					}
				} catch (Exception e) {

				}
			}
		} else {
			for (OField field : OField.getFields(object.getClass())) {
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
						sb.append(URLEncoder.encode(JsonUtil.convert(value), HTTP.UTF_8));
					}
				} catch (Exception e) {

				}
			}
		}
		return sb.toString();
	}

	/**
	 * Convert array to url parameter
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
				sb.append(URLEncoder.encode(JsonUtil.convert(value.get(i)), HTTP.UTF_8));
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
				return new StringEntity((String) object, HTTP.UTF_8);
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
									new StringBody(JsonUtil.convert(value), Charset.forName(HTTP.UTF_8)));
						} catch (Exception e) {

						}
					}
				}
			} else {
				for (OField field : OField.getFields(object.getClass())) {
					try {
						Object value = field.get(object);
						if (value == null) {
							continue;
						}
						if (value instanceof File) {
							entity.addPart(field.getName(), new FileBody((File) value));
						} else {
							entity.addPart(field.getName(),
									new StringBody(JsonUtil.convert(value), Charset.forName(HTTP.UTF_8)));
						}
					} catch (Exception e) {

					}
				}
			}
			return entity;
		}
	}

	/**
	 * Parse response of HTTP 550 to get server stack trace
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
			return Html.fromHtml(html500).toString();
		} catch (Exception e) {
			return Html.fromHtml(html500).toString();
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
			mCode = HttpStatus.SC_OK;
		}

		public HttpStatusException(int code) {
			super(code);
		}

		public HttpStatusException(int code, String message) {
			super(code, message);
		}

	}

}

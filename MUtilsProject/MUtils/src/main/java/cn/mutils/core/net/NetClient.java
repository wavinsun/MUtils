package cn.mutils.core.net;

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
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

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

import cn.mutils.core.IClearable;
import cn.mutils.core.annotation.net.Head;
import cn.mutils.core.beans.BeanField;
import cn.mutils.core.codec.FlagUtil;
import cn.mutils.core.err.AbortedException;
import cn.mutils.core.err.HttpStatusException;
import cn.mutils.core.event.IListener;
import cn.mutils.core.json.JsonUtil;
import cn.mutils.core.reflect.ReflectUtil;
import cn.mutils.core.text.StringUtil;
import cn.mutils.core.time.MillisFormat;

/**
 * Template of request and response for net API at Java application level
 */
@SuppressWarnings({"serial", "unchecked", "deprecation", "unused", "UnusedAssignment", "ConstantConditions"})
public class NetClient<REQUEST, RESPONSE> {

    /**
     * Listener for {@link NetClient}
     */
    public static abstract class NetClientListener<REQUEST, RESPONSE> implements IListener {

        /**
         * Get request entity class
         */
        public abstract Class<?> requestRawType();

        /**
         * Get generic type info of request
         */
        public Type requestGenericType() {
            return null;
        }

        /**
         * Get response entity class
         */
        public abstract Class<?> responseRawType();

        /**
         * Get generic type info of response
         */
        public Type responseGenericType() {
            return null;
        }

        /**
         * Convert some properties who are come form UI to REQUEST
         */
        public REQUEST convertToRequest() {
            return null;
        }

        /**
         * Convert RESPONSE to object who are defined by UI
         */
        public Object convertFromResponse(RESPONSE response) {
            return null;
        }

        /**
         * Sign post JSON request content
         */
        public Object signPostJson(REQUEST request) throws Exception {
            return request;
        }

        /**
         * Give cookie to request
         */
        public abstract String requestCookie(URL url);

        /**
         * Receive cookie from response
         */
        public abstract void responseCookie(URL url, String cookie);

        /**
         * Verify error code of RESPONSE
         */
        public void errorCodeVerify(RESPONSE response) throws Exception {
            debugging(EVENT_ERROR_CODE, "errorCodeVerify");
        }

        /**
         * Debugging
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

    /**
     * Flag of request convertible<br>
     * Whether request is convertible
     */
    public static final int FLAG_REQUEST_CONVERTIBLE = FlagUtil.FLAG_01;

    /**
     * Flag of response convertible<br>
     * Whether response is convertible
     */
    public static final int FLAG_RESPONSE_CONVERTIBLE = FlagUtil.FLAG_02;

    /**
     * Flag of split array parameters<br>
     * Whether to request by split array parameters: ids=1,2,3&name=wavinsun
     */
    public static final int FLAG_SPLIT_ARRAY_PARAMS = FlagUtil.FLAG_03;

    /**
     * Flag of rest URL<br>
     * Whether to request by restful URL: http://www.xxx.cn/detail/{id}/{name}/
     */
    public static final int FLAG_REST_URL = FlagUtil.FLAG_04;

    /**
     * Flag of post parameters<br>
     * Whether to request by post parameter:id=1&name=wavinsun
     */
    public static final int FLAG_POST_PARAMS = FlagUtil.FLAG_05;

    /**
     * Flag of post JSON<br>
     * Whether to request by post JSON: {"id":1,name:"wavinsun"}
     */
    public static final int FLAG_POST_JSON = FlagUtil.FLAG_06;

    /**
     * Flag of post JSON signed<br>
     * Whether to request by signed JSON:<br>
     * {"key":"key","sign_type":"MD5","sign":"sign_result","data":"data"}
     */
    public static final int FLAG_POST_JSON_SIGNED = FlagUtil.FLAG_07;

    /**
     * Flag of cookie with request<br>
     * Whether to use cookie cached to make request
     */
    public static final int FLAG_COOKIE_WITH_REQUEST = FlagUtil.FLAG_08;

    /**
     * Flag of cookie with response<br>
     * Whether to cache cookie responded
     */
    public static final int FLAG_COOKIE_WITH_RESPONSE = FlagUtil.FLAG_09;

    protected REQUEST mRequest;

    protected RESPONSE mResponse;

    protected int mFlags = FlagUtil.FLAGS_FALSE;

    /**
     * Response object converted by {@link NetClientListener#convertFromResponse(Object)}
     */
    protected Object mResponseConverted;

    protected String mUrl;

    protected String mRequestMethod = REQUEST_METHOD_GET;

    /**
     * Value of HTTP head Refer
     */
    protected String mRefer;

    /**
     * Cookie identity
     */
    protected String mCookieCacheId = "JSESSIONID";

    /**
     * Regular expression for HTTP 500
     * <p>
     * Apache Tomcat 7.0.47 HTTP Status 500
     */
    protected String mHttp500HtmlRegex = "(.*?)<pre>(.*?)</pre>(.*?)";

    /**
     * Server stack trace index of regular expression for HTTP 500
     * <p>
     * 2 is server stack trace index of Apache Tomcat 7.0.47 HTTP Status 500
     */
    protected int mHttp500HtmlRegexGroup = 2;

    protected long mResponseTime;

    protected HttpUriRequest mHttpRequest;

    protected boolean mAborted;

    protected NetClientListener<REQUEST, RESPONSE> mListener;

    public NetClient() {
        setSplitArrayParams(true);
    }

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
        return FlagUtil.hasFlags(mFlags, FLAG_SPLIT_ARRAY_PARAMS);
    }

    public void setSplitArrayParams(boolean value) {
        mFlags = FlagUtil.setFlags(mFlags, FLAG_SPLIT_ARRAY_PARAMS, value);
    }

    public boolean isRestUrl() {
        return FlagUtil.hasFlags(mFlags, FLAG_REST_URL);
    }

    public void setRestUrl(boolean value) {
        mFlags = FlagUtil.setFlags(mFlags, FLAG_REST_URL, value);
    }

    public boolean isPostParams() {
        return FlagUtil.hasFlags(mFlags, FLAG_POST_PARAMS);
    }

    public void setPostParams(boolean value) {
        mFlags = FlagUtil.setFlags(mFlags, FLAG_POST_PARAMS, value);
    }

    public boolean isPostJson() {
        return FlagUtil.hasFlags(mFlags, FLAG_POST_JSON);
    }

    public void setPostJson(boolean value) {
        mFlags = FlagUtil.setFlags(mFlags, FLAG_POST_JSON, value);
    }

    public boolean isPostJsonSigned() {
        return FlagUtil.hasFlags(mFlags, FLAG_POST_JSON_SIGNED);
    }

    public void setPostJsonSigned(boolean value) {
        mFlags = FlagUtil.setFlags(mFlags, FLAG_POST_JSON_SIGNED, value);
    }

    public boolean isCookieWithRequest() {
        return FlagUtil.hasFlags(mFlags, FLAG_COOKIE_WITH_REQUEST);
    }

    public void setCookieWithRequest(boolean value) {
        mFlags = FlagUtil.setFlags(mFlags, FLAG_COOKIE_WITH_REQUEST, value);
    }

    public boolean isCookieWithResponse() {
        return FlagUtil.hasFlags(mFlags, FLAG_COOKIE_WITH_RESPONSE);
    }

    public void setCookieWithResponse(boolean value) {
        mFlags = FlagUtil.setFlags(mFlags, FLAG_COOKIE_WITH_RESPONSE, value);
    }

    public boolean isRequestConvertible() {
        return FlagUtil.hasFlags(mFlags, FLAG_REQUEST_CONVERTIBLE);
    }

    public void setRequestConvertible(boolean value) {
        mFlags = FlagUtil.setFlags(mFlags, FLAG_REQUEST_CONVERTIBLE, value);
    }

    public boolean isResponseConvertible() {
        return FlagUtil.hasFlags(mFlags, FLAG_RESPONSE_CONVERTIBLE);
    }

    public void setResponseConvertible(boolean value) {
        mFlags = FlagUtil.setFlags(mFlags, FLAG_RESPONSE_CONVERTIBLE, value);
    }

    public Object getResponseConverted() {
        return mResponseConverted;
    }

    public void setResponseConverted(Object responseConverted) {
        mResponseConverted = responseConverted;
    }

    public void setCookieCachedId(String cookieCachedId) {
        mCookieCacheId = cookieCachedId;
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

    public boolean isAborted() {
        return mAborted;
    }

    public synchronized void abort() {
        if (!mAborted) {
            mAborted = true;
            if (mHttpRequest != null && !mHttpRequest.isAborted()) {
                try {
                    mHttpRequest.abort();
                } catch (Exception e) {
                    // UnsupportedOperationException
                }
            }
        }
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
            if (mAborted) {
                throw new AbortedException();
            }
            if (isRequestConvertible() && mListener != null) {
                mRequest = mListener.convertToRequest();
            }
            String params = null, spec = isRestUrl() ? convertToRestUrl(mRequest, mUrl) : mUrl;
            URL url = new URL(spec);
            if (REQUEST_METHOD_POST.equals(mRequestMethod)) {
                synchronized (this) {
                    mHttpRequest = new HttpPost(url.toURI());
                }
                params = isPostParams() ? convertToParameters(mRequest, isSplitArrayParams()) : null;
                if (isPostJson()) {
                    if (isPostJsonSigned() && mListener != null) {
                        params = JsonUtil.toString(mListener.signPostJson(mRequest));
                    } else {
                        params = JsonUtil.toString(mRequest);
                    }
                }
                HttpEntity entity = convertToEntity(params != null ? params : mRequest);
                if (isPostJson() && entity != null) {
                    ((StringEntity) entity).setContentType("application/json");
                }
                ((HttpPost) mHttpRequest).setEntity(entity);
            } else {
                params = convertToParameters(mRequest, isSplitArrayParams());
                synchronized (this) {
                    mHttpRequest = new HttpGet(spec + (spec.contains("?") ? "&" : "?") + params);
                }
            }
            String headers = convertToHeaders(mRequest, mHttpRequest);
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
            if (isCookieWithRequest()) {
                String cookie = mListener != null ? mListener.requestCookie(url) : (mCookieCacheId + "=Cookie");
                mHttpRequest.setHeader("Cookie", cookie);
                if (mListener != null) {
                    mListener.debugging(EVENT_REQUEST_COOKIE, cookie);
                }
            }
            if (mRefer != null) {
                mHttpRequest.setHeader("Refer", mRefer);
            }
            mHttpRequest.setHeader("Cache-Control", "no-cache");
            HttpParams requestParams = mHttpRequest.getParams();
            requestParams.setParameter(HTTP.CONTENT_ENCODING, "UTF-8");
            requestParams.setParameter(HTTP.CHARSET_PARAM, "UTF-8");
            requestParams.setParameter(HTTP.DEFAULT_PROTOCOL_CHARSET, "UTF-8");
            HttpResponse httpResponse = client.execute(mHttpRequest);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != 200 && statusCode != 500) {
                throw new HttpStatusException(statusCode);
            }
            String response = EntityUtils.toString(httpResponse.getEntity(), "UTF-8");
            if (mAborted) {
                throw new AbortedException();
            }
            mResponseTime = System.currentTimeMillis();
            if (statusCode == 500) {
                throw new HttpStatusException(statusCode,
                        getStackTrace(response, mHttp500HtmlRegex, mHttp500HtmlRegexGroup));
            }
            if (isCookieWithResponse()) {
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
                    : ReflectUtil.getParamRawType(getClass(), 1));
            resJsonGenericType = mListener != null ? mListener.responseGenericType() : null;
            RESPONSE resJson = JsonUtil.fromString(response, resJsonClass, resJsonGenericType);
            if (mListener != null) {
                mListener.errorCodeVerify(resJson);
            }
            if (mAborted) {
                throw new AbortedException();
            }
            mResponse = resJson;
            if (isResponseConvertible() && mListener != null) {
                mResponseConverted = mListener.convertFromResponse(mResponse);
            }
            return resJson;
        } catch (Exception e) {
            if (mListener != null) {
                mListener.debugging(EVENT_EXCEPTION, StringUtil.printStackTrace(e));
            }
            return e;
        } finally {
            synchronized (this) {
                mAborted = false;// Reuse
                mHttpRequest = null;
            }
            client.getConnectionManager().shutdown();
            if (resJsonGenericType != null && (resJsonGenericType instanceof IClearable)) {
                ((IClearable) resJsonGenericType).clear();
            }
        }
    }

    /**
     * Convert object whose properties have {@link Head} to headers
     *
     * @param object Object
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
                    String hValue = JsonUtil.toString(value);
                    httpRequest.setHeader(name, hValue);
                    if (sb.length() != 0) {
                        sb.append("\n");
                    }
                    sb.append(name);
                    sb.append(":");
                    sb.append(hValue);
                } catch (Exception e) {
                    // IllegalAccessException
                }
            }
        }
        return sb.toString();
    }

    /**
     * Convert restful URL expression to exactly URL
     */
    public static String convertToRestUrl(Object object, String url) {
        if (url == null) {
            return null;
        }
        if (object == null) {
            return null;
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
                    String restValue = URLEncoder.encode(JsonUtil.toString(value), "UTF-8");
                    url = url.replace(restKey, restValue);
                } catch (Exception e) {
                    // UnsupportedEncodingException
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
                    String restValue = URLEncoder.encode(JsonUtil.toString(value), "UTF-8");
                    url = url.replace(restKey, restValue);
                } catch (Exception e) {
                    // UnsupportedEncodingException
                }
            }
        }
        return url;
    }

    /**
     * Convert object to URL parameters like "key=value"
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
                        sb.append(URLEncoder.encode(JsonUtil.toString(value), "UTF-8"));
                    }
                } catch (Exception e) {
                    // UnsupportedEncodingException
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
                        sb.append(URLEncoder.encode(JsonUtil.toString(value), "UTF-8"));
                    }
                } catch (Exception e) {
                    // UnsupportedEncodingException
                }
            }
        }
        return sb.toString();
    }

    /**
     * Convert array to URL parameter
     */
    public static String convertToSplitArrayParam(List<?> value) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0, size = value.size(); i < size; i++) {
            try {
                if (i != 0) {
                    sb.append(",");
                }
                sb.append(URLEncoder.encode(JsonUtil.toString(value.get(i)), "UTF-8"));
            } catch (Exception e) {
                // UnsupportedEncodingException
            }
        }
        return sb.toString();
    }

    /**
     * Convert object to HttpEntity
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
                                    new StringBody(JsonUtil.toString(value), Charset.forName("UTF-8")));
                        } catch (Exception e) {
                            // UnsupportedCharsetException
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
                                    new StringBody(JsonUtil.toString(value), Charset.forName("UTF-8")));
                        }
                    } catch (Exception e) {
                        // IllegalAccessException
                    }
                }
            }
            return entity;
        }
    }

    /**
     * Parse response of HTTP 500 to get server stack trace
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

}

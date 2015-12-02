package cn.mutils.app.net;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import cn.mutils.app.util.AppUtil;

/**
 * Cookie cache
 */
public class NetCookieCache {

	private static final String COOKIE_FILENAME = "CookieCache";

	private static Object sSync = new Object();

	private static Map<String, String> sCookieMap = new HashMap<String, String>();

	public static String getCookie(Context context, URL url) {
		String cookieKey = getCookieKey(url);
		synchronized (sSync) {
			String cachedCookie = sCookieMap.get(cookieKey);
			if (cachedCookie != null) {
				return cachedCookie;
			}
		}
		return AppUtil.getPrefString(context, COOKIE_FILENAME, cookieKey, "");
	}

	public static boolean setCookie(Context context, URL url, String value) {
		String cookieKey = getCookieKey(url);
		synchronized (sSync) {
			String cachedCookie = sCookieMap.get(cookieKey);
			if (value.equals(cachedCookie)) {
				return true;
			}
			sCookieMap.put(cookieKey, value);
		}
		return AppUtil.setPrefString(context, COOKIE_FILENAME, cookieKey, value);
	}

	private static String getCookieKey(URL url) {
		StringBuilder sb = new StringBuilder();
		sb.append(url.getHost());
		sb.append(url.getPort() != -1 ? url.getPort() : url.getDefaultPort());
		return AppUtil.md5(sb.toString());
	}

}

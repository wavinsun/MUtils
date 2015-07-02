package cn.o.app.conf;

import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import cn.o.app.OUtil;
import cn.o.app.text.StringUtil;

/**
 * Language configuration for application
 */
public class OLocale {

	protected static final String LOCALE_FILENAME = "Locale";

	public static void syncLocale(Context context) {
		Resources res = context.getResources();
		Configuration config = res.getConfiguration();
		Locale locale = getLocale(context);
		if (!config.locale.equals(locale)) {
			config.locale = locale;
			res.updateConfiguration(config, null);
		}
	}

	public static Locale getLocale(Context context) {
		String str = OUtil.getPrefString(context, LOCALE_FILENAME, OUtil.KEY, null);
		if (str == null) {
			return Resources.getSystem().getConfiguration().locale;
		}
		return StringUtil.getLocale(str);
	}

	/**
	 * Follow system if locale is null
	 * 
	 * @param context
	 * @param locale
	 */
	public static void setLocale(Context context, Locale locale) {
		Locale localeAssit = locale != null ? locale : Resources.getSystem().getConfiguration().locale;
		Resources res = context.getResources();
		Configuration config = res.getConfiguration();
		if (!config.locale.equals(localeAssit)) {
			OUtil.setPrefString(context, LOCALE_FILENAME, OUtil.KEY, StringUtil.toString(locale));
			config.locale = localeAssit;
			res.updateConfiguration(config, null);
			try {
				Intent intent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
				context.startActivity(intent);
			} catch (Exception e) {

			}
		}
	}
}

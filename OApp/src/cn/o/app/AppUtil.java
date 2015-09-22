package cn.o.app;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.Paint.FontMetrics;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import cn.o.app.core.archive.ZipUtil;
import cn.o.app.core.beans.BeanField;
import cn.o.app.core.crypto.AESUtil;
import cn.o.app.core.io.IOUtil;
import cn.o.app.core.math.NumberUtil;
import cn.o.app.core.reflect.ReflectUtil;
import cn.o.app.core.text.StringUtil;

/**
 * Utility of framework for Android runtime
 */
@SuppressWarnings("deprecation")
public class AppUtil {

	/** Framework internal data identity key */
	public static final String KEY = "o";

	/** Transform for degress to radian */
	public static final double TO_RADIAN = NumberUtil.TO_RADIAN;
	/** Transform for radian to degress */
	public static final double TO_DEGRESS = NumberUtil.TO_DEGRESS;

	public static SharedPreferences getPref(Context context, String fileName) {
		return context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
	}

	public static String getPrefString(Context context, String fileName, String key, String defValue) {
		return getPref(context, fileName).getString(key, defValue);
	}

	public static boolean setPrefString(Context context, String fileName, String key, String value) {
		Editor editor = getPref(context, fileName).edit();
		editor.putString(key, value);
		return editor.commit();
	}

	public static boolean getPrefBoolean(Context context, String fileName, String key, boolean defValue) {
		return getPref(context, fileName).getBoolean(key, defValue);
	}

	public static boolean setPrefBoolean(Context context, String fileName, String key, Boolean value) {
		Editor editor = getPref(context, fileName).edit();
		editor.putBoolean(key, value);
		return editor.commit();
	}

	public static int getPrefInt(Context context, String fileName, String key, int defValue) {
		return getPref(context, fileName).getInt(key, defValue);
	}

	public static boolean setPrefInt(Context context, String fileName, String key, int value) {
		Editor editor = getPref(context, fileName).edit();
		editor.putInt(key, value);
		return editor.commit();
	}

	public static long getPrefLong(Context context, String fileName, String key, long defValue) {
		return getPref(context, fileName).getLong(key, defValue);
	}

	public static boolean setPrefLong(Context context, String fileName, String key, long value) {
		Editor editor = getPref(context, fileName).edit();
		editor.putLong(key, value);
		return editor.commit();
	}

	public static float getPrefFloat(Context context, String fileName, String key, float defValue) {
		return getPref(context, fileName).getFloat(key, defValue);
	}

	public static boolean setPrefFloat(Context context, String fileName, String key, float value) {
		Editor editor = getPref(context, fileName).edit();
		editor.putFloat(key, value);
		return editor.commit();
	}

	public static double getPrefDouble(Context context, String fileName, String key, double defValue) {
		String doubleStr = getPrefString(context, fileName, key, "");
		if (doubleStr.isEmpty()) {
			return defValue;
		}
		try {
			return Double.parseDouble(doubleStr);
		} catch (Exception e) {
			return defValue;
		}
	}

	public static boolean setPrefDouble(Context context, String fileName, String key, double value) {
		return setPrefString(context, fileName, key, value + "");
	}

	/**
	 * Get string content of assets file
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static String getAssetString(Context context, String fileName) {
		AssetManager am = context.getAssets();
		InputStream is = null;
		try {
			is = am.open(fileName);
			return getString(is);
		} catch (Exception e) {
			return null;
		} finally {
			IOUtil.close(is);
		}
	}

	/**
	 * Get string content of assets zip file
	 * 
	 * @param context
	 * @param fileName
	 * @param entryName
	 * @return
	 */
	public static String getAssetZipString(Context context, String fileName, String entryName) {
		byte[] bytes = getAssetZipBytes(context, fileName, entryName);
		if (bytes == null) {
			return null;
		}
		try {
			return new String(bytes, "UTF-8");
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * Get byte array of assets zip file
	 * 
	 * @param context
	 * @param fileName
	 * @param entryName
	 * @return
	 */
	public static byte[] getAssetZipBytes(Context context, String fileName, String entryName) {
		AssetManager am = context.getAssets();
		InputStream is = null;
		try {
			is = am.open(fileName);
			return ZipUtil.getBytes(is, entryName);
		} catch (Exception e) {
			return null;
		} finally {
			IOUtil.close(is);
		}
	}

	public static String getAppVersionName(Context context) {
		try {
			PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
					PackageManager.GET_META_DATA);
			return info.versionName;
		} catch (NameNotFoundException e) {
			return "";
		}
	}

	public static String getAppPackage(Context context) {
		return context.getPackageName();
	}

	public static String getAppName(Context context) {
		return getAppName(context, context.getPackageName());
	}

	public static String getAppName(Context context, String packageName) {
		try {
			PackageManager pm = context.getPackageManager();
			ApplicationInfo info = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA);
			return pm.getApplicationLabel(info).toString();
		} catch (Exception e) {
			return "";
		}
	}

	public static Bitmap getAppIcon(Context context) {
		try {
			Class<?> drawableClass = Class.forName(context.getPackageName() + ".R$drawable");
			Field f = drawableClass.getField("ic_launcher");
			return BitmapFactory.decodeResource(context.getResources(), f.getInt(null));
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean isAppInstalled(Context context, String name) {
		try {
			context.getPackageManager().getPackageInfo(name, 0);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static float getRawSize(Context context, int unit, float size) {
		return TypedValue.applyDimension(unit, size,
				(context == null ? Resources.getSystem() : context.getResources()).getDisplayMetrics());
	}

	public static float dp2px(Context context, float size) {
		return size * (context == null ? Resources.getSystem() : context.getResources()).getDisplayMetrics().density;
	}

	public static float sp2px(Context context, float size) {
		return size
				* (context == null ? Resources.getSystem() : context.getResources()).getDisplayMetrics().scaledDensity;
	}

	public static float px2dp(Context context, float size) {
		return size / (context == null ? Resources.getSystem() : context.getResources()).getDisplayMetrics().density;
	}

	public static float px2sp(Context context, float size) {
		return size
				/ (context == null ? Resources.getSystem() : context.getResources()).getDisplayMetrics().scaledDensity;
	}

	public static String md5(String text) {
		return StringUtil.md5(text);
	}

	public static <T> ArrayList<T> asArrayList(T[] array) {
		ArrayList<T> arrayList = new ArrayList<T>();
		if (array == null) {
			return arrayList;
		}
		for (int i = 0, size = array.length; i < size; i++) {
			arrayList.add(array[i]);
		}
		return arrayList;
	}

	public static boolean compress(String sourceImage) {
		return compress(sourceImage, sourceImage);
	}

	public static boolean compress(String sourceImage, String destImage) {
		return compress(sourceImage, destImage, 0, 0);
	}

	public static boolean compress(String sourceImage, int expectWidth, int expectHeight) {
		return compress(sourceImage, sourceImage, expectWidth, expectHeight);
	}

	public static boolean compress(String sourceImage, String destImage, int expectWidth, int expectHeight) {
		BitmapFactory.Options opts = new BitmapFactory.Options();
		opts.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(sourceImage, opts);
		if (expectWidth > 0 && expectHeight > 0) {
			float width = opts.outWidth;
			float height = opts.outHeight;
			float scaleX = 1;
			float scaleY = 1;
			if (width < height) {
				if (expectWidth < expectHeight) {
					scaleX = expectWidth / width;
					scaleY = expectHeight / height;
				} else {
					scaleX = expectHeight / width;
					scaleY = expectWidth / height;
				}
			} else {
				if (expectWidth < expectHeight) {
					scaleX = expectWidth / height;
					scaleY = expectHeight / width;
				} else {
					scaleX = expectHeight / height;
					scaleY = expectWidth / width;
				}
			}
			float scale = scaleX < scaleY ? scaleX : scaleY;
			if (scale < 1) {
				opts.inSampleSize = (int) (1 / scale + 0.5f);
			}
		}
		opts.inJustDecodeBounds = false;
		opts.inInputShareable = true;
		opts.inPurgeable = true;
		Bitmap bitmap = BitmapFactory.decodeFile(sourceImage, opts);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(destImage);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos);
			bitmap.recycle();
			fos.flush();
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			IOUtil.close(fos);
		}
	}

	public static byte[] bitmap2ByteArray(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			return baos.toByteArray();
		} catch (Exception e) {
			return null;
		} finally {
			IOUtil.close(baos);
		}
	}

	public static Bitmap bitmap2Grey(Bitmap bitmap) {
		Bitmap grey = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas c = new Canvas(grey);
		Paint p = new Paint();
		ColorMatrix cm = new ColorMatrix();
		cm.setSaturation(0);
		ColorMatrixColorFilter cmf = new ColorMatrixColorFilter(cm);
		p.setColorFilter(cmf);
		c.drawBitmap(bitmap, 0, 0, p);
		return grey;
	}

	public static String uuid() {
		return StringUtil.uuid();
	}

	/**
	 * Get disk cache root directory
	 * 
	 * @param context
	 * @return
	 */
	public static String getDiskCacheRoot(Context context) {
		String cachePath = null;
		if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
			File externalCacheDir = context.getExternalCacheDir();
			if (externalCacheDir != null) {
				cachePath = externalCacheDir.getPath();
			}
		}
		if (cachePath == null) {
			File cacheDir = context.getCacheDir();
			if (cacheDir != null) {
				cachePath = cacheDir.getPath();
			}
		}
		File cacheRoot = new File(cachePath);
		if (!cacheRoot.exists()) {
			if (!cacheRoot.mkdirs()) {
				return null;
			}
		}
		return cacheRoot.getPath();
	}

	public static String getDiskCacheDir(Context context, String dirName) {
		File dir = new File(getDiskCacheRoot(context) + File.separator + dirName);
		if (!dir.exists()) {
			if (!dir.mkdirs()) {
				return null;
			}
		}
		return dir.getPath();
	}

	public static String getDiskCacheRandomFile(Context context, String prefix, String suffix) {
		String cacheDir = getDiskCacheDir(context, "OApp");
		if (cacheDir == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(cacheDir);
		sb.append(File.separator);
		if (prefix != null) {
			sb.append(prefix);
		}
		sb.append(StringUtil.uuid());
		if (suffix != null) {
			sb.append(suffix);
		}
		return sb.toString();
	}

	public static String getDiskCacheRandomJpg(Context context) {
		return getDiskCacheRandomFile(context, "IMG_", ".jpg");
	}

	/**
	 * Calculate distance for two points by given latitude and longitude
	 * 
	 * @param latitudeA
	 * @param longitudeA
	 * @param latitudeB
	 * @param longitudeB
	 * @return
	 */
	public static double getDistance(double latitudeA, double longitudeA, double latitudeB, double longitudeB) {
		double radianLatitudeA = latitudeA * TO_RADIAN;
		double radianLatitudeB = latitudeB * TO_RADIAN;
		double radianLatidueDistance = radianLatitudeA - radianLatitudeB;
		double radianLongitudeDistance = (longitudeA - longitudeB) * TO_RADIAN;
		return 6378137 * 2
				* Math.asin(Math.sqrt(Math.pow(Math.sin(radianLatidueDistance * 0.5), 2) + Math.cos(radianLatitudeA)
						* Math.cos(radianLatitudeB) * Math.pow(Math.sin(radianLongitudeDistance * 0.5), 2)));
	}

	public static int getYear(Date date) {
		return date.getYear() + 1900;
	}

	public static int getMonth(Date date) {
		return date.getMonth() + 1;
	}

	public static int getDay(Date date) {
		return date.getDate();
	}

	/**
	 * Whether is leap year 闰年
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isLeap(Date date) {
		int year = date.getYear() + 1900;
		return isLeap(year);
	}

	/**
	 * Whether is leap year 闰年
	 * 
	 * @param date
	 * @return
	 */
	public static boolean isLeap(int year) {
		return (year % 4 == 0 && year % 100 != 0) || year % 400 == 0;
	}

	public static int getDaysOfMonth(Date date) {
		return getDaysOfMonth(AppUtil.getYear(date), AppUtil.getMonth(date));
	}

	public static Date getDate(int year, int month, int day) {
		return new Date(year - 1900, month - 1, day);
	}

	public static Date getDate(int year, int month, int day, int hour, int minute) {
		return new Date(year - 1900, month - 1, day, hour, minute);
	}

	public static int getDaysOfMonth(int year, int month) {
		if (month == 2) {
			return isLeap(year) ? 29 : 28;
		} else if (month == 4 || month == 6 || month == 9 || month == 11) {
			return 30;
		} else {
			return 31;
		}
	}

	public static boolean isSameDay(Date d1, Date d2) {
		return (d1 != null && d2 != null)
				? (d1.getYear() == d2.getYear() && d1.getMonth() == d2.getMonth() && d1.getDate() == d2.getDate())
				: (d1 == d2);
	}

	public static boolean isSameMonth(Date d1, Date d2) {
		return (d1 != null && d2 != null) ? (d1.getYear() == d2.getYear() && d1.getMonth() == d2.getMonth())
				: (d1 == d2);
	}

	public static boolean isEmpty(String str) {
		return str == null ? true : str.isEmpty();
	}

	public static String getPhoneNumber(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return tm.getLine1Number();
	}

	/**
	 * Get phone service provider
	 * 
	 * @param context
	 * @return 10086 10010 10000
	 */
	public static String getPhoneServiceProvider(Context context) {
		TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		String id = tm.getSubscriberId();
		if (id == null) {
			return null;
		}
		if (id.startsWith("46000") || id.startsWith("46002")) {
			return "10086";
		}
		if (id.startsWith("46001")) {
			return "10010";
		}
		if (id.startsWith("46003")) {
			return "10000";
		}
		return null;
	}

	public static String getString(File file) {
		return StringUtil.get(file);
	}

	public static String getString(InputStream is) {
		return StringUtil.get(is);
	}

	public static boolean equals(Object one, Object another) {
		if (one == another) {
			return true;
		}
		if (one == null) {
			return false;
		} else {
			return one.equals(another);
		}
	}

	public static <T> T findByProperty(List<T> list, String property, Object propertyValue) {
		if (list == null) {
			return null;
		}
		if (list.size() == 0) {
			return null;
		}
		BeanField propertyField = BeanField.getField(list.get(0), property);
		if (propertyField == null) {
			return null;
		}
		for (T element : list) {
			if (AppUtil.equals(propertyValue, ReflectUtil.get(element, propertyField))) {
				return element;
			}
		}
		return null;
	}

	public static <T> List<T> findAllByProperty(List<T> list, String property, Object propertyValue) {
		if (list == null) {
			return null;
		}
		if (list.size() == 0) {
			return null;
		}
		BeanField propertyField = BeanField.getField(list.get(0), property);
		if (propertyField == null) {
			return null;
		}
		List<T> result = new ArrayList<T>();
		for (T element : list) {
			try {
				Object v = propertyField.get(element);
				if (AppUtil.equals(v, propertyValue)) {
					result.add(element);
				}
			} catch (Exception e) {

			}
		}
		if (result.size() != 0) {
			return result;
		}
		return null;
	}

	public static boolean isVersionStable(String version) {
		return StringUtil.isVersionStable(version);
	}

	public static void exit() {
		exit(0);
	}

	public static void exit(int code) {
		android.os.Process.killProcess(android.os.Process.myPid());
		System.exit(code);
	}

	public static String printStackTrace(Exception e) {
		return StringUtil.printStackTrace(e);
	}

	/**
	 * Get meta data of named node: fix bug for integer values
	 * 
	 * @param context
	 * @param name
	 * @return
	 */
	public static String getMetaData(Context context, String name) {
		try {
			return context.getPackageManager().getApplicationInfo(context.getPackageName(),
					PackageManager.GET_META_DATA).metaData.get(name).toString();
		} catch (Exception e) {
			return null;
		}
	}

	public static boolean startApp(Context context) {
		return startApp(context, context.getPackageName());
	}

	public static boolean startApp(Context context, String packageName) {
		try {
			ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
			for (RunningTaskInfo taskInfo : am.getRunningTasks(500)) {
				if (taskInfo.topActivity.getPackageName().equals(packageName)) {
					Intent intent = new Intent();
					intent.setClassName(packageName, taskInfo.topActivity.getClassName());
					intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
					context.startActivity(intent);
					return true;
				}
			}
			Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
			if (intent == null) {
				return false;
			}
			context.startActivity(intent);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public static boolean installApp(Context context, File apkFile) {
		try {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
			context.startActivity(intent);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Fix bug for AsyncTask.onPostExecute method can not execute because
	 * InternalHandler creation is not in main thread.Please call this method in
	 * OnCreate of Application.
	 */
	public static void fixAsyncTask() {
		Looper looper = Looper.getMainLooper();
		Handler handler = new Handler(looper);
		handler.post(new AsyncTaskLoaderRunnable());
	}

	public static Locale getLocale(Context context) {
		return AppLocale.getLocale(context);
	}

	public static void setLocale(Context context, Locale locale) {
		AppLocale.setLocale(context, locale);
	}

	public static void setLocaleFromString(Context context, String locale) {
		AppLocale.setLocale(context, StringUtil.getLocale(locale));
	}

	/**
	 * AES encrypt
	 * 
	 * @param text
	 * @param pwd
	 * @return
	 */
	public static String toAES(String text, String pwd) {
		return AESUtil.encrypt(text, pwd);
	}

	/**
	 * AES decrypt
	 * 
	 * @param hex
	 * @param pwd
	 * @return
	 */
	public static String fromAES(String hex, String pwd) {
		return AESUtil.decrypt(hex, pwd);
	}

	/**
	 * toStirng of JSON
	 * 
	 * @param obj
	 * @return
	 */
	public static String toStringJSON(Object obj) {
		return StringUtil.toJSON(obj);
	}

	/**
	 * toString of XML
	 * 
	 * @return
	 */
	public static String toStringXML(Object obj) {
		return StringUtil.toXML(obj);
	}

	/**
	 * Get translate animation of PathButton.<br>
	 * The visibility of path button and anchor view must be
	 * {@link View#VISIBLE} or {@link View#INVISIBLE}.
	 * 
	 * @param isOpen
	 *            Is open for button
	 * @param button
	 *            Path button
	 * @param anchor
	 *            Control button
	 * @return
	 */
	public static Animation animOfPathButton(boolean isOpen, View button, View anchor) {
		int[] anchorL = new int[2];
		anchor.getLocationOnScreen(anchorL);
		int[] buttonL = new int[2];
		button.getLocationOnScreen(buttonL);
		int x = anchorL[0] - buttonL[0] + (anchor.getWidth() - button.getWidth()) / 2;
		int y = anchorL[1] - buttonL[1] + (anchor.getHeight() - button.getHeight()) / 2;
		TranslateAnimation anim = new TranslateAnimation(Animation.ABSOLUTE, isOpen ? x : 0, Animation.ABSOLUTE,
				isOpen ? 0 : x, Animation.ABSOLUTE, isOpen ? y : 0, Animation.ABSOLUTE, isOpen ? 0 : y);
		anim.setDuration(300);
		anim.setFillAfter(true);
		return anim;
	}

	public static float getYOfDrawText(Paint p, float centerY) {
		FontMetrics metrics = p.getFontMetrics();
		return centerY - (metrics.top + (metrics.bottom - metrics.top) / 2);
	}

	public static ViewGroup getParent(View v, int parentId) {
		ViewGroup p = (ViewGroup) v.getParent();
		while (true) {
			if (p == null) {
				return null;
			}
			if (p.getId() == parentId) {
				return p;
			}
			p = (ViewGroup) p.getParent();
		}
	}

	public static TextWatcher setEditTextDecimals(EditText editText, int decimals) {
		EditTextDecimalsTextWatcher watcher = new EditTextDecimalsTextWatcher();
		watcher.setEditText(editText);
		watcher.setDecimals(decimals);
		editText.addTextChangedListener(watcher);
		return watcher;
	}

	public static class EditTextDecimalsTextWatcher implements TextWatcher {

		protected EditText mEditText;

		protected int mDecimals;

		protected boolean catchChanged;

		public void setEditText(EditText editText) {
			mEditText = editText;
		}

		public void setDecimals(int decimals) {
			mDecimals = decimals;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (catchChanged) {
				return;
			}
			catchChanged = true;
			String str = s.toString();
			int dotIndex = str.indexOf(".");
			if (dotIndex >= 0) {
				String strDecmals = str.substring(dotIndex + 1);
				if (strDecmals.length() > mDecimals) {
					str = str.substring(0, dotIndex + 1 + mDecimals);
					mEditText.setText(str);
					mEditText.setSelection(str.length());
				}
			}
			catchChanged = false;
		}
	}

	public static class AsyncTaskLoaderRunnable implements Runnable {

		public void run() {
			try {
				Class.forName("android.os.AsyncTask");
			} catch (Exception e) {

			}
		}

	}
}
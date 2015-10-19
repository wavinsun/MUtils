package cn.o.app;

import java.lang.Thread.UncaughtExceptionHandler;

import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateConfig;
import com.umeng.update.UpdateStatus;

import android.app.Application;
import android.content.Context;
import cn.jpush.android.api.JPushInterface;
import cn.o.app.core.log.Logs;
import cn.o.app.os.IContextProvider;
import cn.sharesdk.framework.ShareSDK;

/**
 * Application of framework
 */
public class App extends Application implements IContextProvider {

	public static final String UMENG_APPKEY = "UMENG_APPKEY";

	public static final String JPUSH_APPKEY = "JPUSH_APPKEY";

	public static final String SHARE_SDK_INFO = "ShareSDK.xml";

	protected static App sApp;

	protected boolean mUmengEnabled;

	protected boolean mJPushEnabled;

	protected boolean mShareSDKEnabled;

	@Override
	public void onCreate() {

		// Set exception handler to forbid system crash dialog
		Thread.setDefaultUncaughtExceptionHandler(new AppExceptionHandler());

		AppLocale.syncLocale(this);

		AppUtil.fixAsyncTask();

		sApp = this;

		mUmengEnabled = AppUtil.getMetaData(this, UMENG_APPKEY) != null;
		if (mUmengEnabled) {
			if (BuildConfig.DEBUG) {
				MobclickAgent.setDebugMode(true);
				MobclickAgent.setCatchUncaughtExceptions(false);
				UpdateConfig.setDebug(true);
			} else {
				UmengUpdateAgent.setUpdateCheckConfig(false);
			}
			UmengUpdateAgent.setDeltaUpdate(true);
			UmengUpdateAgent.setUpdateOnlyWifi(false);
			UmengUpdateAgent.setUpdateUIStyle(UpdateStatus.STYLE_DIALOG);
			UmengUpdateAgent.setUpdateAutoPopup(false);
		}
		mJPushEnabled = AppUtil.getMetaData(this, JPUSH_APPKEY) != null;
		if (mJPushEnabled) {
			if (BuildConfig.DEBUG) {
				JPushInterface.setDebugMode(true);
			}
			JPushInterface.init(this);
		}
		mShareSDKEnabled = AppUtil.isAssetExists(this, SHARE_SDK_INFO);
		if (mShareSDKEnabled) {
			ShareSDK.initSDK(this);
		}
	}

	public static App getApp() {
		return sApp;
	}

	@Override
	public Context getContext() {
		return this;
	}

	public boolean isUmengEnabled() {
		return mUmengEnabled;
	}

	public boolean isJPushEneabled() {
		return mJPushEnabled;
	}

	class AppExceptionHandler implements UncaughtExceptionHandler {

		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			Logs.e("AndroidRuntime", thread.getName(), ex);
			try {
				Thread.sleep(300L);
			} catch (Exception e) {
				// crash again
			} finally {
				if (mUmengEnabled) {
					MobclickAgent.onKillProcess(App.this);
				}
				if (mJPushEnabled) {
					JPushInterface.onKillProcess(App.this);
				}
				AppUtil.exit(10);
			}
		}
	}
}

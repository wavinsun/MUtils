package cn.mutils.app;

import java.lang.Thread.UncaughtExceptionHandler;

import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateConfig;
import com.umeng.update.UpdateStatus;

import android.app.Application;
import android.content.Context;
import cn.jpush.android.api.JPushInterface;
import cn.mutils.app.core.log.Logs;
import cn.mutils.app.core.task.RepeatTask;
import cn.mutils.app.core.task.RepeatTask.RepeatTaskListener;
import cn.mutils.app.core.task.RepeatTaskManager;
import cn.mutils.app.os.IContextProvider;
import cn.sharesdk.framework.ShareSDK;

/**
 * Application of framework
 */
@SuppressWarnings("unchecked")
public class App<T extends App<T>> extends Application implements IContextProvider {

	/**
	 * Application Edition
	 */
	public static enum Edition {

		/** Debug edition */
		DEBUG,

		/** Beta edition */
		BETA,

		/** Release edition */
		RELEASE

	}

	protected static App<?> sApp;

	protected boolean mUmengEnabled;

	protected boolean mJPushEnabled;

	protected boolean mShareSDKEnabled;

	protected Edition mEdition;

	protected RepeatTaskManager mRepeatTaskManager;

	@Override
	public void onCreate() {

		// Set exception handler to forbid system crash dialog
		Thread.setDefaultUncaughtExceptionHandler(new AppExceptionHandler());

		sApp = this;
		AppLocale.syncLocale(this);
		AppUtil.fixAsyncTask();

		mEdition = detectEdition();
		mUmengEnabled = AppUtil.getAppMetaData(this, "UMENG_APPKEY") != null;
		if (mUmengEnabled) {
			if (mEdition == Edition.DEBUG) {
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
		mJPushEnabled = AppUtil.getAppMetaData(this, "JPUSH_APPKEY") != null;
		if (mJPushEnabled) {
			if (mEdition == Edition.DEBUG) {
				JPushInterface.setDebugMode(true);
			}
			JPushInterface.init(this);
		}
		mShareSDKEnabled = AppUtil.isAssetExists(this, "ShareSDK.xml");
		if (mShareSDKEnabled) {
			ShareSDK.initSDK(this);
		}
	}

	/**
	 * Get edition of application<br>
	 * 
	 * @return
	 */
	public Edition getEdition() {
		if (mEdition == null) {
			mEdition = detectEdition();
		}
		return mEdition;
	}

	/**
	 * Detect edition of application<br>
	 * 
	 * @return
	 */
	protected Edition detectEdition() {
		if (BuildConfig.DEBUG) {
			return Edition.DEBUG;
		} else {
			String channel = AppUtil.getAppMetaData(this, "UMENG_CHANNEL");
			if (channel == null) {
				return Edition.RELEASE;
			} else {
				if (channel.equalsIgnoreCase("debug")) {
					return Edition.DEBUG;
				} else if (channel.equalsIgnoreCase("beta")) {
					return Edition.BETA;
				} else {
					return Edition.RELEASE;
				}
			}
		}
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

	/**
	 * Repeat task
	 * 
	 * @param name
	 * @param times
	 * @param listener
	 */
	public void repeat(String name, int count, RepeatTaskListener listener) {
		RepeatTask task = new RepeatTask();
		task.setName(name);
		task.setCount(count);
		task.addListener(listener);
		if (mRepeatTaskManager == null) {
			mRepeatTaskManager = new RepeatTaskManager();
		}
		mRepeatTaskManager.add(task);
	}

	public static <T extends App<T>> T getApp() {
		return (T) sApp;
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

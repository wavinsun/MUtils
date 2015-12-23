package cn.mutils.app;

import android.app.Application;
import android.content.Context;

import com.umeng.analytics.MobclickAgent;

import java.lang.Thread.UncaughtExceptionHandler;

import cn.mutils.app.core.codec.FlagUtil;
import cn.mutils.app.core.log.Logs;
import cn.mutils.app.core.task.RepeatTask;
import cn.mutils.app.core.task.RepeatTask.RepeatTaskListener;
import cn.mutils.app.core.task.RepeatTaskManager;
import cn.mutils.app.core.util.Edition;
import cn.mutils.app.os.IContextProvider;
import cn.mutils.app.util.AppUtil;
import cn.mutils.app.util.JPushHelper;
import cn.mutils.app.util.ShareSDKHelper;
import cn.mutils.app.util.UmengHelper;

/**
 * Application of framework
 */
public class App extends Application implements IContextProvider {

	public static final int FLAG_UMENG = FlagUtil.FLAG_01;
	public static final int FLAG_JPUSH = FlagUtil.FLAG_02;
	public static final int FLAG_SHARE_SDK = FlagUtil.FLAG_03;

	protected static App sApp;

	protected int mFlags = FlagUtil.FLAGS_FALSE;

	protected Edition mEdition;

	protected RepeatTaskManager mRepeatTaskManager;

	protected JPushHelper mJPushHelper;

	@Override
	public void onCreate() {

		// Set exception handler to forbid system crash dialog
		Thread.setDefaultUncaughtExceptionHandler(new AppExceptionHandler());

		sApp = this;
		AppLocale.syncLocale(this);
		AppUtil.fixAsyncTask();

		mEdition = detectEdition();
		mFlags = FlagUtil.setFlags(mFlags, FLAG_UMENG, UmengHelper.isUmengEnabled(this));
		if (isUmengEnabled()) {
			UmengHelper.initUmeng(this);
		}
		mJPushHelper=new JPushHelper();
		mFlags = FlagUtil.setFlags(mFlags, FLAG_JPUSH, mJPushHelper.delegate().isJPushEnabled(this));
		if (isJPushEneabled()) {
			mJPushHelper.delegate().initJPush(this);
		}
		mFlags = FlagUtil.setFlags(mFlags, FLAG_SHARE_SDK, ShareSDKHelper.isShareSDKEnabled(this));
		if (isShareSDKEnabled()) {
			ShareSDKHelper.initShareSDK(this);
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
		return FlagUtil.hasFlags(mFlags, FLAG_UMENG);
	}

	public boolean isJPushEneabled() {
		return FlagUtil.hasFlags(mFlags, FLAG_JPUSH);
	}

	public boolean isShareSDKEnabled() {
		return FlagUtil.hasFlags(mFlags, FLAG_SHARE_SDK);
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

	public static App getApp() {
		return sApp;
	}

	class AppExceptionHandler implements UncaughtExceptionHandler {

		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			Logs.e(AppUtil.TAG_ANDROID_RUNTIME, thread.getName(), ex);
			try {
				Thread.sleep(300L);
			} catch (Exception e) {
				// crash again
			} finally {
				if (isUmengEnabled()) {
					MobclickAgent.onKillProcess(App.this);
				}
				if (isJPushEneabled()) {
					mJPushHelper.delegate().onKillProcess(App.this);
				}
				AppUtil.exit(10);
			}
		}
	}
}

package cn.mutils.app.open;

import android.content.Context;
import cn.jpush.android.api.JPushInterface;
import cn.mutils.app.App;
import cn.mutils.app.AppUtil;
import cn.mutils.app.core.build.Edition;
import cn.mutils.app.core.log.Logs;

public class JPushHelper {

	protected Context mContext;

	public JPushHelper(Context context) {
		mContext = context;
	}

	public void onResume() {
		if (App.getApp() == null || !App.getApp().isJPushEneabled()) {
			return;
		}
		JPushInterface.onResume(mContext);
	}

	public void onPause() {
		if (App.getApp() == null || !App.getApp().isJPushEneabled()) {
			return;
		}
		JPushInterface.onPause(mContext);
	}

	public static boolean isJPushEnabled(Context context) {
		return AppUtil.getAppMetaData(context, "JPUSH_APPKEY") != null;
	}

	public static void initJPush(Context context) {
		try {
			if (App.getApp() != null && App.getApp().getEdition() == Edition.DEBUG) {
				JPushInterface.setDebugMode(true);
			}
			JPushInterface.init(context);
		} catch (Throwable tr) {
			// java.lang.UnsatisfiedLinkError
			Logs.e(AppUtil.TAG_ANDROID_RUNTIME, tr);
		}
	}

}

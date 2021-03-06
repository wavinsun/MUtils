package cn.mutils.app.jpush.impl;

import android.content.Context;

import cn.jpush.android.api.JPushInterface;
import cn.mutils.app.App;
import cn.mutils.app.jpush.IJPushHelper;
import cn.mutils.app.util.AppUtil;
import cn.mutils.core.log.Logs;
import cn.mutils.core.util.Edition;

@SuppressWarnings("unused")
public class JPushHelperImpl implements IJPushHelper {

    @Override
    public void onResume(Context context) {
        if (App.getApp() == null || !App.getApp().isJPushEneabled()) {
            return;
        }
        JPushInterface.onResume(context);
    }

    @Override
    public void onPause(Context context) {
        if (App.getApp() == null || !App.getApp().isJPushEneabled()) {
            return;
        }
        JPushInterface.onPause(context);
    }

    @Override
    public boolean isJPushEnabled(Context context) {
        return AppUtil.getAppMetaData(context, "JPUSH_APPKEY") != null;
    }

    @Override
    public void initJPush(Context context) {
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

    @Override
    public void onKillProcess(Context context) {
        JPushInterface.onKillProcess(context);
    }

}

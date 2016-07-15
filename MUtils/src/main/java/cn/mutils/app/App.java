package cn.mutils.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import java.lang.Thread.UncaughtExceptionHandler;

import cn.mutils.app.jpush.IJPushHelper;
import cn.mutils.app.os.IContextProvider;
import cn.mutils.app.ssdk.IShareSDKHelper;
import cn.mutils.app.umeng.IUmengHelper;
import cn.mutils.app.util.AppUtil;
import cn.mutils.core.codec.FlagUtil;
import cn.mutils.core.log.Logs;
import cn.mutils.core.runtime.CC;
import cn.mutils.core.task.RepeatTask;
import cn.mutils.core.task.RepeatTask.RepeatTaskListener;
import cn.mutils.core.task.RepeatTaskManager;
import cn.mutils.core.util.Edition;

/**
 * Application of framework
 */
@SuppressWarnings({"UnnecessaryUnboxing", "UnnecessaryBoxing", "unused"})
@SuppressLint("MissingSuperCall")
public class App extends Application implements IContextProvider {

    public static final int FLAG_UMENG = FlagUtil.FLAG_01;
    public static final int FLAG_JPUSH = FlagUtil.FLAG_02;
    public static final int FLAG_SHARE_SDK = FlagUtil.FLAG_03;

    protected static App sApp;

    protected static String sTencentAppId;
    protected static String sWechatAppId;

    protected int mFlags = FlagUtil.FLAGS_FALSE;

    protected Edition mEdition;

    protected RepeatTaskManager mRepeatTaskManager;

    protected IJPushHelper mJPushHelper;

    protected IUmengHelper mUmengHelper;

    @Override
    public void onCreate() {

        // Set exception handler to forbid system crash dialog
        Thread.setDefaultUncaughtExceptionHandler(new AppExceptionHandler());

        sApp = this;
        synchronized (AppBuildConfig.class) { //BuildConfig.DEBUG
            AppBuildConfig.DEBUG = AppUtil.isAppDebugType(this);
        }
        AppLocale.syncLocale(this);
        AppUtil.fixAsyncTask();

        mEdition = detectEdition();
        mUmengHelper = CC.getService(IUmengHelper.class);
        if (mUmengHelper != null) {
            mFlags = FlagUtil.setFlags(mFlags, FLAG_UMENG, mUmengHelper.isUmengEnabled(this));
            if (isUmengEnabled()) {
                mUmengHelper.initUmeng(this);
            }
        }
        mJPushHelper = CC.getService(IJPushHelper.class);
        if (mJPushHelper != null) {
            mFlags = FlagUtil.setFlags(mFlags, FLAG_JPUSH, mJPushHelper.isJPushEnabled(this));
            if (isJPushEneabled()) {
                mJPushHelper.initJPush(this);
            }
        }
        IShareSDKHelper shareSDKHelper = CC.getService(IShareSDKHelper.class);
        if (shareSDKHelper != null) {
            mFlags = FlagUtil.setFlags(mFlags, FLAG_SHARE_SDK, shareSDKHelper.isShareSDKEnabled(this));
            if (isShareSDKEnabled()) {
                shareSDKHelper.initShareSDK(this);
            }
        }
    }

    /**
     * Get edition of application<br>
     */
    public Edition getEdition() {
        if (mEdition == null) {
            mEdition = detectEdition();
        }
        return mEdition;
    }

    /**
     * Detect edition of application<br>
     */
    protected Edition detectEdition() {
        if (AppBuildConfig.DEBUG) {
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

    public static void setTencentAppId(String appId) {
        sTencentAppId = appId;
    }

    public static String getTencentAppId() {
        return sTencentAppId;
    }

    public static void setWechatAppId(String appId) {
        sWechatAppId = appId;
    }

    public static String getWechatAppId() {
        return sWechatAppId;
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
                if (mUmengHelper != null && isUmengEnabled()) {
                    mUmengHelper.onKillProcess(App.this);
                }
                if (mJPushHelper != null && isJPushEneabled()) {
                    mJPushHelper.onKillProcess(App.this);
                }
                AppUtil.exit(10);
            }
        }
    }
}

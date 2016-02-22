package cn.mutils.app.util;

import android.content.Context;

import cn.mutils.core.event.listener.VersionUpdateListener;
import cn.mutils.core.runtime.Delegate;

public class UmengHelper extends Delegate<UmengHelper> {

    public static final String CLASS_DELEGATE = "cn.mutils.app.umeng.UmengHelperDelegate";

    @Override
    public String classDelegate() {
        return CLASS_DELEGATE;
    }

    public void onResume(Context context) {

    }

    public void onPause(Context context) {

    }

    public void onDestroy(Context context) {

    }

    public void finish(Context context) {

    }

    public boolean hasNewVersion(Context context) {
        return false;
    }

    public void checkNewVersion(Context context, VersionUpdateListener listener) {

    }

    public void feedback(Context context) {

    }

    public boolean isUmengEnabled(Context context) {
        return false;
    }

    public void initUmeng(Context context) {

    }

    public void onKillProcess(Context context) {

    }

}

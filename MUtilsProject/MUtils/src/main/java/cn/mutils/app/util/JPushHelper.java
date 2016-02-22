package cn.mutils.app.util;

import android.content.Context;

import cn.mutils.core.runtime.Delegate;

public class JPushHelper extends Delegate<JPushHelper> {

    public static final String CLASS_DELEGATE = "cn.mutils.app.jpush.JPushHelperDelegate";

    @Override
    public String classDelegate() {
        return CLASS_DELEGATE;
    }

    public void onResume(Context context) {

    }

    public void onPause(Context context) {

    }

    public boolean isJPushEnabled(Context context) {
        return false;
    }

    public void initJPush(Context context) {

    }

    public void onKillProcess(Context context) {

    }

}

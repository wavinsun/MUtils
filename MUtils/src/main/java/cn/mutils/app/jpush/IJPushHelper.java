package cn.mutils.app.jpush;

import android.content.Context;

import cn.mutils.core.runtime.IService;

public interface IJPushHelper extends IService {

    void onResume(Context context);

    void onPause(Context context);

    boolean isJPushEnabled(Context context);

    void initJPush(Context context);

    void onKillProcess(Context context);

}

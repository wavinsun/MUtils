package cn.mutils.app.umeng;

import android.content.Context;

import cn.mutils.core.event.listener.VersionUpdateListener;
import cn.mutils.core.runtime.IService;

public interface IUmengHelper extends IService {

    void onResume(Context context);

    void onPause(Context context);

    void onDestroy(Context context);

    void finish(Context context);

    boolean hasNewVersion(Context context);

    void checkNewVersion(Context context, VersionUpdateListener listener);

    void feedback(Context context);

    boolean isUmengEnabled(Context context);

    void initUmeng(Context context);

    void onKillProcess(Context context);

}

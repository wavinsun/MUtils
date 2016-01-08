package cn.mutils.app.ui.core;

import android.content.Context;
import android.content.Intent;

import java.util.List;

import cn.mutils.app.event.listener.OnActivityResultListener;
import cn.mutils.app.os.IContextProvider;

public interface IActivityExecutor extends IContextProvider {

    List<OnActivityResultListener> getOnActivityResultListeners();

    void addOnActivityResultListener(OnActivityResultListener listener);

    void removeOnActivityResultListener(OnActivityResultListener listener);

    void startActivity(Intent intent);

    void startActivityForResult(Intent intent, int requestCode);

    void onActivityResult(Context context, int requestCode, int resultCode, Intent data);

}

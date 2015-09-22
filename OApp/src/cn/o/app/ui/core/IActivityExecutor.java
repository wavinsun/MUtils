package cn.o.app.ui.core;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import cn.o.app.event.listener.OnActivityResultListener;
import cn.o.app.os.IContextProvider;

public interface IActivityExecutor extends IContextProvider {

	public List<OnActivityResultListener> getOnActivityResultListeners();

	public void addOnActivityResultListener(OnActivityResultListener listener);

	public void removeOnActivityResultListener(OnActivityResultListener listener);

	public void startActivity(Intent intent);

	public void startActivityForResult(Intent intent, int requestCode);

	public void onActivityResult(Context context, int requestCode, int resultCode, Intent data);

}

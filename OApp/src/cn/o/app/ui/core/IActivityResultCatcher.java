package cn.o.app.ui.core;

import java.util.List;

import cn.o.app.context.IContextProvider;
import cn.o.app.event.listener.OnActivityResultListener;

public interface IActivityResultCatcher extends IContextProvider {

	public List<OnActivityResultListener> getOnActivityResultListeners();

	public void addOnActivityResultListener(OnActivityResultListener listener);

	public void removeOnActivityResultListener(OnActivityResultListener listener);

}

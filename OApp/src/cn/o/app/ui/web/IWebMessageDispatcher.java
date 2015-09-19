package cn.o.app.ui.web;

import cn.o.app.context.IContextProvider;
import cn.o.app.core.INoProguard;

public interface IWebMessageDispatcher<MESSAGE> extends IContextProvider, INoProguard {

	public boolean preTranslateMessage();

	public MESSAGE translateMessage();

	public void onMessage(MESSAGE message);

	public void notifyManager(MESSAGE message);

	public IWebMessageManager getManager();

	public void setManager(IWebMessageManager manager);

}

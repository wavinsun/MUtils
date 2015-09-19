package cn.o.app.ui.web;

import cn.o.app.context.IContextProvider;
import cn.o.app.core.IClearable;

public interface IWebMessageManager extends IContextProvider, IClearable {

	public void add(Class<? extends IWebMessageDispatcher<?>> dispatcherClass);

	public void dispatchMessage(String message);

	public void onMessage(String message);

	public IWebFrame getWebFrame();

	public void setWebFrame(IWebFrame webFrame);

}

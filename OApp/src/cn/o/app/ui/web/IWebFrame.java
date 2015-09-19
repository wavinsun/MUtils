package cn.o.app.ui.web;

import cn.o.app.context.IContextProvider;

public interface IWebFrame extends IContextProvider {

	public void sendMessage(String json);

	public void postMessage(String json);

	public void onMessage(String json);

	public void setWebJSInterface(IWebJSInterface webJSInterface);

	public IWebJSInterface getWebJSInterface(IWebJSInterface webJSInterface);

	public IWebMessageManager getWebMessageManager();

	public void setWebMessageManager(IWebMessageManager webMessageManager);

}

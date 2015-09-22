package cn.o.app.ui.web;

import cn.o.app.core.IClearable;
import cn.o.app.os.IContextProvider;

/**
 * Web message manager of framework
 */
public interface IWebMessageManager extends IContextProvider, IClearable {

	/**
	 * Add web message dispatch template to manager
	 * 
	 * @param dispatcherClass
	 */
	public void add(Class<? extends IWebMessageDispatcher<?>> dispatcherClass);

	/**
	 * Dispatch web message
	 * 
	 * @param message
	 */
	public void dispatchMessage(String message);

	/**
	 * Receive dispatched web message result
	 * 
	 * @param message
	 */
	public void onMessage(String message);

	/**
	 * Get WebFrame
	 * 
	 * @return
	 */
	public IWebFrame getWebFrame();

	/**
	 * Set WebFrame
	 * 
	 * @param webFrame
	 */
	public void setWebFrame(IWebFrame webFrame);

}

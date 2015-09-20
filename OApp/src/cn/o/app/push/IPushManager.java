package cn.o.app.push;

import cn.o.app.context.IContextOwner;

/**
 * Push manager of framework
 */
public interface IPushManager extends IContextOwner {

	/**
	 * Add push dispatch template to manager
	 * 
	 * @param dispatcherClass
	 */
	public void add(Class<? extends IPushDispatcher<?>> dispatcherClass);

	/**
	 * Dispatch message
	 * 
	 * @param message
	 */
	public void onMessage(String message);

}

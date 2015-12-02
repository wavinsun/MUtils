package cn.mutils.app.core.event;

import java.util.List;

/**
 * Event dispatcher of framework
 */
public interface IDispatcher {

	/**
	 * Is the listener for default event type
	 * 
	 * @param listener
	 * @return
	 */
	public boolean hasListener(IListener listener);

	/**
	 * Add listener for default event type
	 * 
	 * @param listener
	 */
	public void addListener(IListener listener);

	/**
	 * Remove listener for default event type
	 * 
	 * @param listener
	 */
	public void removeListener(IListener listener);

	/**
	 * Remove all listeners for default event type
	 */
	public void removeAllListeners();

	/**
	 * Is the listener for specify event type
	 * 
	 * @param type
	 * @param listener
	 * @return
	 */
	public boolean hasListener(String type, IListener listener);

	/**
	 * Add listener for specify event type
	 * 
	 * @param type
	 * @param listener
	 */
	public void addListener(String type, IListener listener);

	/**
	 * Remove listener for specify event type
	 * 
	 * @param type
	 * @param listener
	 */
	public void removeListener(String type, IListener listener);

	/**
	 * Remove all listeners for specify event type
	 * 
	 * @param type
	 */
	public void removeAllListeners(String type);

	/**
	 * Get specify type listeners for default event type
	 * 
	 * @param listenerClass
	 * @return
	 */
	public <T extends IListener> List<T> getListeners(Class<T> listenerClass);

	/**
	 * Get specify type listeners for specify event type
	 * 
	 * @param type
	 * @param listenerClass
	 * @return
	 */
	public <T extends IListener> List<T> getListeners(String type, Class<T> listenerClass);

	/**
	 * Get listeners for default event type
	 * 
	 * @return
	 */
	public List<IListener> getListeners();

	/**
	 * Get listeners for specify event type
	 * 
	 * @param type
	 * @return
	 */
	public List<IListener> getListeners(String type);

	/**
	 * Get first listener for default event type
	 * 
	 * @return
	 */
	public IListener getListener();

	/**
	 * Set first listener for default event type
	 * 
	 * @param listener
	 */
	public void setListener(IListener listener);

	/**
	 * Get first listener for specify event type
	 * 
	 * @param type
	 * @return
	 */
	public IListener getListener(String type);

	/**
	 * Set first listener for specify event type
	 * 
	 * @param type
	 * @param listener
	 */
	public void setListener(String type, IListener listener);

	/**
	 * Get first listener for default event type
	 * 
	 * @param listenerClass
	 * @return
	 */
	public <T extends IListener> T getListener(Class<T> listenerClass);

	/**
	 * Get first listener for specify event type
	 * 
	 * @param type
	 * @param listenerClass
	 * @return
	 */
	public <T extends IListener> T getListener(String type, Class<T> listenerClass);

}
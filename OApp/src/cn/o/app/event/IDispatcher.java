package cn.o.app.event;

import java.util.List;

public interface IDispatcher {

	/**
	 * Is the listener for default event type
	 * 
	 * @param listener
	 * @return
	 */
	public boolean hasListener(Listener listener);

	/**
	 * Add listener for default event type
	 * 
	 * @param listener
	 */
	public void addListener(Listener listener);

	/**
	 * Remove listener for default event type
	 * 
	 * @param listener
	 */
	public void removeListener(Listener listener);

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
	public boolean hasListener(String type, Listener listener);

	/**
	 * Add listener for specify event type
	 * 
	 * @param type
	 * @param listener
	 */
	public void addListener(String type, Listener listener);

	/**
	 * Remove listener for specify event type
	 * 
	 * @param type
	 * @param listener
	 */
	public void removeListener(String type, Listener listener);

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
	public <T extends Listener> List<T> getListeners(Class<T> listenerClass);

	/**
	 * Get specify type listeners for specify event type
	 * 
	 * @param type
	 * @param listenerClass
	 * @return
	 */
	public <T extends Listener> List<T> getListeners(String type, Class<T> listenerClass);

	/**
	 * Get listeners for default event type
	 * 
	 * @return
	 */
	public List<Listener> getListeners();

	/**
	 * Get listeners for specify event type
	 * 
	 * @param type
	 * @return
	 */
	public List<Listener> getListeners(String type);

	/**
	 * Get first listener for default event type
	 * 
	 * @return
	 */
	public Listener getListener();

	/**
	 * Set first listener for default event type
	 * 
	 * @param listener
	 */
	public void setListener(Listener listener);

	/**
	 * Get first listener for specify event type
	 * 
	 * @param type
	 * @return
	 */
	public Listener getListener(String type);

	/**
	 * Set first listener for specify event type
	 * 
	 * @param type
	 * @param listener
	 */
	public void setListener(String type, Listener listener);
}
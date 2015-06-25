package cn.o.app.event;

import java.util.List;

public interface IDispatcher {
	// is the listener for default event type
	public boolean hasListener(Listener listener);

	// add listener for default event type
	public void addListener(Listener listener);

	// remove listener for default event type
	public void removeListener(Listener listener);

	// remove all listeners for default event type
	public void removeAllListeners();

	// is the listener for specify event type
	public boolean hasListener(String type, Listener listener);

	// add listener for specify event type
	public void addListener(String type, Listener listener);

	// remove listener for specify event type
	public void removeListener(String type, Listener listener);

	// remove all listeners for specify event type
	public void removeAllListeners(String type);

	// get specify type listeners for default event type
	public <T extends Listener> List<T> getListeners(Class<T> listenerClass);

	// get specify type listeners for specify event type
	public <T extends Listener> List<T> getListeners(String type,
			Class<T> listenerClass);

	// get listeners for default event type
	public List<Listener> getListeners();

	// get listeners for specify event type
	public List<Listener> getListeners(String type);

	// get first listener for default event type
	public Listener getListener();

	// set first listener for default event type
	public void setListener(Listener listener);

	// get first listener for specify event type
	public Listener getListener(String type);

	// set first listener for specify event type
	public void setListener(String type, Listener listener);
}
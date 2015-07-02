package cn.o.app.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@SuppressWarnings("unchecked")
public class Dispatcher implements IDispatcher {

	protected Map<String, List<Listener>> mListenersMap;

	protected Map<String, List<Listener>> allocMap() {
		return new ConcurrentHashMap<String, List<Listener>>();
	}

	protected List<Listener> allocList() {
		return new CopyOnWriteArrayList<Listener>();
	}

	@Override
	public void addListener(Listener listener) {
		addListener("", listener);
	}

	@Override
	public void removeListener(Listener listener) {
		removeListener("", listener);
	}

	@Override
	public void removeAllListeners() {
		removeAllListeners("");
	}

	@Override
	public void addListener(String type, Listener listener) {
		if (listener == null) {
			return;
		}
		if (mListenersMap == null) {
			mListenersMap = allocMap();
		}
		List<Listener> listeners = mListenersMap.get(type);
		if (listeners == null) {
			listeners = allocList();
			mListenersMap.put(type, listeners);
		} else {
			if (listeners.contains(listener)) {
				return;
			}
		}
		listeners.add(listener);
	}

	@Override
	public void removeListener(String type, Listener listener) {
		if (listener == null) {
			return;
		}
		if (mListenersMap == null) {
			return;
		}
		List<Listener> listeners = mListenersMap.get(type);
		if (listeners == null) {
			return;
		}
		listeners.remove(listener);
	}

	@Override
	public void removeAllListeners(String type) {
		if (mListenersMap == null) {
			return;
		}
		List<Listener> listeners = mListenersMap.get(type);
		if (listeners == null) {
			return;
		}
		listeners.clear();
	}

	@Override
	public List<Listener> getListeners() {
		return getListeners("");
	}

	@Override
	public List<Listener> getListeners(String type) {
		if (mListenersMap == null) {
			mListenersMap = allocMap();
		}
		List<Listener> listeners = mListenersMap.get(type);
		if (listeners == null) {
			listeners = allocList();
			mListenersMap.put(type, listeners);
		}
		return listeners;
	}

	@Override
	public Listener getListener() {
		return getListener("");
	}

	@Override
	public void setListener(Listener listener) {
		setListener("", listener);
	}

	@Override
	public Listener getListener(String type) {
		if (mListenersMap == null) {
			return null;
		}
		List<Listener> listeners = mListenersMap.get(type);
		if (listeners == null) {
			return null;
		}
		if (listeners.size() == 0) {
			return null;
		}
		return listeners.get(0);
	}

	@Override
	public void setListener(String type, Listener listener) {
		if (mListenersMap == null) {
			if (listener == null) {
				return;
			}
			mListenersMap = allocMap();
		}
		List<Listener> listeners = mListenersMap.get(type);
		if (listeners == null) {
			if (listener == null) {
				return;
			}
			listeners = allocList();
			mListenersMap.put(type, listeners);
		}
		if (listeners.size() == 0) {
			if (listener != null) {
				listeners.add(listener);
			}
		} else {
			if (listener != null) {
				listeners.set(0, listener);
			} else {
				listeners.remove(0);
			}
		}
	}

	@Override
	public boolean hasListener(Listener listener) {
		return hasListener("", listener);
	}

	@Override
	public boolean hasListener(String type, Listener listener) {
		if (mListenersMap == null) {
			return false;
		}
		List<Listener> listeners = mListenersMap.get(type);
		if (listeners == null) {
			return false;
		}
		if (listeners.contains(listener)) {
			return true;
		}
		return false;
	}

	public <T extends Listener> T getListener(Class<T> listenerClass) {
		if (mListenersMap == null) {
			return null;
		}
		List<Listener> listeners = mListenersMap.get("");
		if (listeners == null) {
			return null;
		}
		if (listeners.size() == 0) {
			return null;
		}
		Listener listener = listeners.get(0);
		return listenerClass.isInstance(listener) ? ((T) listener) : null;
	}

	@Override
	public <T extends Listener> List<T> getListeners(Class<T> listenerClass) {
		return getListeners("", listenerClass);
	}

	@Override
	public <T extends Listener> List<T> getListeners(String type, Class<T> listenerClass) {
		List<T> retListeners = new ArrayList<T>();
		for (Listener listener : getListeners(type)) {
			if (listenerClass.isInstance(listener)) {
				retListeners.add((T) listener);
			}
		}
		return retListeners;
	}

}

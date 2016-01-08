package cn.mutils.app.core.event;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Event dispatcher
 */
@SuppressWarnings({"unchecked", "RedundantIfStatement"})
public class Dispatcher implements IDispatcher {

    protected Map<String, List<IListener>> mListenersMap;

    protected Map<String, List<IListener>> allocMap() {
        return new ConcurrentHashMap<String, List<IListener>>();
    }

    protected List<IListener> allocList() {
        return new CopyOnWriteArrayList<IListener>();
    }

    @Override
    public void addListener(IListener listener) {
        addListener("", listener);
    }

    @Override
    public void removeListener(IListener listener) {
        removeListener("", listener);
    }

    @Override
    public void removeAllListeners() {
        removeAllListeners("");
    }

    @Override
    public void addListener(String type, IListener listener) {
        if (listener == null) {
            return;
        }
        if (mListenersMap == null) {
            mListenersMap = allocMap();
        }
        List<IListener> listeners = mListenersMap.get(type);
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
    public void removeListener(String type, IListener listener) {
        if (listener == null) {
            return;
        }
        if (mListenersMap == null) {
            return;
        }
        List<IListener> listeners = mListenersMap.get(type);
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
        List<IListener> listeners = mListenersMap.get(type);
        if (listeners == null) {
            return;
        }
        listeners.clear();
    }

    @Override
    public List<IListener> getListeners() {
        return getListeners("");
    }

    @Override
    public List<IListener> getListeners(String type) {
        if (mListenersMap == null) {
            mListenersMap = allocMap();
        }
        List<IListener> listeners = mListenersMap.get(type);
        if (listeners == null) {
            listeners = allocList();
            mListenersMap.put(type, listeners);
        }
        return listeners;
    }

    @Override
    public IListener getListener() {
        return getListener("");
    }

    @Override
    public void setListener(IListener listener) {
        setListener("", listener);
    }

    @Override
    public IListener getListener(String type) {
        if (mListenersMap == null) {
            return null;
        }
        List<IListener> listeners = mListenersMap.get(type);
        if (listeners == null) {
            return null;
        }
        if (listeners.size() == 0) {
            return null;
        }
        return listeners.get(0);
    }

    @Override
    public void setListener(String type, IListener listener) {
        if (mListenersMap == null) {
            if (listener == null) {
                return;
            }
            mListenersMap = allocMap();
        }
        List<IListener> listeners = mListenersMap.get(type);
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
    public boolean hasListener(IListener listener) {
        return hasListener("", listener);
    }

    @Override
    public boolean hasListener(String type, IListener listener) {
        if (mListenersMap == null) {
            return false;
        }
        List<IListener> listeners = mListenersMap.get(type);
        if (listeners == null) {
            return false;
        }
        if (listeners.contains(listener)) {
            return true;
        }
        return false;
    }

    @Override
    public <T extends IListener> T getListener(Class<T> listenerClass) {
        return getListener("", listenerClass);
    }

    @Override
    public <T extends IListener> List<T> getListeners(Class<T> listenerClass) {
        return getListeners("", listenerClass);
    }

    @Override
    public <T extends IListener> List<T> getListeners(String type, Class<T> listenerClass) {
        List<T> retListeners = new ArrayList<T>();
        for (IListener listener : getListeners(type)) {
            if (listenerClass.isInstance(listener)) {
                retListeners.add((T) listener);
            }
        }
        return retListeners;
    }

    @Override
    public <T extends IListener> T getListener(String type, Class<T> listenerClass) {
        if (mListenersMap == null) {
            return null;
        }
        List<IListener> listeners = mListenersMap.get(type);
        if (listeners == null) {
            return null;
        }
        if (listeners.size() == 0) {
            return null;
        }
        IListener listener = listeners.get(0);
        return listenerClass.isInstance(listener) ? ((T) listener) : null;
    }

}

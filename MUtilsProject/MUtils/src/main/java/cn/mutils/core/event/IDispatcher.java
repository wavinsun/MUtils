package cn.mutils.core.event;

import java.util.List;

/**
 * Event dispatcher of framework
 */
@SuppressWarnings("unused")
public interface IDispatcher {

    /**
     * Is the listener for default event type
     */
    boolean hasListener(IListener listener);

    /**
     * Add listener for default event type
     */
    void addListener(IListener listener);

    /**
     * Remove listener for default event type
     */
    void removeListener(IListener listener);

    /**
     * Remove all listeners for default event type
     */
    void removeAllListeners();

    /**
     * Is the listener for specify event type
     */
    boolean hasListener(String type, IListener listener);

    /**
     * Add listener for specify event type
     */
    void addListener(String type, IListener listener);

    /**
     * Remove listener for specify event type
     */
    void removeListener(String type, IListener listener);

    /**
     * Remove all listeners for specify event type
     */
    void removeAllListeners(String type);

    /**
     * Get specify type listeners for default event type
     */
    <T extends IListener> List<T> getListeners(Class<T> listenerClass);

    /**
     * Get specify type listeners for specify event type
     */
    <T extends IListener> List<T> getListeners(String type, Class<T> listenerClass);

    /**
     * Get listeners for default event type
     */
    List<IListener> getListeners();

    /**
     * Get listeners for specify event type
     */
    List<IListener> getListeners(String type);

    /**
     * Get first listener for default event type
     */
    IListener getListener();

    /**
     * Set first listener for default event type
     */
    void setListener(IListener listener);

    /**
     * Get first listener for specify event type
     */
    IListener getListener(String type);

    /**
     * Set first listener for specify event type
     */
    void setListener(String type, IListener listener);

    /**
     * Get first listener for default event type
     */
    <T extends IListener> T getListener(Class<T> listenerClass);

    /**
     * Get first listener for specify event type
     */
    <T extends IListener> T getListener(String type, Class<T> listenerClass);

}
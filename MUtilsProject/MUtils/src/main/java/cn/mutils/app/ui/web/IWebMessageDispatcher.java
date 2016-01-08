package cn.mutils.app.ui.web;

import cn.mutils.app.core.INoProguard;
import cn.mutils.app.os.IContextProvider;

/**
 * Web message dispatcher of framework
 *
 * @param <MESSAGE>
 */
public interface IWebMessageDispatcher<MESSAGE> extends IContextProvider, INoProguard {

    /**
     * Whether to intercept translate message
     *
     * @return Return true to intercept dispatching
     */
    boolean preTranslateMessage();

    /**
     * Translate dispatcher to MESSAGE
     */
    MESSAGE translateMessage();

    /**
     * Dispatch message
     */
    void onMessage(MESSAGE message);

    /**
     * Give result of dispatched message to web message manager
     */
    void notifyManager(MESSAGE message);

    /**
     * Get web message manager
     */
    IWebMessageManager getManager();

    /**
     * Set web message manager
     */
    void setManager(IWebMessageManager manager);

}

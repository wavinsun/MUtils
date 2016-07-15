package cn.mutils.app.ui.web;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;
import proguard.annotation.KeepImplementations;

import cn.mutils.app.os.IContextProvider;

/**
 * Web message dispatcher of framework
 *
 * @param <MESSAGE>
 */
@Keep
@KeepClassMembers
@KeepImplementations
public interface IWebMessageDispatcher<MESSAGE> extends IContextProvider {

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

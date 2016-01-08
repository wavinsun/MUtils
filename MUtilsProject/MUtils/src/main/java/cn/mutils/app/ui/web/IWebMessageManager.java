package cn.mutils.app.ui.web;

import cn.mutils.app.core.IClearable;
import cn.mutils.app.os.IContextProvider;

/**
 * Web message manager of framework
 */
public interface IWebMessageManager extends IContextProvider, IClearable {

    /**
     * Add web message dispatch template to manager
     */
    void add(Class<? extends IWebMessageDispatcher<?>> dispatcherClass);

    /**
     * Dispatch web message
     */
    void dispatchMessage(String message);

    /**
     * Receive dispatched web message result
     */
    void onMessage(String message);

    /**
     * Get WebFrame
     */
    IWebFrame getWebFrame();

    /**
     * Set WebFrame
     */
    void setWebFrame(IWebFrame webFrame);

}

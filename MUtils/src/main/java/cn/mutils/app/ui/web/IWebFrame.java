package cn.mutils.app.ui.web;

import android.webkit.WebView;

import cn.mutils.app.os.IContextProvider;

/**
 * WebFrame of framework
 */
@SuppressWarnings("unused")
public interface IWebFrame extends IContextProvider {

    /**
     * Get WebView component
     */
    WebView getWebView();

    /**
     * Send message what created by WebApp to NativeApp
     */
    void sendMessage(String json);

    /**
     * Post message what created by WebApp to NativeApp
     */
    void postMessage(String json);

    /**
     * Send message what handled by NativeApp to WebApp
     */
    void onMessage(String json);

    /**
     * Set web JavaScript call interface
     */
    void setWebJSInterface(IWebJSInterface webJSInterface);

    /**
     * Get web JavaScript call interface
     */
    IWebJSInterface getWebJSInterface(IWebJSInterface webJSInterface);

    /**
     * Get web message manager
     */
    IWebMessageManager getWebMessageManager();

    /**
     * Set web message manager
     */
    void setWebMessageManager(IWebMessageManager webMessageManager);

}

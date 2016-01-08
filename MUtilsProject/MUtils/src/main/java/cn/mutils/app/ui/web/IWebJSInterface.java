package cn.mutils.app.ui.web;

import android.webkit.JavascriptInterface;

import cn.mutils.app.core.INoProguard;

/**
 * Web JavaScript call interface of framework
 */
public interface IWebJSInterface extends INoProguard {

    /**
     * Get WebFrame
     */
    IWebFrame getWebFrame();

    /**
     * Set WebFrame
     */
    void setWebFrame(IWebFrame webFrame);

    /**
     * JavaScript node name what is injected into window object of WebView<br>
     * By default:window.app
     */
    String name();

    /**
     * JavaScript function injected into object of window[{{@link #name()}}] to
     * send message to native application<br>
     * By default:window.app.sendMessage(jsonString)
     */
    @JavascriptInterface
    void sendMessage(String json);

    /**
     * JavaScript function injected into object of window[{@link #name()}}] to
     * post message to native application<br>
     * By default:window.app.postMessage(jsonString)
     */
    @JavascriptInterface
    void postMessage(String json);

    /**
     * JavaScript function name what is provided to send message to web
     * application<br>
     * By default:window.app.onMessage(jsonString)
     */
    String onMessageName();

}

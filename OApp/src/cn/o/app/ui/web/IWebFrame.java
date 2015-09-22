package cn.o.app.ui.web;

import android.webkit.WebView;
import cn.o.app.os.IContextProvider;

/**
 * WebFrame of framework
 */
public interface IWebFrame extends IContextProvider {

	/**
	 * Get WebView component
	 * 
	 * @return
	 */
	public WebView getWebView();

	/**
	 * Send message what created by WebApp to NativeApp
	 * 
	 * @param json
	 */
	public void sendMessage(String json);

	/**
	 * Post message what created by WebApp to NativeApp
	 * 
	 * @param json
	 */
	public void postMessage(String json);

	/**
	 * Send message what handled by NativeApp to WebApp
	 * 
	 * @param json
	 */
	public void onMessage(String json);

	/**
	 * Set web JavaScript call interface
	 * 
	 * @param webJSInterface
	 */
	public void setWebJSInterface(IWebJSInterface webJSInterface);

	/**
	 * Get web JavaScript call interface
	 * 
	 * @param webJSInterface
	 * @return
	 */
	public IWebJSInterface getWebJSInterface(IWebJSInterface webJSInterface);

	/**
	 * Get web message manager
	 * 
	 * @return
	 */
	public IWebMessageManager getWebMessageManager();

	/**
	 * Set web message manager
	 * 
	 * @param webMessageManager
	 */
	public void setWebMessageManager(IWebMessageManager webMessageManager);

}

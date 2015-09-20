package cn.o.app.ui.web;

import android.webkit.JavascriptInterface;
import cn.o.app.core.INoProguard;

/**
 * Web JavaScript call interface of framework
 */
public interface IWebJSInterface extends INoProguard {

	/**
	 * Get WebFrame
	 * 
	 * @return
	 */
	public IWebFrame getWebFrame();

	/**
	 * Set WebFrame
	 * 
	 * @param webFrame
	 */
	public void setWebFrame(IWebFrame webFrame);

	/**
	 * JavaSctipt node name what is injected into window object of WebView<br>
	 * By default:window.app
	 * 
	 * @return
	 */
	public String name();

	/**
	 * JavaScript function injected into object of window[{{@link #name()}}] to
	 * send message to native application<br>
	 * By default:window.app.sendMessage(jsonString)
	 * 
	 * @param json
	 */
	@JavascriptInterface
	public void sendMessage(String json);

	/**
	 * JavaScript function injected into object of window[{@link #name()}}] to
	 * post message to native application<br>
	 * By default:windw.app.postMessage(jsonString)
	 * 
	 * @param json
	 */
	@JavascriptInterface
	public void postMessage(String json);

	/**
	 * JavaScript function name what is provided to send message to web
	 * application<br>
	 * By default:window.app.onMessage(jsonString)
	 * 
	 * @return
	 */
	public String onMessage();

}

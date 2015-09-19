package cn.o.app.ui.web;

import android.webkit.JavascriptInterface;
import cn.o.app.core.INoProguard;

public interface IWebJSInterface extends INoProguard {

	public IWebFrame getWebFrame();

	public void setWebFrame(IWebFrame webFrame);

	public String name();

	@JavascriptInterface
	public void sendMessage(String json);

	@JavascriptInterface
	public void postMessage(String json);

	public String onMessage();

}

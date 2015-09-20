package cn.o.app.ui.web;

import android.webkit.WebChromeClient;
import cn.o.app.core.log.Logs;

/**
 * WebFrame chrome client of framework
 */
public class WebFrameChromeClient extends WebChromeClient {

	@Override
	public void onConsoleMessage(String message, int lineNumber, String sourceID) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(sourceID);
		sb.append("][L:");
		sb.append(lineNumber);
		sb.append("] ");
		sb.append(message);
		Logs.i("WebChromeClient", sb.toString());
	}

}

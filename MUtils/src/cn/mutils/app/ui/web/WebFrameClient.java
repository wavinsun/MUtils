package cn.mutils.app.ui.web;

import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * WebFrame WebView client of framework
 */
public class WebFrameClient extends WebViewClient {

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		return false;
	}

}

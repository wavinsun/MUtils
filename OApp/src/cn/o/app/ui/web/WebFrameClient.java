package cn.o.app.ui.web;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebFrameClient extends WebViewClient {

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		view.loadUrl(url);
		return false;
	}

}

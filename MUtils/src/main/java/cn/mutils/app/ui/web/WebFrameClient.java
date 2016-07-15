package cn.mutils.app.ui.web;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import cn.mutils.app.util.AppUtil;

/**
 * WebFrame WebView client of framework
 */
public class WebFrameClient extends WebViewClient {

	@Override
	public boolean shouldOverrideUrlLoading(WebView view, String url) {
		if (!AppUtil.isEmpty(url)) {
			int pIndex = url.indexOf("://");
			if (pIndex >= 0) {
				String p = url.substring(0, pIndex);
				if (!(p.equalsIgnoreCase("file") || p.equalsIgnoreCase("http") || p.equalsIgnoreCase("https")
						|| p.equalsIgnoreCase("ftp"))) {
					if (p.equalsIgnoreCase("intent")) {
						try {
							Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);
							if (intent != null) {
								Context context = view.getContext();
								ResolveInfo info = context.getPackageManager().resolveActivity(intent,
										PackageManager.MATCH_DEFAULT_ONLY);
								if (info != null) {
									context.startActivity(intent);
									return true;
								}
							}
						} catch (Exception e) {
							// URISyntaxException
						}
					}
					return true;
				}
			}
		}
		view.loadUrl(url);
		return false;
	}

}

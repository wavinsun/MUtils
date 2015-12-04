package cn.mutils.app.ui.web;

import android.content.Intent;
import android.net.Uri;
import android.webkit.DownloadListener;
import cn.mutils.app.os.ContextOwner;

public class WebFrameDownloadListener extends ContextOwner implements DownloadListener {

	@Override
	public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype,
			long contentLength) {
		try {
			mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
		} catch (Exception e) {

		}
	}

}

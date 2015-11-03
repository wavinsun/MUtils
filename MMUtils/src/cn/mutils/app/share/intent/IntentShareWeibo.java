package cn.mutils.app.share.intent;

import android.content.Context;
import android.content.Intent;
import cn.mutils.app.share.ShareBase;

public class IntentShareWeibo extends ShareBase {

	public IntentShareWeibo(Context context) {
		setContext(context);
	}

	@Override
	public int getPlatform() {
		return PLATFORM_WEIBO;
	}

	@Override
	public int getMethod() {
		return METHOD_INTENT;
	}

	@Override
	public void share() {
		String text = mText;
		if (mUrl != null) {
			text += "\n" + mUrl;
		}
		Intent intent = new Intent();
		intent.setAction(Intent.ACTION_SEND);
		intent.setType("text/plain");
		intent.setPackage(PACKAGE_WEIBO);
		intent.putExtra(Intent.EXTRA_TITLE, mTitle);
		intent.putExtra(Intent.EXTRA_SUBJECT, mTitle);
		intent.putExtra(Intent.EXTRA_TEXT, text);
		mContext.startActivity(intent);
	}

}

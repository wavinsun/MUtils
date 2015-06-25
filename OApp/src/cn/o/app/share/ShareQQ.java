package cn.o.app.share;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

public class ShareQQ extends ShareBase {

	protected static String sAppId;

	public ShareQQ(Context context) {
		setContext(context);
	}

	@Override
	public void share() {
		if (sAppId == null) {
			if (mListener != null) {
				mListener.onError(this);
			}
		}
		Bundle bundle = new Bundle();
		bundle.putString(QQShare.SHARE_TO_QQ_TITLE, mTitle);
		bundle.putString(QQShare.SHARE_TO_QQ_SUMMARY, mText);
		bundle.putString(QQShare.SHARE_TO_QQ_TARGET_URL, mUrl);
		bundle.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, mImageUrl);
		Tencent.createInstance(sAppId, mContext).shareToQQ((Activity) mContext,
				bundle, new IUiListener() {

					@Override
					public void onError(UiError arg0) {
						if (mListener != null) {
							mListener.onError(ShareQQ.this);
						}
					}

					@Override
					public void onComplete(Object arg0) {
						if (mListener != null) {
							mListener.onComplete(ShareQQ.this);
						}
					}

					@Override
					public void onCancel() {
						if (mListener != null) {
							mListener.onCancel(ShareQQ.this);
						}
					}
				});
	}

	@Override
	public int getPlatform() {
		return IShare.PLATFORM_QQ;
	}

	public static void setAppId(String appId) {
		sAppId = appId;
	}

	public static String getAppId() {
		return sAppId;
	}

}

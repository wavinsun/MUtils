package cn.o.app.share;

import com.tencent.connect.share.QQShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import android.content.Context;
import android.os.Bundle;
import cn.o.app.AppUtil;

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
		Tencent.createInstance(sAppId, mContext).shareToQQ(AppUtil.toActivity(mContext), bundle, new IUiListener() {

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
		return PLATFORM_QQ;
	}

	@Override
	public int getMethod() {
		return METHOD_API;
	}

	public static void setAppId(String appId) {
		sAppId = appId;
	}

	public static String getAppId() {
		return sAppId;
	}

}

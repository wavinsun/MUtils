package cn.o.app.share;

import android.content.Context;
import cn.o.app.share.intent.IntentShareQzone;
import cn.o.app.share.intent.IntentShareTencentWeibo;
import cn.o.app.share.intent.IntentShareWeibo;

public class Share extends ShareBase {

	public Share(Context context) {
		setContext(context);
		mMethod = METHOD_INTENT;
	}

	@Override
	public void share() {
		IShare wrapper = null;
		switch (mPlatform) {
		case PLATFORM_QQ:
			wrapper = mMethod == METHOD_INTENT ? null : new ShareQQ(mContext);
			break;
		case PLATFORM_QZONE:
			wrapper = mMethod == METHOD_INTENT ? new IntentShareQzone(mContext) : new ShareQzone(mContext);
			break;
		case PLATFORM_WEIBO:
			wrapper = mMethod == METHOD_INTENT ? new IntentShareWeibo(mContext) : new ShareWeibo(mContext);
			break;
		case PLATFORM_WECHAT:
			wrapper = mMethod == METHOD_INTENT ? null : new ShareWechat(mContext);
			break;
		case PLATFORM_WECHAT_MOMENTS:
			wrapper = mMethod == METHOD_INTENT ? null : new ShareWechatMoments(mContext);
			break;
		case PLATFORM_TENCENT_WEIBO:
			wrapper = mMethod == METHOD_INTENT ? new IntentShareTencentWeibo(mContext) : null;
			break;
		default:
			break;
		}
		if (wrapper == null) {
			if (mListener != null) {
				mListener.onError(this);
			}
			return;
		}
		wrapper.setTitle(mTitle);
		wrapper.setText(mText);
		wrapper.setImageUrl(mImageUrl);
		wrapper.setUrl(mUrl);
		wrapper.setListener(mListener);
		wrapper.share();
	}

	public static void setTencentAppId(String appId) {
		ShareQQ.setAppId(appId);
		ShareQzone.setAppId(appId);
	}

	public static String getTencentAppId() {
		return ShareQQ.getAppId();
	}

	public static void setWechatAppId(String appId) {
		ShareWechat.setAppId(appId);
	}

	public static String getAppId() {
		return ShareWechat.getAppId();
	}

	public static void setWeiboAppId(String appId) {
		ShareWeibo.setAppId(appId);
	}

	public static String getWeiboAppId() {
		return ShareWeibo.getAppId();
	}

}

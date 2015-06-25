package cn.o.app.share;

import android.content.Context;

public class Share extends ShareBase {

	public Share(Context context) {
		setContext(context);
	}

	@Override
	public void share() {
		IShare wrapper = null;
		switch (mPlatform) {
		case PLATFORM_QQ:
			wrapper = new ShareQQ(mContext);
			break;
		case PLATFORM_QZONE:
			wrapper = new ShareQzone(mContext);
			break;
		case PLATFORM_WEIBO:
			wrapper = new ShareWeibo(mContext);
			break;
		case PLATFORM_WECHAT:
			wrapper = new ShareWechat(mContext);
			break;
		case PLATFORM_WECHAT_MOMENTS:
			wrapper = new ShareWechatMoments(mContext);
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

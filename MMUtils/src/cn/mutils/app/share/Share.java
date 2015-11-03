package cn.mutils.app.share;

import android.content.Context;
import cn.mutils.app.share.intent.IntentShareQzone;
import cn.mutils.app.share.intent.IntentShareTencentWeibo;
import cn.mutils.app.share.intent.IntentShareWeibo;
import cn.mutils.app.share.mob.MobShareTencentWeibo;
import cn.mutils.app.share.mob.MobShareWeibo;

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
			switch (mMethod) {
			case METHOD_API:
				wrapper = new ShareQQ(mContext);
				break;
			case METHOD_INTENT:
				wrapper = null;
				break;
			case METHOD_SHARE_SDK:
				wrapper = null;
				break;
			default:
				break;
			}
			break;
		case PLATFORM_QZONE:
			switch (mMethod) {
			case METHOD_API:
				wrapper = new ShareQzone(mContext);
				break;
			case METHOD_INTENT:
				wrapper = new IntentShareQzone(mContext);
				break;
			case METHOD_SHARE_SDK:
				wrapper = null;
				break;
			default:
				break;
			}
			break;
		case PLATFORM_WEIBO:
			switch (mMethod) {
			case METHOD_API:
				wrapper = null;
				break;
			case METHOD_INTENT:
				wrapper = new IntentShareWeibo(mContext);
				break;
			case METHOD_SHARE_SDK:
				wrapper = new MobShareWeibo(mContext);
				break;
			default:
				break;
			}
			break;
		case PLATFORM_WECHAT:
			switch (mMethod) {
			case METHOD_API:
				wrapper = new ShareWechat(mContext);
				break;
			case METHOD_INTENT:
				wrapper = null;
				break;
			case METHOD_SHARE_SDK:
				wrapper = null;
				break;
			default:
				break;
			}
			break;
		case PLATFORM_WECHAT_MOMENTS:
			switch (mMethod) {
			case METHOD_API:
				break;
			case METHOD_INTENT:
				break;
			case METHOD_SHARE_SDK:
				break;
			default:
				break;
			}
			wrapper = mMethod == METHOD_INTENT ? null : new ShareWechatMoments(mContext);
			break;
		case PLATFORM_TENCENT_WEIBO:
			switch (mMethod) {
			case METHOD_API:
				wrapper = null;
				break;
			case METHOD_INTENT:
				wrapper = new IntentShareTencentWeibo(mContext);
				break;
			case METHOD_SHARE_SDK:
				wrapper = new MobShareTencentWeibo(mContext);
				break;
			default:
				break;
			}
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

	public static String getWechatAppId() {
		return ShareWechat.getAppId();
	}

}

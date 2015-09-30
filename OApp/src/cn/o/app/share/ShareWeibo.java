package cn.o.app.share;

import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.utils.Utility;

import android.content.Context;
import cn.o.app.AppUtil;

public class ShareWeibo extends ShareBase {

	protected static String sAppId;

	public ShareWeibo(Context context) {
		setContext(context);
	}

	@Override
	public void share() {
		if (sAppId == null) {
			if (mListener != null) {
				mListener.onError(this);
			}
		}
		IWeiboShareAPI api = WeiboShareSDK.createWeiboAPI(mContext, sAppId, false);
		if (!api.checkEnvironment(true)) {
			if (mListener != null) {
				mListener.onError(this);
			}
		}
		if (!api.registerApp()) {
			if (mListener != null) {
				mListener.onError(this);
			}
		}
		TextObject text = new TextObject();
		text.text = mTitle + "\n" + mText + "\n";
		WebpageObject web = new WebpageObject();
		web.identify = Utility.generateGUID();
		web.title = mTitle;
		web.description = mText;
		web.actionUrl = mUrl;
		web.defaultText = "Webpage 默认文案";
		web.thumbData = AppUtil.toByteArray(AppUtil.getAppIcon(mContext));
		WeiboMultiMessage msg = new WeiboMultiMessage();
		msg.textObject = text;
		msg.mediaObject = web;
		SendMultiMessageToWeiboRequest req = new SendMultiMessageToWeiboRequest();
		req.transaction = String.valueOf(System.currentTimeMillis());
		req.multiMessage = msg;
		api.sendRequest(req);
	}

	@Override
	public int getPlatform() {
		return PLATFORM_WEIBO;
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

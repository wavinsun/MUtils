package cn.mutils.app.share;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.content.Context;
import cn.mutils.app.util.AppUtil;

public class ShareWechat extends ShareBase {

	public static final String TRANSACTION = "webpage";

	protected static String sAppId;

	public ShareWechat(Context context) {
		setContext(context);
	}

	@Override
	public void share() {
		if (sAppId == null) {
			if (mListener != null) {
				mListener.onError(this);
			}
		}
		IWXAPI api = WXAPIFactory.createWXAPI(mContext, sAppId, false);
		if (!api.isWXAppInstalled()) {
			if (mListener != null) {
				mListener.onError(this);
			}
			return;
		}
		WXWebpageObject web = new WXWebpageObject();
		web.webpageUrl = mUrl;
		WXMediaMessage msg = new WXMediaMessage(web);
		msg.title = mTitle;
		msg.description = mText;
		msg.thumbData = AppUtil.toByteArray(AppUtil.getAppIcon(mContext));
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = TRANSACTION + System.currentTimeMillis();
		req.message = msg;
		req.scene = getScene();
		api.sendReq(req);
	}

	protected int getScene() {
		return SendMessageToWX.Req.WXSceneSession;
	}

	@Override
	public int getPlatform() {
		return PLATFORM_WECHAT;
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

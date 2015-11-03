package cn.mutils.app.share;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

import android.content.Context;

public class ShareWechatMoments extends ShareWechat {

	public ShareWechatMoments(Context context) {
		super(context);
	}

	@Override
	public int getPlatform() {
		return PLATFORM_WECHAT_MOMENTS;
	}

	@Override
	protected int getScene() {
		return SendMessageToWX.Req.WXSceneTimeline;
	}

}

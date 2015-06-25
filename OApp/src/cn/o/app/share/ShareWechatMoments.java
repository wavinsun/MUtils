package cn.o.app.share;

import android.content.Context;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

public class ShareWechatMoments extends ShareWechat {

	public ShareWechatMoments(Context context) {
		super(context);
	}

	@Override
	protected int getScene() {
		return SendMessageToWX.Req.WXSceneTimeline;
	}

}

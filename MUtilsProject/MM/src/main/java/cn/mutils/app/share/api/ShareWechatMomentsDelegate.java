package cn.mutils.app.share.api;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

public class ShareWechatMomentsDelegate extends ShareWechatDelegate {

    @Override
    public int getPlatform() {
        return PLATFORM_WECHAT_MOMENTS;
    }

    @Override
    protected int getScene() {
        return SendMessageToWX.Req.WXSceneTimeline;
    }

}

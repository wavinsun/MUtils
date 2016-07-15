package cn.mutils.app.mm.impl;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;

import cn.mutils.app.mm.IShareWechatMoments;

public class ShareWechatMomentsImpl extends ShareWechatImpl implements IShareWechatMoments {

    @Override
    public int getPlatform() {
        return PLATFORM_WECHAT_MOMENTS;
    }

    @Override
    protected int getScene() {
        return SendMessageToWX.Req.WXSceneTimeline;
    }

}

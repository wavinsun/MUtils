package cn.mutils.app.mm.impl;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.mutils.app.App;
import cn.mutils.app.mm.IShareWechat;
import cn.mutils.app.share.ShareBase;
import cn.mutils.app.util.AppUtil;

public class ShareWechatImpl extends ShareBase implements IShareWechat {

    public static final String TRANSACTION = "webpage";

    @Override
    public void share() {
        if (App.getWechatAppId() == null) {
            if (mListener != null) {
                mListener.onError(this);
            }
        }
        IWXAPI api = WXAPIFactory.createWXAPI(mContext, App.getWechatAppId(), false);
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

}

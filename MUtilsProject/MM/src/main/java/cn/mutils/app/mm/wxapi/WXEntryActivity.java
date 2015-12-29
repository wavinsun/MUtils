package cn.mutils.app.mm.wxapi;

import android.os.Bundle;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.mutils.app.App;
import cn.mutils.app.share.api.ShareWechatDelegate;
import cn.mutils.app.ui.AppActivity;

public class WXEntryActivity extends AppActivity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (App.getWechatAppId() != null) {
            api = WXAPIFactory.createWXAPI(this, App.getWechatAppId(), false);
            api.handleIntent(getIntent(), this);
        }
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if (resp instanceof SendAuth.Resp) {
                    // 第三方登录
                }
                if (resp.transaction.contains(ShareWechatDelegate.TRANSACTION)) {
                    // 分享回调
                }
                break;
        }
        finish();
    }
}

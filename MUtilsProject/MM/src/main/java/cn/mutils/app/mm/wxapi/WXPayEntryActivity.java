package cn.mutils.app.mm.wxapi;

import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import cn.mutils.app.App;
import cn.mutils.app.pay.WXPayBroadcast;
import cn.mutils.app.ui.AppActivity;

public class WXPayEntryActivity extends AppActivity implements IWXAPIEventHandler {

    protected IWXAPI mApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApi = WXAPIFactory.createWXAPI(this, App.getWechatAppId());
        mApi.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        mApi.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case 0://成功
                    break;
                case -1://失败
                    break;
                case -2://取消
                    break;
            }
            WXPayBroadcast.WXPayExtra extra = new WXPayBroadcast.WXPayExtra();
            extra.errCode = resp.errCode;
            extra.errStr = resp.errStr;
            WXPayBroadcast broadcast = new WXPayBroadcast(this);
            broadcast.send(extra);
        }
        finish();
    }

}

package cn.o.app.wxapi;

import android.os.Bundle;
import cn.o.app.share.Share;
import cn.o.app.share.ShareWechat;
import cn.o.app.ui.OActivity;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class WXEntryActivity extends OActivity implements IWXAPIEventHandler {

	private IWXAPI api;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (ShareWechat.getAppId() != null) {
			api = WXAPIFactory.createWXAPI(this, Share.getWeiboAppId(), false);
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
			if (resp.transaction.contains(ShareWechat.TRANSACTION)) {
				// 分享回调
			}
			break;
		}
		finish();
	}
}

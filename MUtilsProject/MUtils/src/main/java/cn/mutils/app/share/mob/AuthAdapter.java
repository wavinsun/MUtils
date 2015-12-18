package cn.mutils.app.share.mob;

import cn.sharesdk.framework.authorize.AuthorizeAdapter;

public class AuthAdapter extends AuthorizeAdapter {

	@Override
	public void onCreate() {
		super.onCreate();
		this.hideShareSDKLogo();
	}

}

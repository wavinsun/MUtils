package cn.o.app.share.mob;

import java.util.HashMap;

import android.content.Context;
import cn.o.app.share.ShareBase;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.tencent.weibo.TencentWeibo.ShareParams;

public class MobShareTencentWeibo extends ShareBase {

	public MobShareTencentWeibo(Context context) {
		super.setContext(context);
	}

	@Override
	public int getPlatform() {
		return PLATFORM_TENCENT_WEIBO;
	}

	@Override
	public int getMethod() {
		return METHOD_SHARE_SDK;
	}

	@Override
	public void share() {
		String text = mText;
		if (mUrl != null) {
			text += "\n" + mUrl;
		}
		ShareParams params = new ShareParams();
		params.setTitle(mTitle);
		params.setTitleUrl(mUrl);
		params.setText(text);
		params.setImageUrl(mImageUrl);
		params.setSite(mTitle);
		params.setSiteUrl(mUrl);
		Platform platform = ShareSDK.getPlatform(TencentWeibo.NAME);
		if (platform.isValid()) {
			platform.removeAccount(true);
		}
		platform.setPlatformActionListener(new PlatformActionListener() {

			@Override
			public void onError(Platform platform, int action, Throwable t) {
				if (mListener != null) {
					mListener.onError(MobShareTencentWeibo.this);
				}
			}

			@Override
			public void onComplete(Platform platform, int action, HashMap<String, Object> result) {
				if (action == Platform.ACTION_SHARE) {
					if (mListener != null) {
						mListener.onComplete(MobShareTencentWeibo.this);
					}
				}
			}

			@Override
			public void onCancel(Platform platform, int action) {
				if (mListener != null) {
					mListener.onCancel(MobShareTencentWeibo.this);
				}
			}

		});
		platform.share(params);
	}

}

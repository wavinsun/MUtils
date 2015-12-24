package cn.mutils.app.share.mob;

import java.util.HashMap;

import cn.mutils.app.share.ShareBase;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.tencent.weibo.TencentWeibo.ShareParams;

public class MobShareTencentWeiboDelegate extends ShareBase {

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
        Platform platform = ShareSDK.getPlatform(TencentWeibo.NAME);
        platform.setPlatformActionListener(new PlatformActionListener() {

            @Override
            public void onError(Platform platform, int action, Throwable t) {
                if (mListener != null) {
                    mListener.onError(MobShareTencentWeiboDelegate.this);
                }
            }

            @Override
            public void onComplete(Platform platform, int action, HashMap<String, Object> result) {
                if (action == Platform.ACTION_AUTHORIZING) {
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
                    platform.share(params);
                } else if (action == Platform.ACTION_SHARE) {
                    if (mListener != null) {
                        mListener.onComplete(MobShareTencentWeiboDelegate.this);
                    }
                }
            }

            @Override
            public void onCancel(Platform platform, int action) {
                if (mListener != null) {
                    mListener.onCancel(MobShareTencentWeiboDelegate.this);
                }
            }

        });
        platform.authorize();
    }

}

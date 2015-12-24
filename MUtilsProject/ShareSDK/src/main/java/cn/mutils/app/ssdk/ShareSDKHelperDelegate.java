package cn.mutils.app.ssdk;


import android.content.Context;

import cn.mutils.app.util.AppUtil;
import cn.mutils.app.util.ShareSDKHelper;
import cn.sharesdk.framework.ShareSDK;

public class ShareSDKHelperDelegate extends ShareSDKHelper {

    @Override
    public boolean isShareSDKEnabled(Context context) {
        return AppUtil.isAssetExists(context, "ShareSDK.xml");
    }

    @Override
    public void initShareSDK(Context context) {
        ShareSDK.initSDK(context);
    }

}

package cn.mutils.app.ssdk.impl;

import android.content.Context;

import cn.mutils.app.ssdk.IShareSDKHelper;
import cn.mutils.app.util.AppUtil;
import cn.sharesdk.framework.ShareSDK;

public class ShareSDKHelperImpl implements IShareSDKHelper {

    @Override
    public boolean isShareSDKEnabled(Context context) {
        return AppUtil.isAssetExists(context, "ShareSDK.xml");
    }

    @Override
    public void initShareSDK(Context context) {
        ShareSDK.initSDK(context);
    }

}

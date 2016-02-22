package cn.mutils.app.util;

import android.content.Context;

import cn.mutils.core.runtime.Delegate;

public class ShareSDKHelper extends Delegate<ShareSDKHelper> {

    public static final String CLASS_DELEGATE = "cn.mutils.app.ssdk.ShareSDKHelperDelegate";

    @Override
    public String classDelegate() {
        return CLASS_DELEGATE;
    }

    public boolean isShareSDKEnabled(Context context) {
        return false;
    }

    public void initShareSDK(Context context) {

    }

}

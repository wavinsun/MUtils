package cn.mutils.app.share.api;

import android.content.Context;

import cn.mutils.app.share.ShareBase;

public class ShareWechat extends ShareBase {

    public static final String CLASS_DELEGATE = "cn.mutils.app.share.api.ShareWechatDelegate";

    public ShareWechat(Context context) {
        super();
        delegate().setContext(context);
    }

    @Override
    public String classDelegate() {
        return CLASS_DELEGATE;
    }

    @Override
    public void share() {

    }

}

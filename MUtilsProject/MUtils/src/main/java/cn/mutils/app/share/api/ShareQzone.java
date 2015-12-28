package cn.mutils.app.share.api;

import android.content.Context;

import cn.mutils.app.share.ShareBase;

public class ShareQzone extends ShareBase {

    public static final String CLASS_DELEGATE = "cn.mutils.app.share.api.ShareQzoneDelegate";

    public ShareQzone(Context context) {
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

package cn.mutils.app.share.api;

import android.content.Context;

import cn.mutils.app.share.ShareBase;

public class ShareQQ extends ShareBase {

    public static final String CLASS_DELEGATE = "cn.mutils.app.share.api.ShareQQDelegate";

    public ShareQQ(Context context) {
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

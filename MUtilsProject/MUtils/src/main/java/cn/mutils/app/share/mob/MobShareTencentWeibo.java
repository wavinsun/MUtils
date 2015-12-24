package cn.mutils.app.share.mob;


import android.content.Context;

import cn.mutils.app.share.ShareBase;

public class MobShareTencentWeibo extends ShareBase {

    public static final String CLASS_DELEGATE = "cn.mutils.app.share.mob.MobShareTencentWeiboDelegate";

    public MobShareTencentWeibo(Context context) {
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
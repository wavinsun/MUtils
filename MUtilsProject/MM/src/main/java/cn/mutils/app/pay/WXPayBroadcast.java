package cn.mutils.app.pay;

import android.content.Context;

import cn.mutils.app.io.AppBroadcast;
import cn.mutils.app.io.Extra;

/**
 * WeChat pay broadcast to notify pay result
 */
public class WXPayBroadcast extends AppBroadcast<WXPayBroadcast.WXPayExtra> {

    public static class WXPayExtra extends Extra {
        public int errCode;
        public String errStr;
    }

    public static final String ACTION = "cn.mutils.app.pay.WXPayExtra";

    public WXPayBroadcast(Context context) {
        super(context);
        setAction(ACTION);
    }

}

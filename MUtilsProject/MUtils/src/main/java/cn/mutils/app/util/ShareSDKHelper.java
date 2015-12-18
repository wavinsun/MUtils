package cn.mutils.app.util;

import android.content.Context;
import cn.sharesdk.framework.ShareSDK;

public class ShareSDKHelper {

	public static boolean isShareSDKEnabled(Context context) {
		return AppUtil.isAssetExists(context, "ShareSDK.xml");
	}

	public static void initShareSDK(Context context) {
		ShareSDK.initSDK(context);
	}

}

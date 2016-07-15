package cn.mutils.app.ssdk;

import android.content.Context;

import cn.mutils.core.runtime.IService;

public interface IShareSDKHelper extends IService {

    boolean isShareSDKEnabled(Context context);

    void initShareSDK(Context context);

}

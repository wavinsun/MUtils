package cn.mutils.app.settings;

import android.content.Context;

import cn.mutils.core.IClearable;
import cn.mutils.core.INoProguard;

public interface IAssetItem extends INoProguard, IClearable {

    boolean getFromAsset(Context context);

}

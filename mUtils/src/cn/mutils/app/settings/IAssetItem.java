package cn.mutils.app.settings;

import android.content.Context;
import cn.mutils.app.core.IClearable;
import cn.mutils.app.core.INoProguard;

public interface IAssetItem extends INoProguard, IClearable {

	public boolean getFromAsset(Context context);

}

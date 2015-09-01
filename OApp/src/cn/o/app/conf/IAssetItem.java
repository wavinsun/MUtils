package cn.o.app.conf;

import android.content.Context;
import cn.o.app.core.IClearable;
import cn.o.app.core.INoProguard;

public interface IAssetItem extends INoProguard, IClearable {

	public boolean getFromAsset(Context context);

}

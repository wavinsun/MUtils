package cn.o.app.conf;

import android.content.Context;
import cn.o.app.core.io.INoProguard;
import cn.o.app.core.runtime.IClearable;

public interface IAssetItem extends INoProguard, IClearable {

	public boolean getFromAsset(Context context);

}

package cn.o.app.conf;

import cn.o.app.io.ICacheOwner;
import cn.o.app.io.INoProguard;
import android.content.Context;

public interface IAssetItem extends INoProguard, ICacheOwner {

	public boolean getFromAsset(Context context);

}

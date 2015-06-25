package cn.o.app.conf;

import cn.o.app.io.ICacheOwner;
import cn.o.app.io.INoProguard;
import android.content.Context;

public interface IPrefItem extends INoProguard, ICacheOwner {

	public boolean getFromPref(Context context);

	public boolean putToPref(Context context);

}

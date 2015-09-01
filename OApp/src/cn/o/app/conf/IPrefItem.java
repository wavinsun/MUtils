package cn.o.app.conf;

import android.content.Context;
import cn.o.app.core.IClearable;
import cn.o.app.core.INoProguard;

public interface IPrefItem extends INoProguard, IClearable {

	public boolean getFromPref(Context context);

	public boolean putToPref(Context context);

}

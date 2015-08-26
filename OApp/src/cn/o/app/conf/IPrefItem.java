package cn.o.app.conf;

import android.content.Context;
import cn.o.app.core.io.INoProguard;
import cn.o.app.core.runtime.IClearable;

public interface IPrefItem extends INoProguard, IClearable {

	public boolean getFromPref(Context context);

	public boolean putToPref(Context context);

}

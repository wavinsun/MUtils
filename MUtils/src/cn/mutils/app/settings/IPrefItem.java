package cn.mutils.app.settings;

import android.content.Context;
import cn.mutils.app.core.IClearable;
import cn.mutils.app.core.INoProguard;

public interface IPrefItem extends INoProguard, IClearable {

	public boolean getFromPref(Context context);

	public boolean putToPref(Context context);

}

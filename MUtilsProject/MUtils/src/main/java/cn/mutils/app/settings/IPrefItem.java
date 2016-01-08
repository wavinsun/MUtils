package cn.mutils.app.settings;

import android.content.Context;

import cn.mutils.app.core.IClearable;
import cn.mutils.app.core.INoProguard;

public interface IPrefItem extends INoProguard, IClearable {

    boolean getFromPref(Context context);

    boolean putToPref(Context context);

}

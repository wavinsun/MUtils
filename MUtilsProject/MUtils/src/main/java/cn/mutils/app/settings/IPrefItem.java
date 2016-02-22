package cn.mutils.app.settings;

import android.content.Context;

import cn.mutils.core.IClearable;
import cn.mutils.core.INoProguard;

public interface IPrefItem extends INoProguard, IClearable {

    boolean getFromPref(Context context);

    boolean putToPref(Context context);

}

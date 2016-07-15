package cn.mutils.app.settings;

import android.content.Context;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;
import proguard.annotation.KeepImplementations;

import cn.mutils.core.IClearable;

@Keep
@KeepClassMembers
@KeepImplementations
public interface IPrefItem extends IClearable {

    boolean getFromPref(Context context);

    boolean putToPref(Context context);

}

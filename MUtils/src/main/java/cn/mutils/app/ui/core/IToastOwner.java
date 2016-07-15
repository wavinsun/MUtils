package cn.mutils.app.ui.core;

import android.widget.Toast;

import cn.mutils.app.os.IContextProvider;
import cn.mutils.app.ui.InfoToast;

/**
 * Toast of framework
 */
public interface IToastOwner extends IContextProvider {

    InfoToast getInfoToast();

    Toast getToast();

    IToastOwner getToastOwner();

    void toast(CharSequence s);

    void toast(int resId, Object... args);

}

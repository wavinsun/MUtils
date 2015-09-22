package cn.o.app.ui.core;

import android.widget.Toast;
import cn.o.app.os.IContextProvider;
import cn.o.app.ui.InfoToast;

/**
 * Toast of framework
 */
public interface IToastOwner extends IContextProvider {

	public InfoToast getInfoToast();

	public Toast getToast();

	public IToastOwner getToastOwner();

	public void toast(CharSequence s);

	public void toast(int resId, Object... args);

}

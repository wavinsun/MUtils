package cn.o.app.ui.core;

import cn.o.app.context.IContextProvider;
import cn.o.app.ui.OToast;

public interface IToastOwner extends IContextProvider {

	public OToast getToast();

	public IToastOwner getToastOwner();

	public void toast(CharSequence s);

	public void toast(int resId, Object... args);

}

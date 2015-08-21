package cn.o.app.ui.core;

import android.view.Window;
import cn.o.app.context.IContextProvider;

public interface IWindowProvider extends IContextProvider {

	public Window getWindow();

}

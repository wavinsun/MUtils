package cn.mutils.app.ui.core;

import android.view.Window;
import cn.mutils.app.os.IContextProvider;

public interface IWindowProvider extends IContextProvider {

	public Window getWindow();

}

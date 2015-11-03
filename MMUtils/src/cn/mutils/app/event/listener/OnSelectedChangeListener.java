package cn.mutils.app.event.listener;

import android.view.View;
import cn.mutils.app.core.event.Listener;
import cn.mutils.app.ui.StateViewFrame;
import cn.mutils.app.ui.TabBar;

/**
 * Listener for selected index change of UI container
 * 
 * @see StateViewFrame#setOnSelectedChangeListener(OnSelectedChangeListener)
 * @see TabBar#setOnSelectedChangeListener(OnSelectedChangeListener)
 */
public abstract class OnSelectedChangeListener implements Listener {

	/**
	 * Override this method if you want to intercept selected state change
	 * 
	 * @param v
	 * @param index
	 * @return Return true to intercept
	 */
	public boolean onInterceptChange(View v, int index) {
		return false;
	}

	public abstract void onChanged(View v, int index);

}

package cn.o.app.event.listener;

import android.view.View;
import cn.o.app.event.Listener;

public abstract class OnSelectedChangeListener implements Listener {

	// return true to intercept
	public boolean onInterceptChange(View v, int index) {
		return false;
	}

	public abstract void onChanged(View v, int index);

}

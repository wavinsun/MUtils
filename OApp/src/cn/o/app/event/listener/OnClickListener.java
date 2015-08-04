package cn.o.app.event.listener;

import java.lang.reflect.Method;

import android.view.View;
import cn.o.app.LogCat;
import cn.o.app.runtime.ReflectUtil;

/**
 * IOC for @OnClick
 */
public class OnClickListener implements View.OnClickListener {

	/** Callback */
	protected Method mCallBack;

	/** Proxy of onClick */
	protected Object mProxy;

	public void setCallBack(Method m) {
		mCallBack = m;
	}

	public void setProxy(Object proxy) {
		mProxy = proxy;
	}

	@Override
	public void onClick(View v) {
		if (v.getVisibility() != View.VISIBLE) {
			return;
		}
		if (!mCallBack.isAccessible()) {
			mCallBack.setAccessible(true);
		}
		Class<?>[] paramTypes = mCallBack.getParameterTypes();
		boolean invoked = false;
		if (paramTypes.length == 0) {
			ReflectUtil.invoke(mProxy, mCallBack);
			invoked = true;
		} else if (paramTypes.length == 1) {
			if (View.class.isAssignableFrom(paramTypes[0])) {
				ReflectUtil.invoke(mProxy, mCallBack, v);
				invoked = true;
			}
		}
		if (!invoked) {
			LogCat.e("OnClick Invalid: " + v);
		}
	}
}

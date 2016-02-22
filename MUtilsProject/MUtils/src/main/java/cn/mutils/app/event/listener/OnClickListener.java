package cn.mutils.app.event.listener;

import android.view.View;

import java.lang.reflect.Method;

import cn.mutils.core.log.Logs;
import cn.mutils.core.reflect.ReflectUtil;

/**
 * IOC for @Click
 */
public class OnClickListener implements View.OnClickListener {

    /**
     * Callback
     */
    protected Method mCallBack;

    /**
     * Proxy of onClick
     */
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
            Logs.e(mCallBack.getDeclaringClass().getSimpleName() + "." + mCallBack.getName(), "Click Invalid: " + v);
        }
    }
}

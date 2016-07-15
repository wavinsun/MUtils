package cn.mutils.app.event.listener;

import android.view.View;

import cn.mutils.app.ui.StateViewFrame;
import cn.mutils.app.ui.TabBar;
import cn.mutils.core.event.IListener;

/**
 * Listener for selected index change of UI container
 *
 * @see StateViewFrame#setOnSelectedChangeListener(OnSelectedChangeListener)
 * @see TabBar#setOnSelectedChangeListener(OnSelectedChangeListener)
 */
@SuppressWarnings("UnusedParameters")
public abstract class OnSelectedChangeListener implements IListener {

    /**
     * Override this method if you want to intercept try again for that {
     * {@link #onInterceptChange(View, int)} return false.
     *
     * @param v     View
     * @param index Index
     * @return Return true to intercept try again
     */
    public boolean onInterceptTryAgain(View v, int index) {
        return true;
    }

    /**
     * Override this method if you want to intercept selected state change
     *
     * @param v     View
     * @param index Index
     * @return Return true to intercept
     */
    public boolean onInterceptChange(View v, int index) {
        return false;
    }

    public abstract void onChanged(View v, int index);

}

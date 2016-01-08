package cn.mutils.app.event.listener;

import android.view.View;

import cn.mutils.app.core.event.IListener;

/**
 * Input change listener of framework
 */
public abstract class OnInputChangeListener implements IListener {

    /**
     * It will be called back while input type is integer for your component
     */
    public void onChanged(View v, int value) {

    }

    /**
     * It will be called back while input type is double for your component
     */
    public void onChanged(View v, double value) {

    }

    /**
     * It will be called back while input type is string for your component
     */
    public abstract void onChanged(View v, String value);

}

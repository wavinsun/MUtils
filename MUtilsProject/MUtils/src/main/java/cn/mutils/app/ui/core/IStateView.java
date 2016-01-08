package cn.mutils.app.ui.core;

import android.app.Activity;

import cn.mutils.app.ui.AppActivity;
import cn.mutils.app.ui.StateView;
import cn.mutils.app.ui.StateViewFrame;

/**
 * State view of framework who has life cycle as {@link Activity}
 */
public interface IStateView extends IViewFinder, IActivityExecutor {

    IStateViewManager getManager();

    void setManager(IStateViewManager manager);

    /**
     * State onCreate
     *
     * @see Activity#onCreate(android.os.Bundle)
     */
    void onCreate();

    /**
     * State onStart
     *
     * @see Activity#onStart()
     */
    void onStart();

    /**
     * State onResume
     *
     * @see Activity#onResume()
     */
    void onResume();

    /**
     * State onPause
     *
     * @see Activity#onPause()
     */
    void onPause();

    /**
     * State onStop
     *
     * @see Activity#onStop()
     */
    void onStop();

    /**
     * State onDestory
     *
     * @see Activity#onDestroy()
     */
    void onDestroy();

    /**
     * Intercept back press
     *
     * @see AppActivity#onInterceptBackPressed()
     * @see StateView#onInterceptBackPressed()
     * @see StateViewFrame#onInterceptBackPressed()
     */
    boolean onInterceptBackPressed();

    boolean isCreateDispatched();

    void setCreateDispatched(boolean dispatched);

}

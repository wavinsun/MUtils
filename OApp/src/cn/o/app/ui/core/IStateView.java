package cn.o.app.ui.core;

import android.app.Activity;
import cn.o.app.ui.OActivity;
import cn.o.app.ui.StateView;
import cn.o.app.ui.StateViewFrame;

/**
 * State view of framework who has life cycle as {@link Activity}
 */
public interface IStateView extends IViewFinder, IActivityExecutor {

	public IStateViewManager getManager();

	public void setManager(IStateViewManager manager);

	/**
	 * State onCreate
	 * 
	 * @see Activity#onCreate(android.os.Bundle)
	 */
	public void onCreate();

	/**
	 * State onStart
	 * 
	 * @see Activity#onStart()
	 */
	public void onStart();

	/**
	 * State onResume
	 * 
	 * @see Activity#onResume()
	 */
	public void onResume();

	/**
	 * State onPause
	 * 
	 * @see Activity#onPause()
	 */
	public void onPause();

	/**
	 * State onStop
	 * 
	 * @see Activity#onStop()
	 */
	public void onStop();

	/**
	 * State onDestory
	 * 
	 * @see Activity#onDestroy()
	 */
	public void onDestroy();

	/**
	 * Intercept back press
	 * 
	 * @see OActivity#onInterceptBackPressed()
	 * @see StateView#onInterceptBackPressed()
	 * @see StateViewFrame#onInterceptBackPressed()
	 * 
	 * @return
	 */
	public boolean onInterceptBackPressed();

	public boolean isCreateDispatched();

	public void setCreateDispatched(boolean dispatched);

}

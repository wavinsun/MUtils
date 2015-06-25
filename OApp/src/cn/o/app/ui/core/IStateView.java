package cn.o.app.ui.core;


public interface IStateView extends IViewFinder, IActivityStarter {

	public IStateViewManager getManager();

	public void setManager(IStateViewManager manager);

	public void onCreate();

	public void onStart();

	public void onResume();

	public void onPause();

	public void onStop();

	public void onDestroy();

	public boolean onInterceptBackPressed();

	public boolean isCreateDispatched();

	public void setCreateDispatched(boolean dispatched);
}

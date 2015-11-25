package cn.mutils.app.ui.core;

import cn.mutils.app.core.task.IStopableManager;
import cn.mutils.app.data.IAsyncDataQueueOwner;
import cn.mutils.app.net.INetQueueOwner;
import cn.mutils.app.os.IHandlerProvider;
import cn.mutils.app.ui.pattern.IPatternOwner;

public interface IActivity
		extends IFragmenterManager, INetQueueOwner, IPatternOwner, IAsyncDataQueueOwner, IToastOwner, IStopableManager,
		IActivityExecutor, IContentViewOwner, IWindowProvider, IStatusBarOwner, IRunOnceHolder, IHandlerProvider {

	public boolean isBusy();

	public void setBusy(boolean busy);

	public boolean isRunning();

	public boolean isFinished();

}

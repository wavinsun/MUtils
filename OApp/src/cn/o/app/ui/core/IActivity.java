package cn.o.app.ui.core;

import cn.o.app.core.task.IStopableManager;
import cn.o.app.data.IAsyncDataQueueOwner;
import cn.o.app.net.INetQueueOwner;
import cn.o.app.ui.pattern.IPatternOwner;

public interface IActivity extends IFragmentManager, INetQueueOwner, IPatternOwner, IAsyncDataQueueOwner, IToastOwner,
		IStopableManager, IActivityExecutor, IContentViewOwner, IWindowProvider {

	public boolean isBusy();

	public void setBusy(boolean busy);

	public boolean isRunning();

	public boolean isFinished();

}

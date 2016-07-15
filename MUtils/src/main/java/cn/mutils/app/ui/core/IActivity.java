package cn.mutils.app.ui.core;

import cn.mutils.app.data.IAsyncDataQueueOwner;
import cn.mutils.app.net.INetQueueOwner;
import cn.mutils.app.os.IHandlerProvider;
import cn.mutils.core.task.IStoppableManager;

@SuppressWarnings("unused")
public interface IActivity
        extends IFragmenterManager, INetQueueOwner, IAsyncDataQueueOwner, IToastOwner, IStoppableManager,
        IActivityExecutor, IContentViewOwner, IWindowProvider, IStatusBarOwner, IRunOnceHolder, IHandlerProvider {

    boolean isBusy();

    void setBusy(boolean busy);

    boolean isRunning();

    boolean isFinished();

}

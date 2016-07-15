package cn.mutils.app.queue;

import cn.mutils.app.os.IContextOwner;
import cn.mutils.core.IClearable;
import cn.mutils.core.event.IDispatcher;

@SuppressWarnings("unused")
public interface IQueue extends IClearable, IContextOwner, IDispatcher {

    boolean isRunInBackground();

    int getMaxRunningCount();

    void setMaxRunningCount(int maxRunningCount);

    void add(IQueueItem<?> task);

}

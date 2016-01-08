package cn.mutils.app.queue;

import cn.mutils.app.core.IClearable;
import cn.mutils.app.core.event.IDispatcher;
import cn.mutils.app.os.IContextOwner;

public interface IQueue extends IClearable, IContextOwner, IDispatcher {

    boolean isRunInBackground();

    int getMaxRunningCount();

    void setMaxRunningCount(int maxRunningCount);

    void add(IQueueItem<?> task);

}

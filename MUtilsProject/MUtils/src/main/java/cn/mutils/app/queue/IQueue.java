package cn.mutils.app.queue;

import cn.mutils.app.core.IClearable;
import cn.mutils.app.core.event.IDispatcher;
import cn.mutils.app.os.IContextOwner;

public interface IQueue extends IClearable, IContextOwner, IDispatcher {

	public boolean isRunInBackground();

	public int getMaxRunningCount();

	public void setMaxRunningCount(int maxRunningCount);

	public void add(IQueueItem<?> task);

}

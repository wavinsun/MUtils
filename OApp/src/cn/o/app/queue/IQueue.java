package cn.o.app.queue;

import cn.o.app.context.IContextOwner;
import cn.o.app.core.event.IDispatcher;

public interface IQueue extends IContextOwner, IDispatcher {

	public boolean isRunInBackground();

	public int getMaxRunningCount();

	public void setMaxRunningCount(int maxRunningCount);

	public void add(IQueueItem<?> task);

	public void clear();

}

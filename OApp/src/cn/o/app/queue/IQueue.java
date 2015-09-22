package cn.o.app.queue;

import cn.o.app.core.event.IDispatcher;
import cn.o.app.os.IContextOwner;

public interface IQueue extends IContextOwner, IDispatcher {

	public boolean isRunInBackground();

	public int getMaxRunningCount();

	public void setMaxRunningCount(int maxRunningCount);

	public void add(IQueueItem<?> task);

	public void clear();

}

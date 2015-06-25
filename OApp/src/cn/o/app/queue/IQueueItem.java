package cn.o.app.queue;

import cn.o.app.context.IContextOwner;
import cn.o.app.event.IDispatcher;
import cn.o.app.task.IStopable;

public interface IQueueItem<QUEUE_ITEM extends IQueueItem<QUEUE_ITEM>> extends
		IContextOwner, IStopable, IDispatcher {
	public boolean isStarted();

	public boolean start();
}

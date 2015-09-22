package cn.o.app.queue;

import cn.o.app.core.event.IDispatcher;
import cn.o.app.core.task.IStopable;
import cn.o.app.os.IContextOwner;

public interface IQueueItem<QUEUE_ITEM extends IQueueItem<QUEUE_ITEM>> extends IContextOwner, IStopable, IDispatcher {

	public boolean isStarted();

	public boolean start();

}

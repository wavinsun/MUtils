package cn.mutils.app.queue;

import cn.mutils.app.os.IContextOwner;
import cn.mutils.core.event.IDispatcher;
import cn.mutils.core.task.IStoppable;

@SuppressWarnings("unused")
public interface IQueueItem<QUEUE_ITEM extends IQueueItem<QUEUE_ITEM>> extends IContextOwner, IStoppable, IDispatcher {

    boolean isStarted();

    boolean start();

}

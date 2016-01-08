package cn.mutils.app.queue;

import cn.mutils.app.core.event.IDispatcher;
import cn.mutils.app.core.task.IStoppable;
import cn.mutils.app.os.IContextOwner;

public interface IQueueItem<QUEUE_ITEM extends IQueueItem<QUEUE_ITEM>> extends IContextOwner, IStoppable, IDispatcher {

    boolean isStarted();

    boolean start();

}

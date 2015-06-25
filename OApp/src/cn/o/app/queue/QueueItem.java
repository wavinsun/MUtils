package cn.o.app.queue;

import cn.o.app.task.ContextOwnerTask;

@SuppressWarnings("unchecked")
public class QueueItem<QUEUE_ITEM extends IQueueItem<QUEUE_ITEM>> extends
		ContextOwnerTask implements IQueueItem<QUEUE_ITEM> {

	@Override
	protected void onStop() {
		for (IQueueItemListener<QUEUE_ITEM> listener : getListeners(IQueueItemListener.class)) {
			listener.onStop((QUEUE_ITEM) this);
		}
	}

	@Override
	protected void onStart() {
		for (IQueueItemListener<QUEUE_ITEM> listener : getListeners(IQueueItemListener.class)) {
			listener.onStart((QUEUE_ITEM) this);
		}
	}

}

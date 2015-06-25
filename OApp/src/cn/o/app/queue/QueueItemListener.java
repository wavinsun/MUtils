package cn.o.app.queue;

public abstract class QueueItemListener<QUEUE_ITEM extends IQueueItem<QUEUE_ITEM>>
		implements IQueueItemListener<QUEUE_ITEM> {

	@Override
	public void onStart(QUEUE_ITEM task) {

	}

	@Override
	public void onStop(QUEUE_ITEM task) {

	}

}

package cn.o.app.queue;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.o.app.event.ContextOwnerDispathcer;

@SuppressWarnings("rawtypes")
public class Queue extends ContextOwnerDispathcer implements IQueue, IQueueItemListener {

	protected boolean mRunInBackground;

	protected int mMaxRunningCount;

	protected List<IQueueItem<?>> mQueue;

	protected List<IQueueItem<?>> mQueueToBe;

	public Queue() {
		mRunInBackground = true;
		mMaxRunningCount = 1;
		mQueue = new LinkedList<IQueueItem<?>>();
		mQueueToBe = new LinkedList<IQueueItem<?>>();
	}

	protected void updateRunState() {
		boolean runningBackground = isRunningBackground();
		if (runningBackground != mRunInBackground) {
			mRunInBackground = runningBackground;
			for (IQueueListener listener : getListeners(IQueueListener.class)) {
				listener.onRunStateChanged(this);
			}
		}
	}

	@Override
	public boolean isRunInBackground() {
		return mRunInBackground;
	}

	protected boolean isRunningBackground() {
		for (IQueueItem<?> task : mQueue) {
			if (!task.isRunInBackground()) {
				return false;
			}
		}
		for (IQueueItem<?> task : mQueueToBe) {
			if (!task.isRunInBackground()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public int getMaxRunningCount() {
		return mMaxRunningCount;
	}

	@Override
	public void setMaxRunningCount(int maxRunningCount) {
		mMaxRunningCount = maxRunningCount;
	}

	@Override
	public void add(IQueueItem<?> task) {
		mQueueToBe.add(task);
		startOneItem();
	}

	protected void startOneItem() {
		if (mQueue.size() >= mMaxRunningCount) {
			return;
		}
		if (mQueueToBe.size() == 0) {
			return;
		}
		IQueueItem<?> task = mQueueToBe.remove(0);
		mQueue.add(task);
		task.addListener(this);
		task.setContext(mContext);
		task.start();
	}

	@Override
	public void clear() {
		List<IQueueItem<?>> queueCopy = new ArrayList<IQueueItem<?>>();
		queueCopy.addAll(mQueue);
		List<IQueueItem<?>> queueToBeCopy = new ArrayList<IQueueItem<?>>();
		queueToBeCopy.addAll(mQueueToBe);
		mQueue.clear();
		mQueueToBe.clear();
		for (IQueueItem<?> task : queueCopy) {
			task.stop();
		}
		for (IQueueItem<?> task : queueToBeCopy) {
			task.stop();
		}
	}

	@Override
	public void onException(IQueueItem task, Exception e) {

	}

	@Override
	public void onStart(IQueueItem task) {
		if (!mQueue.contains(task)) {
			return;
		}
		onItemStart(task);
		updateRunState();
	}

	@Override
	public void onStop(IQueueItem task) {
		boolean isInQueueToBe = false;
		boolean isInQueue = mQueue.contains(task);
		if (!isInQueue) {
			isInQueueToBe = mQueueToBe.contains(task);
		}
		if (!isInQueue && !isInQueueToBe) {
			return;
		}
		if (isInQueue) {
			mQueue.remove(task);
		} else {
			mQueueToBe.remove(task);
		}
		onItemStop(task);
		startOneItem();
		updateRunState();
	}

	protected void onItemStart(IQueueItem<?> task) {

	}

	protected void onItemStop(IQueueItem<?> task) {

	}

}

package cn.mutils.app.queue;

import java.util.LinkedList;
import java.util.List;

import cn.mutils.app.event.ContextOwnerDispathcer;

@SuppressWarnings("rawtypes")
public class Queue extends ContextOwnerDispathcer implements IQueue, IQueueItemListener {

	protected boolean mRunInBackground = true;

	protected int mMaxRunningCount = 1;

	protected List<IQueueItem<?>> mQueue;

	protected List<IQueueItem<?>> mQueueToBe;

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
		if (mQueue == null || mQueueToBe == null) {
			return true;
		}
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
		if (task.isStoped()) {
			return;
		}
		synchronized (this) {
			if (mQueueToBe == null) {
				mQueueToBe = new LinkedList<IQueueItem<?>>();
			}
		}
		mQueueToBe.add(task);
		task.addListener(this);
		startOneItem();
	}

	protected void startOneItem() {
		if (mQueue != null && mQueue.size() >= mMaxRunningCount) {
			return;
		}
		if (mQueueToBe != null && mQueueToBe.size() == 0) {
			return;
		}
		IQueueItem<?> task = mQueueToBe.remove(0);
		synchronized (this) {
			if (mQueue == null) {
				mQueue = new LinkedList<IQueueItem<?>>();
			}
		}
		mQueue.add(task);
		task.setContext(mContext);
		task.start();
	}

	@Override
	public void clear() {
		if (mQueue != null) {
			for (IQueueItem<?> task : mQueue) {
				task.removeListener(this);
				task.stop();
			}
			mQueue.clear();
		}
		if (mQueueToBe != null) {
			for (IQueueItem<?> task : mQueueToBe) {
				task.removeListener(this);
				task.stop();
			}
			mQueueToBe.clear();
		}
	}

	@Override
	public void onException(IQueueItem task, Exception e) {

	}

	@Override
	public void onStart(IQueueItem task) {
		if (mQueue == null) {
			return;
		}
		if (!mQueue.contains(task)) {
			return;
		}
		onItemStart(task);
		updateRunState();
	}

	@Override
	public void onStop(IQueueItem task) {
		boolean isInQueueToBe = false;
		boolean isInQueue = mQueue != null && mQueue.contains(task);
		if (!isInQueue) {
			isInQueueToBe = mQueueToBe != null && mQueueToBe.contains(task);
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

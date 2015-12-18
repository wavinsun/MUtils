package cn.mutils.app.core.task;

import cn.mutils.app.core.event.IListener;

public class RepeatTask extends Task {

	public static interface IRepeatTaskListener extends IListener {

		public void onStart(RepeatTask task);

		public void onStop(RepeatTask task);

		/**
		 * Prepare repeat
		 * 
		 * @param task
		 * @return return true to intercept repeat
		 */
		public boolean onPreRepeat(RepeatTask task);

		public abstract void onRepeat(RepeatTask task);

	}

	public static abstract class RepeatTaskListener implements IRepeatTaskListener {

		public void onStart(RepeatTask task) {

		}

		public void onStop(RepeatTask task) {

		}

		public boolean onPreRepeat(RepeatTask task) {
			return false;
		}

	}

	protected String mName = "";

	protected int mCount = 1;

	public String getName() {
		return mName;
	}

	public void setName(String name) {
		if (mStarted || mStoped) {
			return;
		}
		mName = name;
	}

	public void setCount(int count) {
		if (mStarted || mStoped) {
			return;
		}
		mCount = count;
	}

	public void addListener(RepeatTaskListener listener) {
		super.addListener(listener);
	}

	public void repeat() {
		if (!isRunning()) {
			return;
		}
		if (mCount <= 0) {
			stop();
			return;
		}
		if (onPreRepeat()) {// Intercept repeat
			stop();
			return;
		}
		mCount--;
		onRepeat();
	}

	protected boolean onPreRepeat() {
		for (IRepeatTaskListener listener : getListeners(IRepeatTaskListener.class)) {
			if (listener.onPreRepeat(this)) {
				return true;
			}
		}
		return false;
	}

	protected void onRepeat() {
		for (IRepeatTaskListener listener : getListeners(IRepeatTaskListener.class)) {
			listener.onRepeat(this);
		}
	}

	@Override
	protected void onStart() {
		for (IRepeatTaskListener listener : getListeners(IRepeatTaskListener.class)) {
			listener.onStart(this);
		}
		repeat();
	}

	@Override
	protected void onStop() {
		for (IRepeatTaskListener listener : getListeners(IRepeatTaskListener.class)) {
			listener.onStop(this);
		}
	}

}

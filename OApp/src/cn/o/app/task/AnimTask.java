package cn.o.app.task;

import android.os.Handler;
import cn.o.app.event.Listener;

public class AnimTask extends Task implements ILockable {

	public static abstract class AnimTaskListener implements Listener {

		public abstract void onUpdate(AnimTask task, double progress);

		public void onComplete(AnimTask task) {

		}

		public void onStart(AnimTask task) {

		}

		public void onStop(AnimTask task) {

		}

	}

	protected long mStep = 0;
	protected long mSteps = 40;
	protected long mStepMillis = 30;

	protected Handler mHandler = new Handler();
	protected Runnable mRunnable = new AnimRunnable();

	@Override
	public boolean isLocked() {
		return true;
	}

	@Override
	public void setLocked(boolean locked) {

	}

	public double getProgress() {
		if (mSteps == 0) {
			return 0;
		}
		double value = mStep;
		value = value / mSteps;
		return value < 1 ? value : 1;
	}

	public long getSteps() {
		return mSteps;
	}

	public void setSteps(long steps) {
		if (mStarted) {
			return;
		}
		mSteps = steps > 0 ? steps : 1;
	}

	public long getStep() {
		return mStep;
	}

	public long getStepMillis() {
		return mStepMillis;
	}

	public void setStepMillis(long stepMillis) {
		if (mStarted) {
			return;
		}
		mStepMillis = stepMillis > 0 ? stepMillis : 1;
	}

	@Override
	public void setRunInBackground(boolean runInBackground) {

	}

	@Override
	protected void onStart() {
		mStep = 0;
		for (AnimTaskListener listener : getListeners(AnimTaskListener.class)) {
			listener.onStart(this);
		}
		mHandler.post(mRunnable);
	}

	@Override
	protected void onStop() {
		mHandler.removeCallbacksAndMessages(null);
		for (AnimTaskListener listener : getListeners(AnimTaskListener.class)) {
			listener.onStop(this);
		}
	}

	public void addListener(AnimTaskListener listener) {
		super.addListener(listener);
	}

	@Override
	public boolean start() {
		if (isRunning()) {
			stop();
		}
		mStarted = true;
		mStoped = false;
		onStart();
		return true;
	}

	class AnimRunnable implements Runnable {

		@Override
		public void run() {
			double progress = getProgress();
			for (AnimTaskListener listener : getListeners(AnimTaskListener.class)) {
				if (mStep < mSteps) {
					listener.onUpdate(AnimTask.this, progress);
				} else if (mStep == mSteps) {
					listener.onUpdate(AnimTask.this, progress);
					listener.onComplete(AnimTask.this);
				}
			}
			if (mStep < mSteps) {
				mStep++;
				mHandler.postDelayed(mRunnable, mStepMillis);
			} else {
				stop();
			}
		}

	}

}

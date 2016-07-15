package cn.mutils.app.task;

import android.os.Handler;
import android.os.Looper;

import cn.mutils.core.task.IStoppable;

public class DelayTask implements IStoppable {

    protected boolean mStopped;

    protected Handler mHandler;

    protected Runnable mRunnable;

    protected Runnable mRunnableWrapper;

    protected long mDelay;

    public DelayTask(Runnable runnable, long delay) {
        mHandler = new Handler(Looper.getMainLooper());
        mRunnable = runnable;
        mRunnableWrapper = new DelayRunnable();
        mDelay = delay;
        mStopped = true;
    }

    @Override
    public boolean isRunInBackground() {
        return true;
    }

    @Override
    public void setRunInBackground(boolean runInBackground) {

    }

    @Override
    public boolean isStopped() {
        return mStopped;
    }

    @Override
    public boolean stop() {
        if (mStopped) {
            return false;
        }
        mHandler.removeCallbacksAndMessages(null);
        mStopped = true;
        return true;
    }

    /**
     * You can call start more than once
     */
    public DelayTask start() {
        stop();
        mHandler.postDelayed(mRunnableWrapper, mDelay);
        mStopped = false;
        return this;
    }

    public DelayTask start(long delay) {
        mDelay = delay;
        return start();
    }

    class DelayRunnable implements Runnable {

        @Override
        public void run() {
            mStopped = true;
            mRunnable.run();
        }

    }

}

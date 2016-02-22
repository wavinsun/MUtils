package cn.mutils.core.task;

import cn.mutils.core.event.Dispatcher;

public class Task extends Dispatcher implements IStoppable {

    protected boolean mStarted;

    protected boolean mStopped;

    protected boolean mRunInBackground = true;

    protected boolean mRestartable = false;

    @Override
    public boolean isRunInBackground() {
        return mRunInBackground;
    }

    @Override
    public void setRunInBackground(boolean runInBackground) {
        if (mStarted || mStopped) {
            return;
        }
        mRunInBackground = runInBackground;
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
        mStopped = true;
        onStop();
        if (!mRestartable) {
            removeAllListeners();
        }
        return true;
    }

    protected void onStop() {

    }

    public boolean isStarted() {
        return mStarted;
    }

    public boolean start() {
        if (mStarted || mStopped) {
            return false;
        }
        mStarted = true;
        onStart();
        return true;
    }

    protected void onStart() {

    }

    public boolean isRunning() {
        return mStarted && !mStopped;
    }

}

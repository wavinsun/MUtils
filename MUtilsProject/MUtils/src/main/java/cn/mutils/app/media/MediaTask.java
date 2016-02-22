package cn.mutils.app.media;

import android.content.Context;
import android.content.Intent;

import cn.mutils.app.event.listener.OnActivityResultListener;
import cn.mutils.app.ui.core.IActivityExecutor;
import cn.mutils.core.ILockable;
import cn.mutils.core.event.Dispatcher;

public class MediaTask extends Dispatcher implements ILockable {

    protected IActivityExecutor mExecutor;

    protected int mRequestCode;

    protected boolean mLocked = false;

    protected OnActivityResultListener mOnActivityResultListener = new MediaResultListener();

    public MediaTask(IActivityExecutor executor, int requestCode) {
        mExecutor = executor;
        mRequestCode = requestCode;
    }

    @Override
    public boolean isLocked() {
        return mLocked;
    }

    @Override
    public void setLocked(boolean locked) {
        mLocked = locked;
    }

    protected void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {

    }

    class MediaResultListener implements OnActivityResultListener {

        @Override
        public void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
            if (mRequestCode != requestCode) {
                return;
            }
            mExecutor.removeOnActivityResultListener(mOnActivityResultListener);
            mLocked = false;
            MediaTask.this.onActivityResult(context, requestCode, resultCode, data);
        }

    }

}

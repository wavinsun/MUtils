package cn.mutils.app.task;

import android.content.Context;

import cn.mutils.app.core.task.Task;
import cn.mutils.app.os.IContextOwner;

public class ContextOwnerTask extends Task implements IContextOwner {

    protected Context mContext;

    @Override
    public Context getContext() {
        return mContext;
    }

    @Override
    public void setContext(Context context) {
        if (mStarted || mStopped) {
            return;
        }
        mContext = context;
    }
}

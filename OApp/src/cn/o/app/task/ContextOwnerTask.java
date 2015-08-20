package cn.o.app.task;

import android.content.Context;
import cn.o.app.context.IContextOwner;
import cn.o.app.core.task.Task;

public class ContextOwnerTask extends Task implements IContextOwner {

	protected Context mContext;

	@Override
	public Context getContext() {
		return mContext;
	}

	@Override
	public void setContext(Context context) {
		if (mStarted || mStoped) {
			return;
		}
		mContext = context;
	}
}

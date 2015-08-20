package cn.o.app.media;

import android.content.Context;
import android.content.Intent;
import cn.o.app.core.event.Dispatcher;
import cn.o.app.core.task.ILockable;
import cn.o.app.event.listener.OnActivityResultListener;
import cn.o.app.ui.core.IActivityExecutor;

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

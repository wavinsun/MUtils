package cn.o.app.media;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import cn.o.app.OUtil;
import cn.o.app.event.Dispatcher;
import cn.o.app.event.Listener;
import cn.o.app.event.listener.OnActivityResultListener;
import cn.o.app.io.ODate;
import cn.o.app.task.ILockable;
import cn.o.app.ui.core.IActivityResultCatcher;
import cn.o.app.ui.core.IActivityStarter;

/**
 * Take photo by system call
 */
public class TakePhotoTask implements ILockable {

	public static interface TakePhotoListener extends Listener {

		public void onComplete(Uri uri);

	}

	public static final int EXPECT_WIDTH = 768;
	public static final int EXPECT_HEIGHT = 1024;

	protected IActivityResultCatcher mCatcher;

	protected int mRequestCode;

	protected boolean mLocked = false;

	protected Uri mExtraOutput;

	protected Dispatcher mDispatcher = new Dispatcher();

	protected OnActivityResultListener mOnActivityResultListener = new TakePhotoResultListener();

	public TakePhotoTask(IActivityResultCatcher catcher, int requestCode) {
		mRequestCode = requestCode;
		mCatcher = catcher;
	}

	public boolean takePhoto() {
		if (mLocked) {
			return false;
		}
		mExtraOutput = generateExtraOutput();
		if (mExtraOutput == null) {
			return false;
		}
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mExtraOutput);
		if (mCatcher instanceof IActivityStarter) {
			mCatcher.addOnActivityResultListener(mOnActivityResultListener);
			((IActivityStarter) mCatcher).startActivityForResult(intent, mRequestCode);
			mLocked = true;
			return true;
		} else if (mCatcher instanceof Activity) {
			mCatcher.addOnActivityResultListener(mOnActivityResultListener);
			((Activity) mCatcher).startActivityForResult(intent, mRequestCode);
			mLocked = true;
			return true;
		}
		return false;
	}

	public void setListener(TakePhotoListener listener) {
		mDispatcher.setListener(listener);
	}

	protected Uri generateExtraOutput() {
		try {
			String mediaStorageDir = OUtil.getDiskCacheDir(mCatcher.getContext(), "OApp");
			if (mediaStorageDir == null) {
				return null;
			}
			ODate date = new ODate();
			date.setFormat("yyyyMMdd_HHmmss");
			StringBuilder sb = new StringBuilder();
			sb.append(mediaStorageDir);
			sb.append(File.separator);
			sb.append("IMG_");
			sb.append(date.toString());
			sb.append(".jpg");
			return Uri.fromFile(new File(sb.toString()));
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public boolean isLocked() {
		return mLocked;
	}

	@Override
	public void setLocked(boolean locked) {
		mLocked = locked;
	}

	class TakePhotoResultListener implements OnActivityResultListener {

		@Override
		public void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
			if (mRequestCode != requestCode) {
				return;
			}
			mCatcher.removeOnActivityResultListener(mOnActivityResultListener);
			mLocked = false;
			if (resultCode != Activity.RESULT_OK) {
				return;
			}
			Uri uri = data != null ? data.getData() : mExtraOutput;
			if (OUtil.compress(uri.getPath(), EXPECT_WIDTH, EXPECT_HEIGHT)) {
				TakePhotoListener listener = (TakePhotoListener) mDispatcher.getListener();
				if (listener != null) {
					listener.onComplete(uri);
				}
			}
		}

	}

}
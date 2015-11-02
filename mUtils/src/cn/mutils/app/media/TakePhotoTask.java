package cn.mutils.app.media;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import cn.mutils.app.AppUtil;
import cn.mutils.app.core.event.Listener;
import cn.mutils.app.core.time.DateTime;
import cn.mutils.app.ui.core.IActivityExecutor;

/**
 * Take photo by system call
 */
public class TakePhotoTask extends MediaTask {

	public static interface TakePhotoListener extends Listener {

		public void onComplete(Uri uri);

	}

	public static final int EXPECT_WIDTH = 768;
	public static final int EXPECT_HEIGHT = 1024;

	protected Uri mExtraOutput;

	public TakePhotoTask(IActivityExecutor executor, int requestCode) {
		super(executor, requestCode);
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
		mExecutor.startActivityForResult(intent, mRequestCode);
		mExecutor.addOnActivityResultListener(mOnActivityResultListener);
		mLocked = true;
		return true;
	}

	public void setListener(TakePhotoListener listener) {
		super.setListener(listener);
	}

	protected Uri generateExtraOutput() {
		try {
			String mediaStorageDir = AppUtil.getDiskCacheDir(mExecutor.getContext(), AppUtil.TAG);
			if (mediaStorageDir == null) {
				return null;
			}
			DateTime date = new DateTime();
			date.setFormat("yyyyMMdd_HHmmss");
			StringBuilder sb = new StringBuilder();
			sb.append(mediaStorageDir);
			sb.append("IMG_");
			sb.append(date.toString());
			sb.append(".jpg");
			return Uri.fromFile(new File(sb.toString()));
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	protected void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		Uri uri = data != null ? data.getData() : mExtraOutput;
		if (AppUtil.compress(uri.getPath(), EXPECT_WIDTH, EXPECT_HEIGHT)) {
			TakePhotoListener listener = getListener(TakePhotoListener.class);
			if (listener != null) {
				listener.onComplete(uri);
			}
		}
	}

}
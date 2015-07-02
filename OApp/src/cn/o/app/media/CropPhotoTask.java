package cn.o.app.media;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import cn.o.app.OUtil;
import cn.o.app.event.Dispatcher;
import cn.o.app.event.Listener;
import cn.o.app.event.listener.OnActivityResultListener;
import cn.o.app.task.ILockable;
import cn.o.app.ui.core.IActivityResultCatcher;
import cn.o.app.ui.core.IActivityStarter;

/**
 * Crop photo by system call
 */
public class CropPhotoTask implements ILockable {

	public static interface CropPhotoListener extends Listener {

		public void onComplete(Uri uri);

	}

	protected IActivityResultCatcher mCatcher;

	protected int mRequestCode;

	protected boolean mLocked = false;

	protected Uri mExtraOutput;

	protected Dispatcher mDispatcher = new Dispatcher();

	protected OnActivityResultListener mOnActivityResultListener = new OnActivityResultListener() {

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
			if (OUtil.compress(mExtraOutput.getPath())) {
				CropPhotoListener listener = (CropPhotoListener) mDispatcher.getListener();
				if (listener != null) {
					listener.onComplete(mExtraOutput);
				}
			}
		}
	};

	public CropPhotoTask(IActivityResultCatcher catcher, int requestCode) {
		mRequestCode = requestCode;
		mCatcher = catcher;
	}

	public boolean cropPhoto(Uri uri, int width, int height) {
		if (mLocked) {
			return false;
		}
		mExtraOutput = generateExtraOutput(uri, width, height);
		if (mExtraOutput == null) {
			return false;
		}
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", width);
		intent.putExtra("aspectY", height);
		intent.putExtra("outputX", width);
		intent.putExtra("outputY", height);
		intent.putExtra("return-data", false);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, mExtraOutput);
		intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
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

	public void setListener(CropPhotoListener listener) {
		mDispatcher.setListener(listener);
	}

	protected Uri generateExtraOutput(Uri uri, int width, int height) {
		try {
			String mediaStorageDir = OUtil.getDiskCacheDir(mCatcher.getContext(), "OApp");
			if (mediaStorageDir == null) {
				return null;
			}
			String md5 = OUtil.md5(uri.getPath());
			if (md5.isEmpty()) {
				return null;
			}
			StringBuilder sb = new StringBuilder();
			sb.append(mediaStorageDir);
			sb.append(File.separator);
			sb.append("IMG_");
			sb.append(md5);
			sb.append("_");
			sb.append(width);
			sb.append("x");
			sb.append(height);
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

}

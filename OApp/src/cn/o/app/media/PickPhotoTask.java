package cn.o.app.media;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import cn.o.app.OUtil;
import cn.o.app.event.Dispatcher;
import cn.o.app.event.Listener;
import cn.o.app.event.listener.OnActivityResultListener;
import cn.o.app.ui.core.IActivityResultCatcher;
import cn.o.app.ui.core.IActivityStarter;

/**
 * Pick photo by system call
 */
public class PickPhotoTask {

	public static interface PickPhotoListener extends Listener {

		public void onComplete(Uri uri);

	}

	public static final int EXPECT_WIDTH = 768;
	public static final int EXPECT_HEIGHT = 1024;

	protected IActivityResultCatcher mCatcher;

	protected int mRequestCode;

	protected Dispatcher mDispatcher = new Dispatcher();

	protected OnActivityResultListener mOnActivityResultListener = new OnActivityResultListener() {

		@Override
		public void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
			if (mRequestCode != requestCode) {
				return;
			}
			mCatcher.removeOnActivityResultListener(mOnActivityResultListener);
			if (resultCode != Activity.RESULT_OK) {
				return;
			}
			CursorLoader cursorLoader = new CursorLoader(mCatcher.getContext(), data.getData(),
					new String[] { MediaStore.Images.Media.DATA }, null, null, null);
			Cursor cursor = cursorLoader.loadInBackground();
			if (!cursor.moveToFirst()) {
				return;
			}
			String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
			if (path == null || path.isEmpty()) {
				return;
			}
			String mediaStorageDir = OUtil.getDiskCacheDir(mCatcher.getContext(), "OApp");
			if (mediaStorageDir == null) {
				return;
			}
			String md5 = OUtil.md5(path);
			if (md5.isEmpty()) {
				return;
			}
			StringBuilder sb = new StringBuilder();
			sb.append(mediaStorageDir);
			sb.append(File.separator);
			sb.append("IMG_");
			sb.append(md5);
			sb.append(".jpg");
			String output = sb.toString();
			if (OUtil.compress(path, output, EXPECT_WIDTH, EXPECT_HEIGHT)) {
				PickPhotoListener listener = (PickPhotoListener) mDispatcher.getListener();
				if (listener != null) {
					listener.onComplete(Uri.fromFile(new File(output)));
				}
			}
		}
	};

	public PickPhotoTask(IActivityResultCatcher catcher, int requestCode) {
		mRequestCode = requestCode;
		mCatcher = catcher;
	}

	public boolean pickPhoto() {
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		if (mCatcher instanceof IActivityStarter) {
			mCatcher.addOnActivityResultListener(mOnActivityResultListener);
			((IActivityStarter) mCatcher).startActivityForResult(intent, mRequestCode);
			return true;
		} else if (mCatcher instanceof Activity) {
			mCatcher.addOnActivityResultListener(mOnActivityResultListener);
			((Activity) mCatcher).startActivityForResult(intent, mRequestCode);
			return true;
		}
		return false;
	}

	public void setListener(PickPhotoListener listener) {
		mDispatcher.setListener(listener);
	}

}

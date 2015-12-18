package cn.mutils.app.media;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;
import cn.mutils.app.core.event.IListener;
import cn.mutils.app.ui.core.IActivityExecutor;
import cn.mutils.app.util.AppUtil;

/**
 * Pick photo by system call
 */
public class PickPhotoTask extends MediaTask {

	public static interface PickPhotoListener extends IListener {

		public void onComplete(Uri uri);

	}

	public static final int EXPECT_WIDTH = 768;
	public static final int EXPECT_HEIGHT = 1024;

	public PickPhotoTask(IActivityExecutor executor, int requestCode) {
		super(executor, requestCode);
	}

	public boolean pickPhoto() {
		if (mLocked) {
			return false;
		}
		Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		mExecutor.startActivityForResult(intent, mRequestCode);
		mExecutor.addOnActivityResultListener(mOnActivityResultListener);
		mLocked = true;
		return true;
	}

	public void setListener(PickPhotoListener listener) {
		super.setListener(listener);
	}

	@Override
	protected void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		CursorLoader cursorLoader = new CursorLoader(mExecutor.getContext(), data.getData(),
				new String[] { MediaStore.Images.Media.DATA }, null, null, null);
		Cursor cursor = cursorLoader.loadInBackground();
		if (!cursor.moveToFirst()) {
			return;
		}
		String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
		if (path == null || path.isEmpty()) {
			return;
		}
		String mediaStorageDir = AppUtil.getDiskCacheDir(mExecutor.getContext(), AppUtil.TAG);
		if (mediaStorageDir == null) {
			return;
		}
		String md5 = AppUtil.md5(path);
		if (md5.isEmpty()) {
			return;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(mediaStorageDir);
		sb.append("IMG_");
		sb.append(md5);
		sb.append(".jpg");
		String output = sb.toString();
		if (AppUtil.compress(path, output, EXPECT_WIDTH, EXPECT_HEIGHT)) {
			PickPhotoListener listener = getListener(PickPhotoListener.class);
			if (listener != null) {
				listener.onComplete(Uri.fromFile(new File(output)));
			}
		}
	}

}

package cn.mutils.app.media;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Video.VideoColumns;
import cn.mutils.app.core.event.IListener;
import cn.mutils.app.ui.core.IActivityExecutor;

public class MakeVideoTask extends MediaTask {

	public static interface MakeVideoListener extends IListener {

		public void onComplete(Uri uri);

	}

	public MakeVideoTask(IActivityExecutor executor, int requestCode) {
		super(executor, requestCode);
	}

	public boolean makeVideo() {
		if (mLocked) {
			return false;
		}
		Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		mExecutor.startActivityForResult(intent, mRequestCode);
		mExecutor.addOnActivityResultListener(mOnActivityResultListener);
		mLocked = true;
		return true;
	}

	public void setListener(MakeVideoListener listener) {
		super.setListener(listener);
	}

	@Override
	protected void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) {
			return;
		}
		Cursor cursor = mExecutor.getContext().getContentResolver().query(data.getData(), null, null, null, null);
		if (cursor == null || !cursor.moveToNext()) {
			return;
		}
		String path = cursor.getString(cursor.getColumnIndex(VideoColumns.DATA));
		MakeVideoListener listener = getListener(MakeVideoListener.class);
		if (listener != null) {
			listener.onComplete(Uri.fromFile(new File(path)));
		}
	}

}

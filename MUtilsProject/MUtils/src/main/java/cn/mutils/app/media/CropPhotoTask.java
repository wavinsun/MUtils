package cn.mutils.app.media;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.File;

import cn.mutils.app.ui.core.IActivityExecutor;
import cn.mutils.app.util.AppUtil;
import cn.mutils.core.event.IListener;

/**
 * Crop photo by system call
 */
@SuppressWarnings({"UnnecessaryInterfaceModifier", "StringBufferReplaceableByString"})
public class CropPhotoTask extends MediaTask {

    public static interface CropPhotoListener extends IListener {

        void onComplete(Uri uri);

    }

    protected Uri mExtraOutput;

    public CropPhotoTask(IActivityExecutor executor, int requestCode) {
        super(executor, requestCode);
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
        mExecutor.startActivityForResult(intent, mRequestCode);
        mExecutor.addOnActivityResultListener(mOnActivityResultListener);
        mLocked = true;
        return false;
    }

    public void setListener(CropPhotoListener listener) {
        super.setListener(listener);
    }

    protected Uri generateExtraOutput(Uri uri, int width, int height) {
        try {
            String mediaStorageDir = AppUtil.getDiskCacheDir(mExecutor.getContext(), AppUtil.TAG);
            if (mediaStorageDir == null) {
                return null;
            }
            String md5 = AppUtil.md5(uri.getPath());
            if (md5.isEmpty()) {
                return null;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(mediaStorageDir);
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
    protected void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (AppUtil.compress(mExtraOutput.getPath())) {
            CropPhotoListener listener = getListener(CropPhotoListener.class);
            if (listener != null) {
                listener.onComplete(mExtraOutput);
            }
        }
    }

}

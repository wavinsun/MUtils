package cn.o.app.qrcode.decode;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import cn.o.app.qrcode.CaptureConst;
import cn.o.app.qrcode.CaptureView;
import cn.o.app.qrcode.camera.CameraManager;

import com.google.zxing.Result;

public class CaptureHandler extends Handler {

	protected CaptureView mView;
	protected DecodeThread mDecodeThread;
	protected State mState;

	private enum State {
		PREVIEW, SUCCESS, DONE
	}

	public CaptureHandler(CaptureView view, String charset) {
		this.mView = view;
		mDecodeThread = new DecodeThread(view, charset);
		mDecodeThread.start();
		mState = State.SUCCESS;

		// Start ourselves capturing previews and decoding.
		CameraManager.get().startPreview();
		restartPreviewAndDecode();
	}

	@Override
	public void handleMessage(Message message) {
		switch (message.what) {
		case CaptureConst.ID_AUTO_FOCUS:
			// Log.d(TAG, "Got auto-focus message");
			// When one auto focus pass finishes, start another. This is the
			// closest thing to
			// continuous AF. It does seem to hunt a bit, but I'm not sure what
			// else to do.
			if (mState == State.PREVIEW) {
				CameraManager.get().requestAutoFocus(this,
						CaptureConst.ID_AUTO_FOCUS);
			}
			break;
		case CaptureConst.ID_RESTART_PREVIEW:
			// Log.d(TAG, "Got restart preview message");
			restartPreviewAndDecode();
			break;
		case CaptureConst.ID_DECODE_SUCCEEDED:
			// Log.d(TAG, "Got decode succeeded message");
			mState = State.SUCCESS;
			Bundle bundle = message.getData();
			Bitmap barcode = bundle == null ? null : (Bitmap) bundle
					.getParcelable(DecodeThread.BARCODE_BITMAP);
			mView.handleDecode((Result) message.obj, barcode);
			break;
		case CaptureConst.ID_DECODE_FAILED:
			// We're decoding as fast as possible, so when one decode fails,
			// start another.
			mState = State.PREVIEW;
			CameraManager.get().requestPreviewFrame(mDecodeThread.getHandler(),
					CaptureConst.ID_DECODE);
			break;
		case CaptureConst.ID_RETURN_SCAN_RESULT:
			// Log.d(TAG, "Got return scan result message");
			// activity.setResult(Activity.RESULT_OK, (Intent) message.obj);
			// activity.finish();
			mView.handleDecode((Result) message.obj, null);
			break;
		}
	}

	public void quitSynchronously() {
		mState = State.DONE;
		CameraManager.get().stopPreview();
		Message quit = Message.obtain(mDecodeThread.getHandler(),
				CaptureConst.ID_QUIT);
		quit.sendToTarget();
		try {
			mDecodeThread.join();
		} catch (InterruptedException e) {
			// continue
		}

		// Be absolutely sure we don't send any queued up messages
		removeMessages(CaptureConst.ID_DECODE_SUCCEEDED);
		removeMessages(CaptureConst.ID_DECODE_FAILED);
	}

	private void restartPreviewAndDecode() {
		if (mState == State.SUCCESS) {
			mState = State.PREVIEW;
			CameraManager.get().requestPreviewFrame(mDecodeThread.getHandler(),
					CaptureConst.ID_DECODE);
			CameraManager.get().requestAutoFocus(this,
					CaptureConst.ID_AUTO_FOCUS);
			mView.drawViewfinder();
		}
	}
}

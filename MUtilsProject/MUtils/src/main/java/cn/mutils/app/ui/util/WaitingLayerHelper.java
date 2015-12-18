package cn.mutils.app.ui.util;

import android.os.Handler;
import android.os.Looper;
import cn.mutils.app.ui.AppActivity;
import cn.mutils.app.ui.Dialoger;

/**
 * Helper class for waiting layer
 */
public class WaitingLayerHelper {

	protected Handler mWaitingLayerHandler;
	protected Runnable mWaitingLayerRunnable;
	protected Dialoger mWaitingDialog;

	protected AppActivity mActivity;

	public WaitingLayerHelper(AppActivity context) {
		mActivity = context;
		mWaitingDialog = mActivity.newWaitingDialog();
		if (mWaitingDialog == null) {
			mWaitingDialog = new WaitingDialog(mActivity);
		}
	}

	public void postUpdateWaitingViewState() {
		if (mWaitingLayerHandler == null) {
			mWaitingLayerHandler = new Handler(Looper.getMainLooper());
			mWaitingLayerRunnable = new Runnable() {

				@Override
				public void run() {
					mWaitingLayerHandler.removeCallbacksAndMessages(null);
					mActivity.updateWaitingLayerState();
				}
			};
		}
		mWaitingLayerHandler.postDelayed(mWaitingLayerRunnable, 400);
	}

	public void show() {
		if (mWaitingDialog.isShowing()) {
			return;
		}
		mWaitingDialog.show();
	}

	public void hide() {
		if (!mWaitingDialog.isShowing()) {
			return;
		}
		mWaitingDialog.dismiss();
	}

	public void onDestroy() {
		if (mWaitingLayerHandler != null) {
			mWaitingLayerHandler.removeCallbacksAndMessages(null);
		}
		mWaitingDialog.dismiss();
	}

}

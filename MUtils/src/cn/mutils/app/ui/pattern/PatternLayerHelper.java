package cn.mutils.app.ui.pattern;

import android.os.Handler;
import cn.mutils.app.ui.AppActivity;

/**
 * Helper class for pattern layer of gestures password
 */
public class PatternLayerHelper {

	/** No checking gestures password for three minutes */
	public static final long PATTERN_DISABLE_SHORT = 180000L;

	/** No checking gestures password for five minutes */
	public static final long PATTERN_DISABLE_LONG = 300000L;

	protected static long sHeartbeatTime = 0;

	/** Whether open UI heart beat */
	protected boolean mHeartbeatEnable;

	protected long mNoPatternDeadLine = 0;
	protected long mNoPatternDuration = 0;

	protected Handler mHeartbeatHandler;
	protected Runnable mHeartbeatRunnable;

	protected AppActivity mActivity;
	protected PatternDialog mPatternDialog;

	public PatternLayerHelper(AppActivity context) {
		mActivity = context;
		mPatternDialog = mActivity.newPatternDialog();
	}

	public void show() {
		if (mPatternDialog == null) {
			return;
		}
		if (mPatternDialog.isShowing()) {
			return;
		}
		mPatternDialog.refresh();
		mPatternDialog.show();
	}

	public void hide() {
		if (mPatternDialog == null) {
			return;
		}
		if (!mPatternDialog.isShowing()) {
			return;
		}
		mPatternDialog.dismiss();
	}

	public void onDestroy() {
		if (mHeartbeatHandler != null) {
			mHeartbeatHandler.removeCallbacksAndMessages(null);
		}
		if (mPatternDialog != null) {
			mPatternDialog.dismiss();
		}
	}

	public void onStart() {
		if (mHeartbeatEnable) {
			if (mActivity.getApplicationContext() instanceof IPatternDataProvider) {
				((IPatternDataProvider) mActivity.getApplicationContext()).isLogined();
			}
			mHeartbeatHandler.postDelayed(mHeartbeatRunnable, 1000);
		}
	}

	public void onResume() {
		if (mHeartbeatEnable) {
			if (mNoPatternDeadLine == 0) {
				mActivity.doCheckPattern();
			} else {
				if (mNoPatternDeadLine < System.currentTimeMillis()) {
					mActivity.doCheckPattern();
					mNoPatternDeadLine = 0;
				}
			}
			mActivity.enablePattern();
		}
	}

	public void onStop() {
		if (mHeartbeatEnable) {
			mHeartbeatHandler.removeCallbacksAndMessages(null);
			long currentTime = System.currentTimeMillis();
			sHeartbeatTime = currentTime;
			if (mNoPatternDuration != 0) {
				mNoPatternDeadLine = currentTime + mNoPatternDuration;
				mNoPatternDuration = 0;
			}
		}
	}

	public void disable(long duration) {
		mNoPatternDuration = duration;
	}

	public void enable() {
		mNoPatternDeadLine = 0;
		mNoPatternDuration = 0;
	}

	public boolean isHeartbeatEnabled() {
		return mHeartbeatEnable;
	}

	public void setHeartbeatEnabled(boolean enabled) {
		mHeartbeatEnable = enabled;
		if (mHeartbeatEnable) {
			if (mHeartbeatHandler == null) {
				mHeartbeatHandler = new Handler();
				mHeartbeatRunnable = new Runnable() {

					@Override
					public void run() {
						sHeartbeatTime = System.currentTimeMillis();
						mHeartbeatHandler.removeCallbacksAndMessages(null);
						mHeartbeatHandler.postDelayed(mHeartbeatRunnable, 1000);
					}
				};
			}
			if (mActivity.isRunning()) {
				mHeartbeatHandler.postDelayed(mHeartbeatRunnable, 1000);
			}
		}
	}

	public void doCheck() {
		if (!(mActivity.getApplicationContext() instanceof IPatternDataProvider)) {
			return;
		}
		IPatternDataProvider patternDataProvider = (IPatternDataProvider) mActivity.getApplicationContext();
		if (patternDataProvider.isLogined()) {
			if (patternDataProvider.isPatternEnabled()) {
				if (sHeartbeatTime == 0
						|| (System.currentTimeMillis() - sHeartbeatTime) > patternDataProvider.getPatternPeriod()) {
					show();
				}
			}
		}
	}

}

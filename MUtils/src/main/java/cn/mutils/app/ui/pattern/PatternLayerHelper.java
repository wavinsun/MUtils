package cn.mutils.app.ui.pattern;

import android.os.Handler;
import android.os.Looper;

import cn.mutils.app.ui.AppActivity;

/**
 * Helper class for pattern layer of gestures password
 */
@SuppressWarnings("unused")
public class PatternLayerHelper {

    /**
     * No checking gestures password for three minutes
     */
    public static final long PATTERN_DISABLE_SHORT = 180000L;

    /**
     * No checking gestures password for five minutes
     */
    public static final long PATTERN_DISABLE_LONG = 300000L;

    protected static long sHeartbeatTime = 0;

    /**
     * Whether open UI heart beat
     */
    protected boolean mHeartbeatEnable;

    protected long mNoPatternDeadLine = 0;
    protected long mNoPatternDuration = 0;

    protected Handler mHeartbeatHandler;
    protected Runnable mHeartbeatRunnable;

    protected AppActivity mActivity;

    public PatternLayerHelper(AppActivity context) {
        mActivity = context;
    }

    public void show() {
        mActivity.startPatternActivity();
    }

    public void onDestroy() {
        if (mHeartbeatHandler != null) {
            mHeartbeatHandler.removeCallbacksAndMessages(null);
        }
    }

    public void onStart() {
        if (mHeartbeatEnable) {
            mHeartbeatHandler.postDelayed(mHeartbeatRunnable, 1000);
        }
    }

    public void onResume() {
        if (mHeartbeatEnable) {
            if (mNoPatternDeadLine == 0) {
                if (mActivity.checkPattern()) {
                    doCheck();
                }
            } else {
                if (mNoPatternDeadLine < System.currentTimeMillis()) {
                    if (mActivity.checkPattern()) {
                        doCheck();
                    }
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
                mHeartbeatHandler = new Handler(Looper.getMainLooper());
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
        if (sHeartbeatTime == 0 || (System.currentTimeMillis() - sHeartbeatTime) > 1500) {
            show();
        }
    }

}

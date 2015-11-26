package cn.mutils.app.ui.motion;

import android.content.Context;
import cn.mutils.app.ui.core.IToastOwner;

public class DoubleBackClickHelper {

	protected Context mContext;

	protected boolean mEnabled = true;

	protected long mTimeOfBackPressed;

	public DoubleBackClickHelper(Context context) {
		mContext = context;
	}

	public boolean isEnabled() {
		return mEnabled;
	}

	public void setEnabled(boolean enabled) {
		mEnabled = enabled;
	}

	public boolean onInterceptBackPressed() {
		if (!mEnabled) {
			return false;
		}
		long now = System.currentTimeMillis();
		if (now - mTimeOfBackPressed > 3000) {
			mTimeOfBackPressed = now;
			if (mContext instanceof IToastOwner) {
				((IToastOwner) mContext).toast("再按一次退出");
			}
			return true;
		}
		mTimeOfBackPressed = now;
		return false;
	}

}

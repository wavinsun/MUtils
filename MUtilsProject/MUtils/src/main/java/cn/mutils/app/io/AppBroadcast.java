package cn.mutils.app.io;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import cn.mutils.app.core.reflect.ReflectUtil;

@SuppressWarnings("unchecked")
public class AppBroadcast<EXTRA extends Extra> implements IBroadcast {

	public static interface OnReceiveListener<EXTRA extends Extra> {

		public void onReceive(AppBroadcast<EXTRA> broadcast, EXTRA extra);

	}

	protected String mAction;

	protected Context mContext;

	protected boolean mStoped;

	protected OnReceiveListener<EXTRA> mOnReceiveListener;

	protected AppBroadcastReceiver mReceiver;

	public AppBroadcast(Context context) {
		mContext = context;
		mStoped = true;
	}

	public String getAction() {
		return mAction;
	}

	public void setAction(String action) {
		mAction = action;
		if (this.stop()) {
			this.start();
		}
	}

	@Override
	public boolean isLocked() {
		return true;
	}

	@Override
	public void setLocked(boolean locked) {

	}

	@Override
	public boolean isRunInBackground() {
		return true;
	}

	@Override
	public void setRunInBackground(boolean runInBackground) {

	}

	@Override
	public boolean isStoped() {
		return mStoped;
	}

	@Override
	public boolean stop() {
		if (mStoped) {
			return false;
		}
		mStoped = true;
		mContext.unregisterReceiver(mReceiver);
		return true;
	}

	public AppBroadcast<EXTRA> start() {
		this.stop();
		if (mReceiver == null) {
			mReceiver = new AppBroadcastReceiver();
		}
		IntentFilter filter = new IntentFilter();
		filter.addAction(mAction);
		mContext.registerReceiver(mReceiver, filter);
		mStoped = false;
		return this;
	}

	public void send(EXTRA extra) {
		Intent intent = new Intent();
		intent.setAction(mAction);
		if (extra.putTo(intent)) {
			mContext.sendBroadcast(intent);
		}
	}

	@Override
	public Context getContext() {
		return mContext;
	}

	@Override
	public void setContext(Context context) {

	}

	public void setOnReceiveListener(OnReceiveListener<EXTRA> listener) {
		mOnReceiveListener = listener;
	}

	class AppBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (mOnReceiveListener != null) {
				try {
					Class<EXTRA> extraClass = (Class<EXTRA>) ReflectUtil
							.getParameterizedRawType(AppBroadcast.this.getClass(), 0);
					EXTRA extra = extraClass.newInstance();
					if (extra.getFrom(intent)) {
						mOnReceiveListener.onReceive(AppBroadcast.this, extra);
					}
				} catch (Exception e) {

				}
			}
		}

	}
}

package cn.o.app.ui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cn.o.app.AppUtil;
import cn.o.app.R;
import cn.o.app.core.event.Dispatcher;
import cn.o.app.core.net.NetClient.CookieExpiredException;
import cn.o.app.core.task.IStopable;
import cn.o.app.data.AsyncDataQueue;
import cn.o.app.data.IAsyncDataQueue;
import cn.o.app.data.IAsyncDataQueueListener;
import cn.o.app.data.IAsyncDataTask;
import cn.o.app.event.listener.OnActivityResultListener;
import cn.o.app.net.INetQueue;
import cn.o.app.net.INetQueueListener;
import cn.o.app.net.INetTask;
import cn.o.app.net.NetQueue;
import cn.o.app.queue.IQueue;
import cn.o.app.ui.core.IActivity;
import cn.o.app.ui.core.IPrivateActivity;
import cn.o.app.ui.core.IStateView;
import cn.o.app.ui.core.IToastOwner;
import cn.o.app.ui.core.UICore;
import cn.o.app.ui.pattern.IPatternDataProvider;
import cn.o.app.ui.pattern.PatternDialog;

@SuppressLint({ "ShowToast", "InlinedApi" })
@SuppressWarnings("deprecation")
public class Activitier extends FragmentActivity implements IActivity {

	protected AsyncDataQueue mAsyncDataQueue;

	protected NetQueue mNetQueue;

	protected PatternLayerHelper mPatternLayerHelper;

	protected WaitingLayerHelper mWaitingLayerHelper;

	protected boolean mBusy;

	protected List<IStateView> mBindViews;

	protected List<IStopable> mBindStopables;

	protected InfoToast mInfoToast;

	protected Toast mToast;

	protected boolean mRunning;

	protected boolean mFinished;

	protected boolean mStatusBarTranslucent;

	protected Dispatcher mDispatcher;

	public static void redirectTo(Class<? extends Activity> activityCls) {
		ActivityMgr.redirectTo(activityCls);
	}

	public static void finishAll() {
		ActivityMgr.finishAll();
	}

	public Context getContext() {
		return this;
	}

	@Override
	public InfoToast getInfoToast() {
		return mInfoToast;
	}

	public Toast getToast() {
		if (mToast == null) {
			mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		}
		return mToast;
	}

	public void toast(CharSequence s) {
		UICore.toast(this, s);
	}

	public void toast(int resId, Object... args) {
		UICore.toast(this, resId, args);
	}

	@Override
	public IToastOwner getToastOwner() {
		return this;
	}

	public boolean isBusy() {
		return mBusy;
	}

	public void setBusy(boolean busy) {
		mBusy = busy;
		if (mWaitingLayerHelper == null) {
			mWaitingLayerHelper = new WaitingLayerHelper(this);
		}
		mWaitingLayerHelper.postUpdateWaitingViewState();
	}

	@Override
	public void bind(IStopable stopable) {
		UICore.bind(this, stopable);
	}

	@Override
	public void disablePattern(long duration) {
		if (mPatternLayerHelper != null) {
			mPatternLayerHelper.disable(duration);
		}
	}

	@Override
	public void enablePattern() {
		if (mPatternLayerHelper != null) {
			mPatternLayerHelper.enable();
		}
	}

	public boolean isRunning() {
		return mRunning;
	}

	public boolean isHeartbeatEnabled() {
		return mPatternLayerHelper != null && mPatternLayerHelper.isHeartbeatEnabled();
	}

	public void setHeartbeatEnabled(boolean enabled) {
		if (mPatternLayerHelper == null) {
			mPatternLayerHelper = new PatternLayerHelper(this);
		}
		mPatternLayerHelper.setHeartbeatEnabled(enabled);
	}

	@Override
	public boolean checkPattern() {
		return false;
	}

	@Override
	public void doCheckPattern() {
		if (!this.checkPattern()) {
			return;
		}
		if (mPatternLayerHelper != null) {
			mPatternLayerHelper.doCheck();
		}
	}

	@Override
	public PatternDialog newPatternDialog() {
		return null;
	}

	@Override
	public void showPattern() {
		if (mPatternLayerHelper == null) {
			mPatternLayerHelper = new PatternLayerHelper(this);
		}
		mPatternLayerHelper.show();
	}

	@Override
	public void hidePattern() {
		if (mPatternLayerHelper == null) {
			return;
		}
		mPatternLayerHelper.hide();
	}

	@Override
	public void bind(IStateView stateView) {
		UICore.bind(this, stateView);
	}

	public View getContentView() {
		return UICore.getContentView(this);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		this.bindStateViews();
		UICore.injectResources(this);
		UICore.injectEvents(this);
	}

	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		this.bindStateViews();
		UICore.injectResources(this);
		UICore.injectEvents(this);
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
		bindStateViews();
		UICore.injectResources(this);
		UICore.injectEvents(this);
	}

	protected void bindStateViews() {
		UICore.bindStateViews(this, this.getWindow().getDecorView());
	}

	@Override
	public boolean isStatusBarTranslucent() {
		return mStatusBarTranslucent;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Window w = this.getWindow();
		w.requestFeature(Window.FEATURE_NO_TITLE);
		w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			mStatusBarTranslucent = true;
		}

		super.onCreate(savedInstanceState);

		ActivityMgr.attach(this);

		UICore.injectContentView(this);

		mRunning = true;
	}

	@Override
	protected void onStart() {
		super.onStart();
		UICore.dispatchStart(this);
		if (mPatternLayerHelper != null) {
			mPatternLayerHelper.onStart();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		UICore.dispatchResume(this);
		if (mPatternLayerHelper != null) {
			mPatternLayerHelper.onResume();
		}
		mRunning = true;
	}

	@Override
	protected void onPause() {
		mRunning = false;
		UICore.dispatchPause(this);
		super.onPause();
	}

	@Override
	protected void onStop() {
		UICore.dispatchStop(this);
		if (mPatternLayerHelper != null) {
			mPatternLayerHelper.onStop();
		}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		doFinish();
		super.onDestroy();
	}

	@Override
	public void finish() {
		doFinish();
		super.finish();
	}

	public boolean isFinished() {
		return mFinished;
	}

	protected void doFinish() {
		if (mFinished) {
			return;
		}
		mFinished = true;
		if (mNetQueue != null) {
			mNetQueue.clear();
		}
		if (mAsyncDataQueue != null) {
			mAsyncDataQueue.clear();
		}
		if (mWaitingLayerHelper != null) {
			mWaitingLayerHelper.onDestroy();
		}
		if (mPatternLayerHelper != null) {
			mPatternLayerHelper.onDestroy();
		}
		UICore.dispatchDestroy(this);
		ActivityMgr.detach(this);
	}

	@Override
	public void onBackPressed() {
		if (this.onInterceptBackPressed()) {
			return;
		}
		super.onBackPressed();
	}

	public boolean onInterceptBackPressed() {
		if (UICore.interceptBackPressed(this)) {
			return true;
		}
		if (mBusy) {
			this.setBusy(false);
			return true;
		}
		return false;
	}

	public INetQueue getNetQueue() {
		if (mNetQueue == null) {
			mNetQueue = new NetQueue();
			mNetQueue.setContext(this);
			mNetQueue.addListener(new INetQueueListener() {

				@Override
				public void onRunStateChanged(IQueue queue) {
					if (mWaitingLayerHelper == null) {
						mWaitingLayerHelper = new WaitingLayerHelper(Activitier.this);
					}
					mWaitingLayerHelper.postUpdateWaitingViewState();
				}
			});
		}
		return mNetQueue;
	}

	public IAsyncDataQueue getAsyncDataQueue() {
		if (mAsyncDataQueue == null) {
			mAsyncDataQueue = new AsyncDataQueue();
			mAsyncDataQueue.setContext(this);
			mAsyncDataQueue.addListener(new IAsyncDataQueueListener() {

				@Override
				public void onRunStateChanged(IQueue queue) {
					if (mWaitingLayerHelper == null) {
						mWaitingLayerHelper = new WaitingLayerHelper(Activitier.this);
					}
					mWaitingLayerHelper.postUpdateWaitingViewState();
				}
			});
		}
		return mAsyncDataQueue;
	}

	protected void updateWaitingLayerState() {
		if (mNetQueue == null) {
			if (mAsyncDataQueue == null) {
				if (!mBusy) {
					hideWaiting();
				} else {
					showWaiting();
				}
			} else {
				if ((!mBusy) && mAsyncDataQueue.isRunInBackground()) {
					hideWaiting();
				} else {
					showWaiting();
				}
			}
		} else {
			if (mAsyncDataQueue == null) {
				if ((!mBusy) && mNetQueue.isRunInBackground()) {
					hideWaiting();
				} else {
					showWaiting();
				}
			} else {
				if ((!mBusy) && mNetQueue.isRunInBackground() && mAsyncDataQueue.isRunInBackground()) {
					this.hideWaiting();
				} else {
					this.showWaiting();
				}
			}
		}
	}

	protected void showWaiting() {
		if (mWaitingLayerHelper == null) {
			mWaitingLayerHelper = new WaitingLayerHelper(this);
		}
		mWaitingLayerHelper.show();
	}

	protected void hideWaiting() {
		if (mWaitingLayerHelper == null) {
			return;
		}
		mWaitingLayerHelper.hide();
	}

	protected Dialoger newWaitingDialog() {
		return null;
	}

	public void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
		UICore.onActivityResult(this, requestCode, resultCode, data);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		onActivityResult(this, requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		UICore.dispatchTouchEvent(ev, this);
		return super.dispatchTouchEvent(ev);
	}

	public void add(INetTask<?, ?> task) {
		this.bind(task);
		getNetQueue().add(task);
	}

	public void add(IAsyncDataTask<?> task) {
		this.bind(task);
		getAsyncDataQueue().add(task);
	}

	@Override
	public void notify(Object message) {
		if (message == null) {
			return;
		}
		if (message instanceof CookieExpiredException) {
			if (this instanceof IPrivateActivity) {
				((IPrivateActivity) this).refresh();
			}
		}
	}

	@Override
	public void stopAll() {
		UICore.stopAll(this);
	}

	@Override
	public void stopAll(boolean includeLockable) {
		UICore.stopAll(this, includeLockable);
	}

	@Override
	public <T extends View> T findViewById(int id, Class<T> viewClass) {
		return UICore.findViewById(this, id, viewClass);
	}

	@Override
	public List<IStateView> getBindStateViews() {
		if (mBindViews == null) {
			mBindViews = new ArrayList<IStateView>();
		}
		return mBindViews;
	}

	@Override
	public List<IStopable> getBindStopables() {
		if (mBindStopables == null) {
			mBindStopables = new LinkedList<IStopable>();
		}
		return mBindStopables;
	}

	@Override
	public boolean redirectToSelectedView() {
		return false;
	}

	@Override
	public IStateView getSelectedView() {
		return null;
	}

	@Override
	public List<OnActivityResultListener> getOnActivityResultListeners() {
		if (mDispatcher == null) {
			mDispatcher = new Dispatcher();
		}
		return mDispatcher.getListeners(OnActivityResultListener.EVENT_TYPE, OnActivityResultListener.class);
	}

	@Override
	public void addOnActivityResultListener(OnActivityResultListener listener) {
		if (mDispatcher == null) {
			mDispatcher = new Dispatcher();
		}
		mDispatcher.addListener(OnActivityResultListener.EVENT_TYPE, listener);
	}

	@Override
	public void removeOnActivityResultListener(OnActivityResultListener listener) {
		if (mDispatcher == null) {
			return;
		}
		mDispatcher.removeListener(OnActivityResultListener.EVENT_TYPE, listener);
	}

	protected static class ActivityMgr {

		protected static List<Activity> sActivitys;

		public static void redirectTo(Class<? extends Activity> activityCls) {
			if (sActivitys == null) {
				return;
			}
			boolean finishBehind = false;
			for (int i = 0; i < sActivitys.size(); i++) {
				Activity activity = sActivitys.get(i);
				if (finishBehind) {
					activity.finish();
					i--;
				} else {
					if (activityCls.isInstance(activity)) {
						finishBehind = true;
					}
				}
			}
		}

		public static void finishAll() {
			if (sActivitys == null) {
				return;
			}
			for (Activity activity : sActivitys) {
				activity.finish();
			}
			sActivitys.clear();
		}

		public static void attach(Activity activity) {
			if (sActivitys == null) {
				sActivitys = new CopyOnWriteArrayList<Activity>();
			} else {
				if (sActivitys.contains(activity)) {
					return;
				}
			}
			sActivitys.add(activity);
		}

		public static void detach(Activity activity) {
			if (sActivitys == null) {
				return;
			}
			sActivitys.remove(activity);
		}
	}

	/**
	 * Waiting Dialog
	 */
	public static class WaitingDialog extends Dialoger {

		public WaitingDialog(Context context) {
			super(context);
		}

		@Override
		protected void init(Context context) {
			super.init(context);
			this.setWindowAnimations(R.style.DialogerNoAnim);
			this.clearBehind();
			this.requestFill();
			this.setCancelable(false);
			this.setCanceledOnTouchOutside(false);

			RelativeLayout root = new RelativeLayout(context);
			root.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT));
			RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
			iconParams.addRule(RelativeLayout.CENTER_IN_PARENT);
			ProgressIcon icon = new ProgressIcon(context);
			icon.setDrawable(context.getResources().getDrawable(R.drawable.ic_waiting));
			icon.setLayoutParams(iconParams);
			root.addView(icon);
			this.setContentView(root);
		}

		@Override
		public void onBackPressed() {
			Activity activity = AppUtil.toActivity(getContext());
			if (activity != null) {
				activity.onBackPressed();
			} else {
				super.onBackPressed();
			}
		}

	}

	/**
	 * Helper class for waiting layer
	 */
	protected static class WaitingLayerHelper {

		protected Handler mWaitingLayerHandler;

		protected Runnable mWaitingLayerRunnable;

		protected Activitier mContext;

		protected Dialoger mWaitingDialog;

		public WaitingLayerHelper(Activitier context) {
			mContext = context;
			mWaitingDialog = mContext.newWaitingDialog();
			if (mWaitingDialog == null) {
				mWaitingDialog = new WaitingDialog(mContext);
			}
		}

		public void postUpdateWaitingViewState() {
			if (mWaitingLayerHandler == null) {
				mWaitingLayerHandler = new Handler();
				mWaitingLayerRunnable = new Runnable() {

					@Override
					public void run() {
						mWaitingLayerHandler.removeCallbacksAndMessages(null);
						mContext.updateWaitingLayerState();
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
			hide();
		}
	}

	/**
	 * Helper class for pattern layer of gestures password
	 */
	protected static class PatternLayerHelper {

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

		protected Activitier mContext;

		protected PatternDialog mPatternDialog;

		public PatternLayerHelper(Activitier context) {
			mContext = context;
			mPatternDialog = mContext.newPatternDialog();
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
			hide();
		}

		public void onStart() {
			if (mHeartbeatEnable) {
				if (mContext.getApplicationContext() instanceof IPatternDataProvider) {
					((IPatternDataProvider) mContext.getApplicationContext()).isLogined();
				}
				mHeartbeatHandler.postDelayed(mHeartbeatRunnable, 1000);
			}
		}

		public void onResume() {
			if (mHeartbeatEnable) {
				if (mNoPatternDeadLine == 0) {
					mContext.doCheckPattern();
				} else {
					if (mNoPatternDeadLine < System.currentTimeMillis()) {
						mContext.doCheckPattern();
						mNoPatternDeadLine = 0;
					}
				}
				mContext.enablePattern();
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
				if (mContext.mRunning) {
					mHeartbeatHandler.postDelayed(mHeartbeatRunnable, 1000);
				}
			}
		}

		public void doCheck() {
			if (!(mContext.getApplicationContext() instanceof IPatternDataProvider)) {
				return;
			}
			IPatternDataProvider patternDataProvider = (IPatternDataProvider) mContext.getApplicationContext();
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

}

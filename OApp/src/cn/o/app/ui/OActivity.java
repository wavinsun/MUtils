package cn.o.app.ui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cn.o.app.OWrapper;
import cn.o.app.data.AsyncDataQueue;
import cn.o.app.data.IAsyncDataQueue;
import cn.o.app.data.IAsyncDataQueueListener;
import cn.o.app.data.IAsyncDataQueueOwner;
import cn.o.app.data.IAsyncDataTask;
import cn.o.app.event.Dispatcher;
import cn.o.app.event.listener.OnActivityResultListener;
import cn.o.app.net.INetQueue;
import cn.o.app.net.INetQueueListener;
import cn.o.app.net.INetQueueOwner;
import cn.o.app.net.INetTask;
import cn.o.app.net.NetClient.CookieExpiredException;
import cn.o.app.net.NetQueue;
import cn.o.app.queue.IQueue;
import cn.o.app.socket.ISocketQueueOwner;
import cn.o.app.socket.ISocketTask;
import cn.o.app.task.IStopable;
import cn.o.app.task.IStopableManager;
import cn.o.app.ui.core.IActivityResultCatcher;
import cn.o.app.ui.core.IContentViewOwner;
import cn.o.app.ui.core.IFragmentManager;
import cn.o.app.ui.core.IPrivateActivity;
import cn.o.app.ui.core.IStateView;
import cn.o.app.ui.core.IToastOwner;
import cn.o.app.ui.pattern.IPatternDataProvider;
import cn.o.app.ui.pattern.IPatternOwner;
import cn.o.app.ui.pattern.IPatternView;

public class OActivity extends FragmentActivity implements IFragmentManager, INetQueueOwner, IPatternOwner,
		IAsyncDataQueueOwner, IToastOwner, IStopableManager, IActivityResultCatcher, IContentViewOwner {

	protected AsyncDataQueue mAsyncDataQueue;

	protected NetQueue mNetQueue;

	protected WaitingViewHelper mWaitingViewHelper;

	protected boolean mBusy;

	protected List<IStateView> mBindViews;

	protected PatternViewHelper mPatternViewHelper;

	protected List<IStopable> mBindStopables;

	protected OToast mToast;

	protected boolean mRunning;

	protected Toast mSysToast;

	protected Dispatcher mDispatcher;

	public static void finishAll() {
		ActivityMgr.finishAll();
	}

	public Context getContext() {
		return this;
	}

	public void toast(CharSequence s) {
		if (!mRunning) {
			return;
		}
		if (mSysToast == null) {
			mSysToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		}
		if (s == null) {
			mSysToast.cancel();
		} else {
			if (s.equals("")) {
				mSysToast.cancel();
			} else {
				mSysToast.setText(s);
				mSysToast.show();
			}
		}
	}

	public void toast(int resId, Object... args) {
		if (!mRunning) {
			return;
		}
		if (mSysToast == null) {
			mSysToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
		}
		if (resId == 0) {
			mSysToast.cancel();
		} else {
			if (args != null & args.length != 0) {
				mSysToast.setText(getString(resId, args));
			} else {
				mSysToast.setText(resId);
			}
			mSysToast.show();
		}
	}

	public boolean isBusy() {
		return mBusy;
	}

	public void setBusy(boolean busy) {
		mBusy = busy;
		if (mWaitingViewHelper == null) {
			mWaitingViewHelper = new WaitingViewHelper(this);
		}
		mWaitingViewHelper.postUpdateWaitingViewState();
	}

	@Override
	public OToast getToast() {
		return mToast;
	}

	@Override
	public void bind(IStopable stopable) {
		OWrapper.bind(this, stopable);
	}

	@Override
	public void disablePattern(long duration) {
		if (mPatternViewHelper != null) {
			mPatternViewHelper.disable(duration);
		}
	}

	@Override
	public void enablePattern() {
		if (mPatternViewHelper != null) {
			mPatternViewHelper.enable();
		}
	}

	public boolean isRunning() {
		return mRunning;
	}

	public boolean isHeartbeatEnabled() {
		return mPatternViewHelper != null && mPatternViewHelper.isHeartbeatEnabled();
	}

	public void setHeartbeatEnabled(boolean enabled) {
		if (mPatternViewHelper == null) {
			mPatternViewHelper = new PatternViewHelper(this);
		}
		mPatternViewHelper.setHeartbeatEnabled(enabled);
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
		if (mPatternViewHelper != null) {
			mPatternViewHelper.doCheck();
		}
	}

	@Override
	public IPatternView newPattern() {
		return null;
	}

	@Override
	public void showPattern() {
		if (mPatternViewHelper == null) {
			mPatternViewHelper = new PatternViewHelper(this);
		}
		mPatternViewHelper.show();
	}

	@Override
	public void hidePattern() {
		if (mPatternViewHelper == null) {
			return;
		}
		mPatternViewHelper.hide();
	}

	protected void hidePatternNow() {
		if (mPatternViewHelper == null) {
			return;
		}
		mPatternViewHelper.hideNow();
	}

	@Override
	public void bind(IStateView stateView) {
		OWrapper.bind(this, stateView);
	}

	public View getContentView() {
		return OWrapper.getContentView(this);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		this.bindStateViews();
		OWrapper.injectResources(this);
		OWrapper.injectEvents(this);
	}

	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		this.bindStateViews();
		OWrapper.injectResources(this);
		OWrapper.injectEvents(this);
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
		bindStateViews();
		OWrapper.injectResources(this);
		OWrapper.injectEvents(this);
	}

	protected void bindStateViews() {
		OWrapper.bindStateViews(this, this.getWindow().getDecorView());
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);

		ActivityMgr.attach(this);

		OWrapper.injectContentView(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		OWrapper.dispatchStart(this);
		if (mPatternViewHelper != null) {
			mPatternViewHelper.onStart();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		OWrapper.dispatchResume(this);
		if (mPatternViewHelper != null) {
			mPatternViewHelper.onResume();
		}
		mRunning = true;
	}

	@Override
	protected void onPause() {
		mRunning = false;
		OWrapper.dispatchPause(this);
		super.onPause();
	}

	@Override
	protected void onStop() {
		OWrapper.dispatchStop(this);
		if (mPatternViewHelper != null) {
			mPatternViewHelper.onStop();
		}
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		if (mNetQueue != null) {
			mNetQueue.clear();
		}
		if (mAsyncDataQueue != null) {
			mAsyncDataQueue.clear();
		}
		if (mWaitingViewHelper != null) {
			mWaitingViewHelper.onDestroy();
		}
		if (mPatternViewHelper != null) {
			mPatternViewHelper.onDestroy();
		}
		OWrapper.dispatchDestroy(this);
		ActivityMgr.detach(this);
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		if (this.onInterceptBackPressed()) {
			return;
		}
		super.onBackPressed();
	}

	public boolean onInterceptBackPressed() {
		if (OWrapper.interceptBackPressed(this)) {
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
					if (mWaitingViewHelper == null) {
						mWaitingViewHelper = new WaitingViewHelper(OActivity.this);
					}
					mWaitingViewHelper.postUpdateWaitingViewState();
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
					if (mWaitingViewHelper == null) {
						mWaitingViewHelper = new WaitingViewHelper(OActivity.this);
					}
					mWaitingViewHelper.postUpdateWaitingViewState();
				}
			});
		}
		return mAsyncDataQueue;
	}

	protected void updateWaitingViewState() {
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
		if (mWaitingViewHelper == null) {
			mWaitingViewHelper = new WaitingViewHelper(this);
		}
		mWaitingViewHelper.show();
	}

	protected void hideWaiting() {
		if (mWaitingViewHelper == null) {
			return;
		}
		mWaitingViewHelper.hide();
	}

	protected void hideWaitingNow() {
		if (mWaitingViewHelper == null) {
			return;
		}
		mWaitingViewHelper.hideNow();
	}

	protected View getWaitingViewLayout() {
		return null;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		OWrapper.onActivityResult(this, requestCode, resultCode, data);
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		OWrapper.dispatchTouchEvent(ev, this);
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

	public void add(ISocketTask<?, ?> task) {
		this.bind(task);
		Context appContext = this.getApplicationContext();
		if (appContext instanceof ISocketQueueOwner) {
			((ISocketQueueOwner) appContext).getSocketQueue().add(task);
		}
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
	public void finish() {
		hidePatternNow();
		hideWaitingNow();
		super.finish();
	}

	@Override
	public void stopAll() {
		OWrapper.stopAll(this);
	}

	@Override
	public void stopAll(boolean includeLockable) {
		OWrapper.stopAll(this, includeLockable);
	}

	@Override
	public <T extends View> T findViewById(int id, Class<T> viewClass) {
		return OWrapper.findViewById(this, id, viewClass);
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

	@Override
	public IToastOwner getToastOwner() {
		return this;
	}

	/**
	 * Helper class for waiting view
	 */
	protected static class WaitingViewHelper {

		protected boolean mShowing;

		protected FrameLayout mView;

		protected WindowManager.LayoutParams mViewParams;

		protected Animation mFadeInAnim;

		protected Animation mFadeOutAnim;

		protected Handler mWaitingViewHandler;

		protected Runnable mWaitingViewRunnable;

		protected OActivity mContext;

		public WaitingViewHelper(OActivity context) {
			mContext = context;
			mView = new FrameLayout(mContext);
			mViewParams = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.TYPE_APPLICATION,
					WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
					PixelFormat.TRANSPARENT);
			// Used to implement animation
			// The view added by WindowManager can not use animation.
			View virtualWaitingView;
			View waitingViewLayout = mContext.getWaitingViewLayout();
			if (waitingViewLayout != null) {
				virtualWaitingView = waitingViewLayout;
			} else {
				RelativeLayout defaultWaitingView = new RelativeLayout(mContext);
				View makLayer = new View(mContext);
				makLayer.setBackgroundColor(0x30000000);
				defaultWaitingView.addView(makLayer, new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
				ProgressBar progressBar = new ProgressBar(mContext, null, android.R.attr.progressBarStyleLarge);
				RelativeLayout.LayoutParams progressBarParams = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
				progressBarParams.addRule(RelativeLayout.CENTER_IN_PARENT);
				defaultWaitingView.addView(progressBar, progressBarParams);
				virtualWaitingView = defaultWaitingView;
			}
			mView.addView(virtualWaitingView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
					FrameLayout.LayoutParams.MATCH_PARENT));
			mFadeInAnim = new AlphaAnimation(0, 1);
			mFadeInAnim.setDuration(200);
			mFadeInAnim.setFillAfter(true);
			mFadeOutAnim = new AlphaAnimation(1, 0);
			mFadeOutAnim.setDuration(200);
			mFadeOutAnim.setFillAfter(true);
			mFadeOutAnim.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					hideNow();
				}
			});
		}

		public void postUpdateWaitingViewState() {
			if (mWaitingViewHandler == null) {
				mWaitingViewHandler = new Handler();
				mWaitingViewRunnable = new Runnable() {

					@Override
					public void run() {
						mWaitingViewHandler.removeCallbacksAndMessages(null);
						mContext.updateWaitingViewState();
					}
				};
			}
			mWaitingViewHandler.postDelayed(mWaitingViewRunnable, 400);
		}

		public void show() {
			if (mShowing) {
				return;
			}
			View child = mView.getChildAt(0);
			mContext.getWindowManager().addView(mView, mViewParams);
			child.clearAnimation();
			child.startAnimation(mFadeInAnim);
			mShowing = true;
		}

		public void hide() {
			if (!mShowing) {
				return;
			}
			View child = mView.getChildAt(0);
			child.clearAnimation();
			child.startAnimation(mFadeOutAnim);
		}

		public void hideNow() {
			if (!mShowing) {
				return;
			}
			mContext.getWindowManager().removeView(mView);
			mShowing = false;
		}

		public void onDestroy() {
			if (mWaitingViewHandler != null) {
				mWaitingViewHandler.removeCallbacksAndMessages(null);
			}
			hideNow();
		}
	}

	/**
	 * Helper class for pattern view of gestures password
	 */
	protected static class PatternViewHelper {

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

		protected boolean mShowing;

		protected FrameLayout mView;

		protected WindowManager.LayoutParams mViewParams;

		protected Animation mFadeInAnim;

		protected Animation mFadeOutAnim;

		protected OActivity mContext;

		public PatternViewHelper(OActivity context) {
			mContext = context;
			mView = new FrameLayout(mContext);
			mViewParams = new WindowManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.TYPE_APPLICATION,
					WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, PixelFormat.TRANSPARENT);
			View virtualPatternView = (View) mContext.newPattern();
			mView.addView(virtualPatternView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
					FrameLayout.LayoutParams.MATCH_PARENT));
			mFadeInAnim = new AlphaAnimation(0, 1);
			mFadeInAnim.setDuration(200);
			mFadeInAnim.setFillAfter(true);
			mFadeOutAnim = new AlphaAnimation(1, 0);
			mFadeOutAnim.setDuration(200);
			mFadeOutAnim.setFillAfter(true);
			mFadeOutAnim.setAnimationListener(new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					hideNow();
				}
			});
		}

		public void show() {
			if (mShowing) {
				return;
			}
			View child = mView.getChildAt(0);
			((IPatternView) child).refresh();
			mContext.getWindowManager().addView(mView, mViewParams);
			child.clearAnimation();
			child.startAnimation(mFadeInAnim);
			mShowing = true;
		}

		public void hide() {
			if (!mShowing) {
				return;
			}
			View child = mView.getChildAt(0);
			child.clearAnimation();
			child.startAnimation(mFadeOutAnim);
		}

		public void hideNow() {
			if (!mShowing) {
				return;
			}
			mContext.getWindowManager().removeView(mView);
			mShowing = false;
		}

		public void onDestroy() {
			if (mHeartbeatHandler != null) {
				mHeartbeatHandler.removeCallbacksAndMessages(null);
			}
			hideNow();
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

	protected static class ActivityMgr {

		protected static ArrayList<Activity> sActivitys;

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
				sActivitys = new ArrayList<Activity>();
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

}

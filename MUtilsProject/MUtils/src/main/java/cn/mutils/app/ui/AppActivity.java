package cn.mutils.app.ui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.madebycm.hellocordova.AndroidBug5497Workaround;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.mutils.app.R;
import cn.mutils.app.core.event.Dispatcher;
import cn.mutils.app.core.event.listener.VersionUpdateListener;
import cn.mutils.app.core.net.NetClient.CookieExpiredException;
import cn.mutils.app.core.task.IStopable;
import cn.mutils.app.data.AsyncDataQueue;
import cn.mutils.app.data.IAsyncDataQueue;
import cn.mutils.app.data.IAsyncDataQueueListener;
import cn.mutils.app.data.IAsyncDataTask;
import cn.mutils.app.event.listener.OnActivityResultListener;
import cn.mutils.app.net.INetQueue;
import cn.mutils.app.net.INetQueueListener;
import cn.mutils.app.net.INetTask;
import cn.mutils.app.net.NetQueue;
import cn.mutils.app.os.AppActivityManager;
import cn.mutils.app.queue.IQueue;
import cn.mutils.app.ui.core.IActivity;
import cn.mutils.app.ui.core.ISessionHolder;
import cn.mutils.app.ui.core.IStateView;
import cn.mutils.app.ui.core.IToastOwner;
import cn.mutils.app.ui.core.UICore;
import cn.mutils.app.ui.pattern.PatternDialog;
import cn.mutils.app.ui.pattern.PatternLayerHelper;
import cn.mutils.app.ui.util.DoubleBackClickHelper;
import cn.mutils.app.ui.util.WaitingLayerHelper;
import cn.mutils.app.util.JPushHelper;
import cn.mutils.app.util.UmengHelper;

@SuppressLint({ "ShowToast", "InlinedApi" })
public class AppActivity extends FragmentActivity implements IActivity, ISessionHolder {

	protected PatternLayerHelper mPatternLayerHelper;
	protected WaitingLayerHelper mWaitingLayerHelper;
	protected UmengHelper mUmengHelper;
	protected JPushHelper mJHelper;

	protected AsyncDataQueue mAsyncDataQueue;
	protected NetQueue mNetQueue;
	protected boolean mBusy;
	protected boolean mRunning;
	protected boolean mFinished;

	protected List<Runnable> mRunOnceOnResumeList;
	protected Handler mHandler;

	protected List<IStateView> mBindViews;
	protected List<IStopable> mBindStopables;
	protected Dispatcher mDispatcher;

	protected InfoToast mInfoToast;
	protected Toast mToast;

	protected boolean mStatusBarTranslucent;
	protected StatusBox mStatusBox;
	protected RelativeLayout mTitleBox;
	protected TextView mTitleBoxName;
	protected ImageView mTitleBoxBackButton;

	protected DoubleBackClickHelper mDoubleBackClickHelper;

	public boolean isDoubleBackClickEnabled() {
		return mDoubleBackClickHelper == null ? false : mDoubleBackClickHelper.isEnabled();
	}

	public void setDoubleBackClickEnabled(boolean enabled) {
		if (mDoubleBackClickHelper == null) {
			mDoubleBackClickHelper = new DoubleBackClickHelper(this);
		}
		mDoubleBackClickHelper.setEnabled(enabled);
	}

	protected void onClickTitleBoxBackBtn() {
		finish();
	}

	public static boolean exists(Class<? extends Activity> activityCls) {
		return AppActivityManager.exists(activityCls);
	}

	public static void redirectTo(Class<? extends Activity> activityCls) {
		AppActivityManager.redirectTo(activityCls);
	}

	public static void finishAll() {
		AppActivityManager.finishAll();
	}

	public Context getContext() {
		return this;
	}

	@Override
	public Handler getMainHandler() {
		if (mHandler == null) {
			mHandler = new Handler(Looper.getMainLooper());
		}
		return mHandler;
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
		onSetContentView();
	}

	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		onSetContentView();
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
		onSetContentView();
	}

	protected void bindStateViews() {
		UICore.bindStateViews(this, this.getWindow().getDecorView());
	}

	protected void onSetContentView() {
		UICore.bindStateViews(this, this.getWindow().getDecorView());
		UICore.injectResources(this);
		UICore.injectEvents(this);
		fixAndroidBug5497();

		if (mStatusBox == null) {
			mStatusBox = findViewById(R.id.status_box, StatusBox.class);
		}
		if (mTitleBox == null) {
			mTitleBox = findViewById(R.id.title_box, RelativeLayout.class);
		}
		if (mTitleBoxName == null) {
			mTitleBoxName = findViewById(R.id.title_box_name, TextView.class);
		}
		if (mTitleBoxBackButton == null) {
			mTitleBoxBackButton = findViewById(R.id.title_box_back, ImageView.class);
			if (mTitleBoxBackButton != null) {
				mTitleBoxBackButton.setOnClickListener(new OnClickTitleBoxBackButtonListener());
			}
		}
	}

	@Override
	public boolean isStatusBarTranslucent() {
		return mStatusBarTranslucent;
	}

	public boolean onInterceptSetSoftInputMode() {
		return false;
	}

	/**
	 * Fix bug for android bug 5497<br>
	 * Keyboard open but content view size is not changed while
	 * SOFT_INPUT_ADJUST_RESIZE
	 */
	protected void fixAndroidBug5497() {
		if (mStatusBarTranslucent && (getWindow().getAttributes().softInputMode
				& WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE) == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE) {
			try {
				AndroidBug5497Workaround.assistActivity(this);
			} catch (Exception e) {

			}
		}
	}

	@Override
	public boolean isSessionHolder() {
		return false;
	}

	@Override
	public void validateSession() {

	}

	@Override
	public boolean hasSession() {
		return false;
	}

	@Override
	public boolean isSessionChanged() {
		return false;
	}

	@Override
	public void onSessionChanged() {

	}

	public void runOnceOnResume(Runnable r) {
		if (mRunOnceOnResumeList == null) {
			mRunOnceOnResumeList = new ArrayList<Runnable>();
		}
		mRunOnceOnResumeList.add(r);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Window w = this.getWindow();
		w.requestFeature(Window.FEATURE_NO_TITLE);
		if (!this.onInterceptSetSoftInputMode()) {
			w.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		}
		if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
			w.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			mStatusBarTranslucent = true;
		}

		super.onCreate(savedInstanceState);
		AppActivityManager.attach(this);
		UICore.injectContentView(this);

		mRunning = true;
		mUmengHelper = new UmengHelper();
		mJHelper = new JPushHelper();
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
		mUmengHelper.delegate().onResume(this);
		mJHelper.delegate().onResume(this);
		// Validate session or user login state
		if (this.isSessionHolder()) {
			this.validateSession();
			if (this.isSessionChanged()) {
				getMainHandler().post(new OnSessionChangedRunnable());
			}
		}
		if (mRunOnceOnResumeList != null) {
			for (Runnable r : mRunOnceOnResumeList) {
				r.run();
			}
			mRunOnceOnResumeList.clear();
		}
	}

	@Override
	protected void onPause() {
		mUmengHelper.delegate().onPause(this);
		mJHelper.delegate().onPause(this);
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
		if (mRunOnceOnResumeList != null) {
			mRunOnceOnResumeList.clear();
		}
		if (mHandler != null) {
			mHandler.removeCallbacksAndMessages(null);
		}
		mUmengHelper.delegate().onDestroy(this);
		if (mWaitingLayerHelper != null) {
			mWaitingLayerHelper.onDestroy();
		}
		if (mPatternLayerHelper != null) {
			mPatternLayerHelper.onDestroy();
		}
		UICore.dispatchDestroy(this);
		AppActivityManager.detach(this);
	}

	@Override
	public void onBackPressed() {
		if (this.onInterceptBackPressed()) {
			return;
		}
		try {
			super.onBackPressed();
		} catch (Exception e) {
			// java.lang.IllegalStateException
		}
	}

	public boolean onInterceptBackPressed() {
		if (UICore.interceptBackPressed(this)) {
			return true;
		}
		if (mBusy) {
			this.setBusy(false);
			return true;
		}
		if (this.isDoubleBackClickEnabled()) {
			if (mDoubleBackClickHelper.onInterceptBackPressed()) {
				return true;
			}
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
						mWaitingLayerHelper = new WaitingLayerHelper(AppActivity.this);
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
						mWaitingLayerHelper = new WaitingLayerHelper(AppActivity.this);
					}
					mWaitingLayerHelper.postUpdateWaitingViewState();
				}
			});
		}
		return mAsyncDataQueue;
	}

	public void updateWaitingLayerState() {
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

	public Dialoger newWaitingDialog() {
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
			if (this.isSessionHolder()) {
				this.validateSession();
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

	public boolean hasNewVersion() {
		return mUmengHelper.delegate().hasNewVersion(this);
	}

	public void checkNewVersion(VersionUpdateListener listener) {
		mUmengHelper.delegate().checkNewVersion(this,listener);
	}

	public void feedback() {
		mUmengHelper.delegate().feedback(this);
	}

	class OnClickTitleBoxBackButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			onClickTitleBoxBackBtn();
		}

	}

	class OnSessionChangedRunnable implements Runnable {

		@Override
		public void run() {
			onSessionChanged();
		}

	}

}

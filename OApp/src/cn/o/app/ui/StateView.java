package cn.o.app.ui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cn.o.app.core.event.Dispatcher;
import cn.o.app.core.net.NetClient.CookieExpiredException;
import cn.o.app.core.task.IStopable;
import cn.o.app.core.task.IStopableManager;
import cn.o.app.data.IAsyncDataQueueOwner;
import cn.o.app.data.IAsyncDataTask;
import cn.o.app.event.listener.OnActivityResultListener;
import cn.o.app.net.INetQueueOwner;
import cn.o.app.net.INetTask;
import cn.o.app.task.DelayTask;
import cn.o.app.ui.core.IContentViewOwner;
import cn.o.app.ui.core.IPrivateView;
import cn.o.app.ui.core.IStateView;
import cn.o.app.ui.core.IStateViewManager;
import cn.o.app.ui.core.IToastOwner;
import cn.o.app.ui.core.UICore;

@SuppressLint("ShowToast")
public class StateView extends RelativeLayout
		implements IStateView, IStateViewManager, IStopableManager, IToastOwner, IContentViewOwner {

	protected List<IStateView> mBindViews;
	protected List<IStopable> mBindStopables;

	protected InfoToast mInfoToast;

	protected Toast mToast;

	protected boolean mCreateDispatched;

	protected IStateViewManager mManager;

	protected Dispatcher mDispatcher;

	public StateView(Context context) {
		super(context);
	}

	public StateView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public StateView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public IStateViewManager getManager() {
		return mManager;
	}

	@Override
	public void setManager(IStateViewManager manager) {
		this.mManager = manager;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		bindStateViews();
	}

	protected void bindStateViews() {
		UICore.bindStateViews(this, this);
	}

	@Override
	public void bind(IStateView stateView) {
		UICore.bind(this, stateView);
	}

	public void bind(IStopable stopable) {
		UICore.bind(this, stopable);
	}

	@Override
	public void onCreate() {
		UICore.injectContentView(this);
	}

	@Override
	public void onStart() {
		UICore.dispatchStart(this);
	}

	@Override
	public void onResume() {
		UICore.dispatchResume(this);
	}

	@Override
	public void onPause() {
		UICore.dispatchPause(this);
	}

	@Override
	public void onStop() {
		UICore.dispatchStop(this);
	}

	@Override
	public void onDestroy() {
		UICore.dispatchDestroy(this);
	}

	@Override
	public boolean onInterceptBackPressed() {
		if (UICore.interceptBackPressed(this)) {
			return true;
		}
		return false;
	}

	public boolean onInterceptGesture() {
		return false;
	}

	public boolean onGestureTouch(View v, MotionEvent event) {
		if (onInterceptGesture()) {
			return false;
		}
		ViewParent parent = getParent();
		if (parent != null) {
			if (parent instanceof StateViewFlipper) {
				return ((StateViewFlipper) parent).getGestureListener().onTouch(v, event);
			}
		}
		return false;
	}

	public void startActivity(Intent intent) {
		UICore.startActivity(this, intent);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		UICore.startActivityForResult(this, intent, requestCode);
	}

	@Override
	public void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
		UICore.onActivityResult(this, requestCode, resultCode, data);
	}

	public Context getApplicationContext() {
		return getContext().getApplicationContext();
	}

	public void start(DelayTask delayedTask) {
		this.bind(delayedTask.start());
	}

	public void add(INetTask<?, ?> task) {
		Context context = this.getContext();
		if (context instanceof INetQueueOwner) {
			this.bind(task);
			((INetQueueOwner) context).getNetQueue().add(task);
		}
	}

	public void add(IAsyncDataTask<?> task) {
		Context context = this.getContext();
		if (context instanceof IAsyncDataQueueOwner) {
			this.bind(task);
			((IAsyncDataQueueOwner) context).getAsyncDataQueue().add(task);
		}
	}

	public void setContentView(int layoutRes) {
		LayoutInflater.from(getContext()).inflate(layoutRes, this);
		bindStateViews();
		UICore.injectResources(this);
		UICore.injectEvents(this);
	}

	public void setContentView(View view) {
		this.addView(view);
		bindStateViews();
		UICore.injectResources(this);
		UICore.injectEvents(this);
	}

	@Override
	public void notify(Object message) {
		if (message == null) {
			return;
		}
		if (message instanceof CookieExpiredException) {
			if (this instanceof IPrivateView) {
				((IPrivateView) this).refresh();
			}
		}
		if (mManager != null) {
			mManager.notify(message);
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
	public boolean isCreateDispatched() {
		return mCreateDispatched;
	}

	@Override
	public void setCreateDispatched(boolean dispatched) {
		mCreateDispatched = dispatched;
	}

	public InfoToast getInfoToast() {
		return mInfoToast;
	}

	public Toast getToast() {
		Context context = this.getContext();
		if (context instanceof IToastOwner) {
			return ((IToastOwner) context).getToast();
		}
		if (mToast == null) {
			mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
		}
		return mToast;
	}

	@Override
	public void toast(CharSequence s) {
		UICore.toast(this, s);
	}

	@Override
	public void toast(int resId, Object... args) {
		UICore.toast(this, resId, args);
	}

	@Override
	public IToastOwner getToastOwner() {
		return this;
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

}

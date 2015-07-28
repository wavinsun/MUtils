package cn.o.app.ui;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.RelativeLayout;
import cn.o.app.OWrapper;
import cn.o.app.data.IAsyncDataQueueOwner;
import cn.o.app.data.IAsyncDataTask;
import cn.o.app.event.Dispatcher;
import cn.o.app.event.listener.OnActivityResultListener;
import cn.o.app.net.INetQueueOwner;
import cn.o.app.net.INetTask;
import cn.o.app.net.NetClient.CookieExpiredException;
import cn.o.app.task.DelayTask;
import cn.o.app.task.IStopable;
import cn.o.app.task.IStopableManager;
import cn.o.app.ui.core.IContentViewOwner;
import cn.o.app.ui.core.IPrivateView;
import cn.o.app.ui.core.IStateView;
import cn.o.app.ui.core.IStateViewManager;
import cn.o.app.ui.core.IToastOwner;

public class StateView extends RelativeLayout
		implements IStateView, IStateViewManager, IStopableManager, IToastOwner, IContentViewOwner {

	protected List<IStateView> mBindViews;
	protected List<IStopable> mBindStopables;

	protected OToast mToast;

	protected boolean mCreateDispatched;

	protected IStateViewManager mManager;

	protected Dispatcher mDispatcher = new Dispatcher();

	public StateView(Context context) {
		super(context);
	}

	public StateView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public StateView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public OToast getToast() {
		return mToast;
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
		OWrapper.bindStateViews(this, this);
	}

	@Override
	public void bind(IStateView stateView) {
		OWrapper.bind(this, stateView);
	}

	public void bind(IStopable stopable) {
		OWrapper.bind(this, stopable);
	}

	@Override
	public void onCreate() {
		OWrapper.injectContentView(this);
	}

	@Override
	public void onStart() {
		OWrapper.dispatchStart(this);
	}

	@Override
	public void onResume() {
		OWrapper.dispatchResume(this);
	}

	@Override
	public void onPause() {
		OWrapper.dispatchPause(this);
	}

	@Override
	public void onStop() {
		OWrapper.dispatchStop(this);
	}

	@Override
	public void onDestroy() {
		OWrapper.dispatchDestroy(this);
	}

	@Override
	public boolean onInterceptBackPressed() {
		if (OWrapper.interceptBackPressed(this)) {
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
		OWrapper.startActivity(this, intent);
	}

	@Override
	public void startActivityForResult(Intent intent, int requestCode) {
		OWrapper.startActivityForResult(this, intent, requestCode);
	}

	@Override
	public void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
		OWrapper.onActivityResult(this, requestCode, resultCode, data);
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
		OWrapper.injectResources(this);
		OWrapper.injectEvents(this);
	}

	public void setContentView(View view) {
		this.addView(view);
		bindStateViews();
		OWrapper.injectResources(this);
		OWrapper.injectEvents(this);
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
	public boolean isCreateDispatched() {
		return mCreateDispatched;
	}

	@Override
	public void setCreateDispatched(boolean dispatched) {
		mCreateDispatched = dispatched;
	}

	@Override
	public void toast(CharSequence s) {
		Context context = this.getContext();
		if (context instanceof IToastOwner) {
			((IToastOwner) context).toast(s);
		}
	}

	@Override
	public void toast(int resId, Object... args) {
		Context context = this.getContext();
		if (context instanceof IToastOwner) {
			((IToastOwner) context).toast(resId, args);
		}
	}

	@Override
	public List<OnActivityResultListener> getOnActivityResultListeners() {
		return mDispatcher.getListeners(OnActivityResultListener.EVENT_TYPE, OnActivityResultListener.class);
	}

	@Override
	public void addOnActivityResultListener(OnActivityResultListener listener) {
		mDispatcher.addListener(OnActivityResultListener.EVENT_TYPE, listener);
	}

	@Override
	public void removeOnActivityResultListener(OnActivityResultListener listener) {
		mDispatcher.removeListener(OnActivityResultListener.EVENT_TYPE, listener);
	}

	@Override
	public IToastOwner getToastOwner() {
		return this;
	}

}

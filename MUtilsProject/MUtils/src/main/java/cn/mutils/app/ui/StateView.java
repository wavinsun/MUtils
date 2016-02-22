package cn.mutils.app.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import cn.mutils.app.data.IAsyncDataQueueOwner;
import cn.mutils.app.data.IAsyncDataTask;
import cn.mutils.app.event.listener.OnActivityResultListener;
import cn.mutils.app.net.INetQueueOwner;
import cn.mutils.app.net.INetTask;
import cn.mutils.app.os.IHandlerProvider;
import cn.mutils.app.task.DelayTask;
import cn.mutils.app.ui.core.IContentViewOwner;
import cn.mutils.app.ui.core.IRunOnceHolder;
import cn.mutils.app.ui.core.ISessionHolder;
import cn.mutils.app.ui.core.IStateView;
import cn.mutils.app.ui.core.IStateViewManager;
import cn.mutils.app.ui.core.IToastOwner;
import cn.mutils.app.ui.core.UICore;
import cn.mutils.core.err.CookieExpiredException;
import cn.mutils.core.event.Dispatcher;
import cn.mutils.core.task.IStoppable;
import cn.mutils.core.task.IStoppableManager;

@SuppressWarnings({"RedundantIfStatement", "unused"})
@SuppressLint("ShowToast")
public class StateView extends RelativeLayout implements IStateView, ISessionHolder, IRunOnceHolder, IHandlerProvider,
        IStateViewManager, IStoppableManager, IToastOwner, IContentViewOwner {

    protected IStateViewManager mManager;
    protected List<IStateView> mBindViews;
    protected List<IStoppable> mBindStoppables;
    protected Dispatcher mDispatcher;

    protected InfoToast mInfoToast;
    protected Toast mToast;

    protected boolean mCreateDispatched;

    protected List<Runnable> mRunOnceOnResumeList;
    protected Handler mHandler;

    public StateView(Context context) {
        super(context);
        init(context, null);
    }

    public StateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public StateView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {

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

    public void bind(IStoppable stoppable) {
        UICore.bind(this, stoppable);
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
    public void onPause() {
        UICore.dispatchPause(this);
    }

    @Override
    public void onStop() {
        UICore.dispatchStop(this);
    }

    @Override
    public void onDestroy() {
        if (mRunOnceOnResumeList != null) {
            mRunOnceOnResumeList.clear();
        }
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
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
            if (this.isSessionHolder()) {
                this.validateSession();
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
    public List<IStoppable> getBindStoppables() {
        if (mBindStoppables == null) {
            mBindStoppables = new LinkedList<IStoppable>();
        }
        return mBindStoppables;
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
    public Handler getMainHandler() {
        if (mHandler == null) {
            mHandler = new Handler(Looper.getMainLooper());
        }
        return mHandler;
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

    class OnSessionChangedRunnable implements Runnable {

        @Override
        public void run() {
            onSessionChanged();
        }

    }

}

package cn.o.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import cn.o.app.core.event.Dispatcher;
import cn.o.app.event.listener.OnActivityResultListener;
import cn.o.app.event.listener.OnSelectedChangeListener;
import cn.o.app.ui.core.ICachedViewManager;
import cn.o.app.ui.core.IFragment;
import cn.o.app.ui.core.IStateView;
import cn.o.app.ui.core.IStateViewManager;
import cn.o.app.ui.core.UICore;

public class FragmentFrame extends FrameLayout implements IStateView, IStateViewManager, ICachedViewManager {

	protected boolean mCreateDispatched;

	protected List<View> mCachedViews = new ArrayList<View>();

	/** -1 for default */
	protected int mSelectedIndex = -1;

	protected OnSelectedChangeListener mOnSelectedChangeListener;

	protected IStateViewManager mManager;

	protected Dispatcher mDispatcher = new Dispatcher();

	public FragmentFrame(Context context) {
		super(context);
	}

	public FragmentFrame(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FragmentFrame(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	protected void setDisplayedChild(int whichChild) {
		if (whichChild >= getChildCount()) {
			whichChild = 0;
		} else if (whichChild < 0) {
			whichChild = getChildCount() - 1;
		}
		boolean hasFocus = getFocusedChild() != null;
		showOnly(whichChild);
		if (hasFocus) {
			requestFocus(FOCUS_FORWARD);
		}
	}

	protected void showOnly(int childIndex) {
		final int count = getChildCount();
		for (int i = 0; i < count; i++) {
			final View child = getChildAt(i);
			if (i == childIndex) {
				if (child.getVisibility() != View.VISIBLE) {
					child.setVisibility(View.VISIBLE);
				}
			} else {
				if (child.getAnimation() != null) {
					child.clearAnimation();
				}
				if (child.getVisibility() != View.GONE) {
					child.setVisibility(View.GONE);
				}
			}
		}
	}

	public void setOnSelectedChangeListener(OnSelectedChangeListener listener) {
		mOnSelectedChangeListener = listener;
	}

	@Override
	public IStateView getSelectedView() {
		if (mSelectedIndex >= 0 && mSelectedIndex < mCachedViews.size()) {
			View v = mCachedViews.get(mSelectedIndex);
			if (v instanceof IStateView) {
				return (IStateView) v;
			}
		}
		return null;
	}

	public int getSelectedIndex() {
		return this.mSelectedIndex;
	}

	public void setSelectedIndex(int selectedIndex) {
		if (!mCreateDispatched) {
			this.onCreate();
		}
		if (selectedIndex < 0 || selectedIndex >= this.mCachedViews.size()) {
			return;
		}
		if (selectedIndex == mSelectedIndex) {
			return;
		}

		// first time
		if (mSelectedIndex == -1) {
			mSelectedIndex = selectedIndex;
			View v = mCachedViews.get(mSelectedIndex);
			afterSelectedFirstTime();
			this.setDisplayedChild(this.indexOfChild(v));
			return;
		}

		// preSelecting
		View view = null;
		if (mSelectedIndex >= 0) {
			view = mCachedViews.get(mSelectedIndex);
			if (view instanceof IStateView) {
				if (view instanceof IFragment) {
					((IFragment) view).setLocked(true);
				}
				((IStateView) view).onPause();
				((IStateView) view).onStop();
			}
		}
		view = mCachedViews.get(selectedIndex);
		if (view instanceof IStateView) {
			if (view instanceof IFragment) {
				((IFragment) view).setLocked(false);
			}
			((IStateView) view).onStart();
		}
		if (this.indexOfChild(view) < 0) {
			this.addView(view);
		}
		setDisplayedChild(this.indexOfChild(view));

		// post selecting
		if (view instanceof IStateView) {
			if (view instanceof IFragment) {
				((IFragment) view).setLocked(false);
			}
			((IStateView) view).onResume();
		}
		mSelectedIndex = selectedIndex;
		afterSelected();
		if (mOnSelectedChangeListener != null) {
			mOnSelectedChangeListener.onChanged(this, mSelectedIndex);
		}
	}

	protected void afterSelectedFirstTime() {

	}

	protected void afterSelected() {
		View view = mCachedViews.get(mSelectedIndex);
		for (int i = this.getChildCount() - 1; i >= 0; i--) {
			View childView = this.getChildAt(i);
			if (childView.equals(view)) {
				continue;
			}
			if (childView instanceof IFragment) {
				IFragment fragment = (IFragment) childView;
				fragment.setLocked(false);
				fragment.setFragmentVisible(false);
			}
		}
	}

	protected void afterCreated() {

	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		for (int i = 0, size = this.getChildCount(); i < size; i++) {
			View v = this.getChildAt(i);
			mCachedViews.add(v);
			if (v instanceof IStateView) {
				this.bind((IStateView) v);
			}
		}
	}

	@Override
	public void onCreate() {
		if (mCreateDispatched) {
			return;
		}
		mCreateDispatched = true;
		for (View v : mCachedViews) {
			if (v instanceof IStateView) {
				UICore.dispatchCreate((IStateView) v);
			}
		}
		afterCreated();
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
		for (View v : mCachedViews) {
			if (v instanceof IStateView) {
				((IStateView) v).onDestroy();
			}
		}
	}

	@Override
	public boolean onInterceptBackPressed() {
		if (UICore.interceptBackPressed(this)) {
			return true;
		}
		return false;
	}

	@Override
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

	@Override
	public IStateViewManager getManager() {
		return mManager;
	}

	@Override
	public void setManager(IStateViewManager manager) {
		mManager = manager;
	}

	@Override
	public void bind(IStateView stateView) {
		stateView.setManager(this);
	}

	@Override
	public void notify(Object message) {
		if (mManager != null) {
			mManager.notify(message);
		}
	}

	@Override
	public <T extends View> T findViewById(int id, Class<T> viewClass) {
		return UICore.findViewById(this, id, viewClass);
	}

	@Override
	public List<IStateView> getBindStateViews() {
		return null;
	}

	@Override
	public boolean redirectToSelectedView() {
		return true;
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
	public List<View> getCachedViews() {
		return mCachedViews;
	}

}

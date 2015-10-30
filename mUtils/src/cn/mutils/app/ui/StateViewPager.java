package cn.mutils.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import cn.mutils.app.core.event.Dispatcher;
import cn.mutils.app.event.listener.OnActivityResultListener;
import cn.mutils.app.event.listener.OnSelectedChangeListener;
import cn.mutils.app.ui.core.ICachedViewManager;
import cn.mutils.app.ui.core.IStateView;
import cn.mutils.app.ui.core.IStateViewManager;
import cn.mutils.app.ui.core.UICore;

public class StateViewPager extends ViewPager implements IStateView, IStateViewManager, ICachedViewManager {

	protected boolean mCreateDispatched;

	protected List<View> mCachedViews = new ArrayList<View>();

	/** -1 for default */
	protected int mSelectedIndex = -1;

	protected OnSelectedChangeListener mOnSelectedChangeListener;

	protected IStateViewManager mManager;

	protected Dispatcher mDispatcher = new Dispatcher();

	public StateViewPager(Context context) {
		super(context);
	}

	public StateViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
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
		return mSelectedIndex;
	}

	@Override
	public void setCurrentItem(int item, boolean smoothScroll) {
		this.setCurrentItem(item);
	}

	@Override
	public void setCurrentItem(int item) {
		this.setSelectedIndex(item);
	}

	public void setSelectedIndex(int selectedIndex) {
		if (!mCreateDispatched) {
			this.onCreate();
		}
		if (selectedIndex < 0 || selectedIndex >= mCachedViews.size()) {
			return;
		}
		if (mSelectedIndex == selectedIndex) {
			return;
		}
		if (mSelectedIndex != -1) {
			View view = mCachedViews.get(mSelectedIndex);
			if (view instanceof IStateView) {
				((IStateView) view).onPause();
				((IStateView) view).onStop();
			}
			view = mCachedViews.get(selectedIndex);
			if (view instanceof IStateView) {
				((IStateView) view).onStart();
				((IStateView) view).onResume();
			}
			if (mOnSelectedChangeListener != null) {
				mOnSelectedChangeListener.onChanged(this, selectedIndex);
			}
		}
		mSelectedIndex = selectedIndex;
		super.setCurrentItem(mSelectedIndex);
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
		if (this.mCreateDispatched) {
			return;
		}
		this.mCreateDispatched = true;
		for (View v : mCachedViews) {
			if (v instanceof IStateView) {
				UICore.dispatchCreate((IStateView) v);
			}
		}
		this.removeAllViews();
		this.setAdapter(new OStateViewPagerAdapter());
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
		for (View view : this.mCachedViews) {
			if (view instanceof IStateView) {
				((IStateView) view).onDestroy();
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

	class OStateViewPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mCachedViews.size();
		}

		@Override
		public boolean isViewFromObject(View v, Object o) {
			return v.equals(o);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			View v = mCachedViews.get(position);
			if (indexOfChild(v) != -1) {
				removeView(v);
			}
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View v = mCachedViews.get(position);
			if (indexOfChild(v) == -1) {
				addView(v);
			}
			return v;
		}

		@Override
		public void setPrimaryItem(ViewGroup container, int position, Object object) {
			setSelectedIndex(position);
		}

	}

}

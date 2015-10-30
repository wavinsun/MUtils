package cn.mutils.app.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.mutils.app.ui.core.IFragmenter;
import cn.mutils.app.ui.core.IFragmenterManager;
import cn.mutils.app.ui.core.IStateView;
import cn.mutils.app.ui.core.UICore;

@SuppressWarnings("unchecked")
public class Fragmenter extends StateView implements IFragmenter, IFragmenterManager {

	protected FragmentManager mSupportFragmentManagerFromParent;

	protected FragmentWrapper mFragment;

	protected View mContentView;

	protected boolean mFragmentVisible = false;

	protected boolean mLocked;

	public Fragmenter(Context context) {
		super(context);
	}

	public Fragmenter(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public Fragmenter(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	protected void checkValidId() {
		if (this.getId() == View.NO_ID) {
			throw new UnsupportedOperationException("Fragmenter must has id");
		}
	}

	@Override
	public void setLocked(boolean locked) {
		mLocked = locked;
	}

	@Override
	public boolean isLocked() {
		return mLocked;
	}

	@Override
	public boolean isFragmentVisible() {
		return mFragmentVisible;
	}

	@Override
	public void setFragmentVisible(boolean visible) {
		if (mLocked) {
			return;
		}
		if (mFragmentVisible == visible) {
			return;
		}
		FragmentManager fm = this.getSupportFragmentManagerFromParent();
		FragmentTransaction ft = fm.beginTransaction();
		if (visible) {
			ft.show(mFragment);
		} else {
			ft.hide(mFragment);
		}
		ft.commitAllowingStateLoss();
		mFragmentVisible = visible;
	}

	@Override
	public void onStart() {
		super.onStart();
		setFragmentVisible(true);
	}

	@Override
	public void onStop() {
		setFragmentVisible(false);
		super.onStop();
	}

	@Override
	public void onDestroy() {
		FragmentManager fm = this.getSupportFragmentManagerFromParent();
		FragmentTransaction ft = fm.beginTransaction();
		ft.remove(mFragment);
		ft.commitAllowingStateLoss();
		super.onDestroy();
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		if (this.getChildCount() != 0) {
			throw new UnsupportedOperationException("Fragmenter must be empty in layout Resource");
		}
	}

	public void setContentView(View v) {
		if (mContentView != null || mFragment != null) {
			throw new UnsupportedOperationException();
		}
		checkValidId();

		mContentView = v;
		bindStateViews();

		mFragment = new FragmentWrapper();
		mFragment.setContentView(mContentView);

		FragmentManager fm = this.getSupportFragmentManagerFromParent();
		FragmentTransaction ft = fm.beginTransaction();
		ft.add(this.getId(), mFragment);
		ft.hide(mFragment);
		ft.commitAllowingStateLoss();

		UICore.injectResources(this);
		UICore.injectEvents(this);
	}

	@Override
	public void setContentView(int layoutRes) {
		LayoutInflater inflater = LayoutInflater.from(getContext());
		setContentView(inflater.inflate(layoutRes, this, false));
	}

	@Override
	protected void bindStateViews() {
		if (mContentView == null) {
			return;
		}
		UICore.bindStateViews(this, mContentView);
	}

	@Override
	public <T extends View> T findViewById(int id, Class<T> viewClass) {
		if (this.getId() == id) {
			if (viewClass != null) {
				if (viewClass.isInstance(this)) {
					return (T) this;
				} else {
					return null;
				}
			}
			return (T) this;
		}
		return UICore.findViewById(mContentView, id, viewClass);
	}

	@Override
	public FragmentManager getSupportFragmentManager() {
		return mFragment.getChildFragmentManager();
	}

	@Override
	public FragmentManager getSupportFragmentManagerFromParent() {
		if (mSupportFragmentManagerFromParent == null) {
			Object o = this.getManager();
			while (o != null) {
				if (o instanceof IFragmenterManager) {
					mSupportFragmentManagerFromParent = ((IFragmenterManager) o).getSupportFragmentManager();
				}
				if (o instanceof IStateView) {
					o = ((IStateView) o).getManager();
				} else {
					break;
				}
			}
			Context context = this.getContext();
			if (context instanceof FragmentActivity) {
				mSupportFragmentManagerFromParent = ((FragmentActivity) context).getSupportFragmentManager();
			} else {
				throw new UnsupportedOperationException();
			}
		}
		return mSupportFragmentManagerFromParent;
	}

	public static class FragmentWrapper extends Fragment {

		protected View mContentView;

		public void setContentView(View contentView) {
			mContentView = contentView;
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			if (mContentView == null) {
				return new View(getActivity());// fix NullPointerException
			}
			return mContentView;
		}

		@Override
		public void onSaveInstanceState(Bundle outState) {
			setUserVisibleHint(true);
			outState.putString("FragmentWrapper", "onSaveInstanceState");
			super.onSaveInstanceState(outState);
		}

	}

}

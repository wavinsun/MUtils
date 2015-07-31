package cn.o.app.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ViewFlipper;
import cn.o.app.OUtil;
import cn.o.app.event.Dispatcher;
import cn.o.app.event.listener.OnActivityResultListener;
import cn.o.app.event.listener.OnSelectedChangeListener;
import cn.o.app.ui.core.ICachedViewManager;
import cn.o.app.ui.core.IFragment;
import cn.o.app.ui.core.IStateView;
import cn.o.app.ui.core.IStateViewManager;
import cn.o.app.ui.core.UICore;

@SuppressLint("ClickableViewAccessibility")
public class FragmentFlipper extends ViewFlipper implements IStateView, IStateViewManager, ICachedViewManager {

	protected Handler mAnimHandler;

	protected boolean mCreateDispatched;

	protected List<View> mCachedViews = new ArrayList<View>();

	protected Animation mAnimPushLeftIn;

	protected Animation mAnimPushLeftOut;

	protected Animation mAnimPushRightIn;

	protected Animation mAnimPushRightOut;

	protected AnimationListener mAnimPushListener;

	protected boolean mAnimEnabled = true;

	/** -1 for default,-2 for flipping */
	protected int mSelectedIndex = -1;

	protected int mTargetIndex = -1;

	protected OnSelectedChangeListener mOnSelectedChangeListener;

	protected boolean mGestureDetectorEnabled;

	protected GestureDetector mGestureDetector;

	protected int mMinDistance;

	protected int mMinVelocity;

	protected OnGestureListener mGestureDetectorOnGestureListener;

	protected OnTouchListener mOnTouchListener;

	protected IStateViewManager mManager;

	protected Dispatcher mDispatcher = new Dispatcher();

	public FragmentFlipper(Context context) {
		super(context);
	}

	public FragmentFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setAnimationEnable(boolean enable) {
		this.mAnimEnabled = enable;
		if (mAnimEnabled) {
			if (mAnimPushLeftIn == null) {
				this.setAnimation(0, 0, 0, 0);
			}
		}
	}

	public void setAnimation(int pushLeftIn, int pushLeftOut, int pushRightIn, int pushRightOut) {
		mAnimEnabled = true;
		Context context = this.getContext();
		if (pushLeftIn == 0) {
			mAnimPushLeftIn = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 1, Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
			mAnimPushLeftIn.setDuration(300);
		} else {
			mAnimPushLeftIn = AnimationUtils.loadAnimation(context, pushLeftIn);
		}
		if (pushLeftOut == 0) {
			mAnimPushLeftOut = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, -1,
					Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
			mAnimPushLeftOut.setDuration(300);
		} else {
			mAnimPushLeftOut = AnimationUtils.loadAnimation(context, pushLeftOut);
		}
		if (pushRightIn == 0) {
			mAnimPushRightIn = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1, Animation.RELATIVE_TO_SELF, 0,
					Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
			mAnimPushRightIn.setDuration(300);
		} else {
			mAnimPushRightIn = AnimationUtils.loadAnimation(context, pushRightIn);
		}
		if (pushRightOut == 0) {
			mAnimPushRightOut = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 1,
					Animation.RELATIVE_TO_SELF, 0, Animation.RELATIVE_TO_SELF, 0);
			mAnimPushRightOut.setDuration(300);
		} else {
			mAnimPushRightOut = AnimationUtils.loadAnimation(context, pushRightOut);
		}

		if (mAnimPushListener == null) {
			mAnimPushListener = new AnimationListener() {

				@Override
				public void onAnimationStart(Animation animation) {

				}

				@Override
				public void onAnimationRepeat(Animation animation) {

				}

				@Override
				public void onAnimationEnd(Animation animation) {
					if (mAnimHandler == null) {
						mAnimHandler = new Handler();
					}
					mAnimHandler.post(new Runnable() {

						@Override
						public void run() {
							postSelecting();
						}
					});
				}
			};
		}
		mAnimPushLeftIn.setAnimationListener(mAnimPushListener);
		mAnimPushRightIn.setAnimationListener(mAnimPushListener);
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
		if (selectedIndex == mSelectedIndex || selectedIndex == mTargetIndex) {
			return;
		}

		// first time
		if (mSelectedIndex == -1) {
			setInAnimation(null);
			setOutAnimation(null);
			mSelectedIndex = selectedIndex;
			View v = mCachedViews.get(mSelectedIndex);
			afterSelectedFirstTime();
			this.setDisplayedChild(this.indexOfChild(v));
			return;
		}

		preSelecting(selectedIndex);
		if (!mAnimEnabled) {
			postSelecting();
		}

	}

	protected void afterSelectedFirstTime() {

	}

	protected void afterSelected() {
		View view = mCachedViews.get(mSelectedIndex);
		setInAnimation(null);
		setOutAnimation(null);
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

	protected void preSelecting(int target) {
		View view;
		boolean pushLeft = false;
		if (mTargetIndex >= 0) {
			pushLeft = target > mTargetIndex;
			view = mCachedViews.get(mTargetIndex);
			if (view instanceof IStateView) {
				if (view instanceof IFragment) {
					((IFragment) view).setLocked(true);
				}
				((IStateView) view).onStop();
			}
		}
		mTargetIndex = target;

		if (mSelectedIndex >= 0) {
			pushLeft = target > mSelectedIndex;
			view = mCachedViews.get(mSelectedIndex);
			if (view instanceof IStateView) {
				if (view instanceof IFragment) {
					((IFragment) view).setLocked(true);
				}
				((IStateView) view).onPause();
				((IStateView) view).onStop();
			}
			mSelectedIndex = -2;
		}

		view = mCachedViews.get(mTargetIndex);
		if (view instanceof IStateView) {
			if (view instanceof IFragment) {
				((IFragment) view).setLocked(false);
			}
			((IStateView) view).onStart();
		}

		if (this.indexOfChild(view) < 0) {
			setInAnimation(null);
			setOutAnimation(null);
			this.addView(view);
		}

		if (mAnimEnabled) {
			if (pushLeft) {
				setInAnimation(mAnimPushLeftIn);
				setOutAnimation(mAnimPushLeftOut);
			} else {
				setInAnimation(mAnimPushRightIn);
				setOutAnimation(mAnimPushRightOut);
			}
		} else {
			setInAnimation(null);
			setOutAnimation(null);
		}
		setDisplayedChild(this.indexOfChild(view));
	}

	protected void postSelecting() {
		if (mTargetIndex < 0) {// onPause->onAnimationEnd
			return;
		}
		View view = mCachedViews.get(mTargetIndex);
		if (view instanceof IStateView) {
			if (view instanceof IFragment) {
				((IFragment) view).setLocked(false);
			}
			((IStateView) view).onResume();
		}
		mSelectedIndex = mTargetIndex;
		afterSelected();
		if (mOnSelectedChangeListener != null) {
			mOnSelectedChangeListener.onChanged(this, mSelectedIndex);
		}
		mTargetIndex = -1;
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

	public void setGestureDetectorEnabled(boolean v) {
		mGestureDetectorEnabled = v;
		if (mGestureDetectorEnabled) {
			Context context = this.getContext();
			mMinDistance = (int) OUtil.getRawSize(context, TypedValue.COMPLEX_UNIT_DIP, 64);
			mMinVelocity = (int) OUtil.getRawSize(context, TypedValue.COMPLEX_UNIT_DIP, 16);
		}
	}

	@Override
	public void onCreate() {
		if (mCreateDispatched) {
			return;
		}
		mCreateDispatched = true;
		if (mAnimEnabled) {
			if (mAnimPushLeftIn == null) {
				this.setAnimation(0, 0, 0, 0);
			}
		}
		if (mGestureDetectorEnabled) {
			if (mGestureDetectorOnGestureListener == null) {
				mGestureDetectorOnGestureListener = new OnGestureListener() {

					@Override
					public boolean onSingleTapUp(MotionEvent e) {
						return false;
					}

					@Override
					public void onShowPress(MotionEvent e) {

					}

					@Override
					public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
						return false;
					}

					@Override
					public void onLongPress(MotionEvent e) {

					}

					@Override
					public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
						if (Math.abs(e1.getY() - e2.getY()) > mMinDistance) {
							return false;
						}
						if (e1.getX() - e2.getX() > mMinDistance && Math.abs(velocityX) > mMinVelocity) {
							setSelectedIndex(mSelectedIndex + 1);
						} else if (e2.getX() - e1.getX() > mMinDistance && Math.abs(velocityX) > mMinVelocity) {
							setSelectedIndex(mSelectedIndex - 1);
						} else {
							return false;
						}
						return true;
					}

					@Override
					public boolean onDown(MotionEvent e) {
						return true;
					}
				};
			}
			mGestureDetector = new GestureDetector(getContext(), mGestureDetectorOnGestureListener);

			if (mOnTouchListener == null) {
				mOnTouchListener = new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (mGestureDetectorEnabled && !onInterceptGesture()) {
							return mGestureDetector.onTouchEvent(event);
						}
						return false;
					}
				};
			}
			this.setOnTouchListener(mOnTouchListener);
		}
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
	public boolean onInterceptTouchEvent(MotionEvent ev) {
		if (mGestureDetectorEnabled && !onInterceptGesture()) {
			return mGestureDetector.onTouchEvent(ev) && super.onInterceptTouchEvent(ev);
		}
		return super.onInterceptTouchEvent(ev);
	}

	public OnTouchListener getGestureListener() {
		return mOnTouchListener;
	}

	@Override
	public boolean onInterceptBackPressed() {
		if (UICore.interceptBackPressed(this)) {
			return true;
		}
		return false;
	}

	public boolean onInterceptGesture() {
		IStateView view = getSelectedView();
		if (view == null) {
			return true;
		}
		if (view instanceof StateView) {
			return ((StateView) view).onInterceptGesture();
		} else {
			return false;
		}
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

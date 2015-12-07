package cn.mutils.app.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.TextView;

public class InfoToast extends TextView {

	protected Animation mFadeInAnim;
	protected Animation mFadeOutAnim;

	protected CharSequence mText;
	protected int mDuration;

	protected Handler mHandler;

	protected Runnable mShowRunable;
	protected Runnable mHideRunable;

	protected boolean mVisibleInvalidate;

	public InfoToast(Context context) {
		super(context);
		this.init(context, null);
	}

	public InfoToast(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.init(context, attrs);
	}

	public InfoToast(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.init(context, attrs);
	}

	public void init(Context context, AttributeSet attrs) {
		this.setVisibility(View.INVISIBLE);
		mFadeInAnim = new AlphaAnimation(0, 1);
		mFadeInAnim.setDuration(300);
		mFadeInAnim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (mVisibleInvalidate) {
					setVisibility(View.VISIBLE);
					mVisibleInvalidate = false;
				}
			}
		});

		mFadeOutAnim = new AlphaAnimation(1, 0);
		mFadeOutAnim.setDuration(300);
		mFadeOutAnim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {

			}

			@Override
			public void onAnimationEnd(Animation animation) {
				if (mVisibleInvalidate) {
					setVisibility(View.INVISIBLE);
					mVisibleInvalidate = false;
				}
			}
		});

		mHandler = new Handler(Looper.getMainLooper());
		mShowRunable = new Runnable() {
			public void run() {
				show(mText, mDuration);
			}
		};
		mHideRunable = new Runnable() {
			public void run() {
				hide();
			}
		};
	}

	public void show(CharSequence text, int duration, int delay) {
		mVisibleInvalidate = true;
		mHandler.removeCallbacksAndMessages(null);

		if (!isEnabled()) {
			return;
		}

		if (delay > 0) {
			mText = text;
			mDuration = duration;
			mHandler.postDelayed(mShowRunable, delay);
		} else {
			setText(text);
			startAnimation(mFadeInAnim);

			if (duration > 0) {
				mHandler.postDelayed(mHideRunable, duration);
			}
		}
	}

	public void show(CharSequence text, int duration) {
		show(text, duration, 0);
	}

	public void show(CharSequence text) {
		show(text, 0);
	}

	public void show(int resid, int duration, int delay) {
		show(getContext().getString(resid), duration, delay);
	}

	public void show(int resid, int duration) {
		show(resid, duration, 0);
	}

	public void show(int resid) {
		show(resid, 0);
	}

	public void hide(int delay) {
		mVisibleInvalidate = true;
		mHandler.removeCallbacksAndMessages(null);

		if (delay > 0) {
			mHandler.postDelayed(mHideRunable, delay);
		} else if (getVisibility() == View.VISIBLE) {
			startAnimation(mFadeOutAnim);
		}
	}

	public void hide() {
		hide(0);
	}

	public void hideNow() {
		mVisibleInvalidate = false;
		mHandler.removeCallbacksAndMessages(null);
		if (getVisibility() == View.VISIBLE) {
			clearAnimation();
			setVisibility(View.INVISIBLE);
		}
	}

}

package cn.o.app.ui;

import android.content.Context;
import android.os.Handler;
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

	protected Handler mDurationHandler;
	protected Handler mShowDelayHandler;
	protected Handler mHideDelayHandler;

	protected Runnable mShowRunable;
	protected Runnable mHideRunable;

	protected boolean mVisibleInvalidate;

	public InfoToast(Context context) {
		super(context);
		this.onCreate();
	}

	public InfoToast(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.onCreate();
	}

	public InfoToast(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.onCreate();
	}

	public void onCreate() {
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

		mDurationHandler = new Handler();
		mShowDelayHandler = new Handler();
		mHideDelayHandler = new Handler();

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
		clearScheduledTask();

		if (isEnabled() == false)
			return;

		if (delay > 0) {
			mText = text;
			mDuration = duration;
			mShowDelayHandler.postDelayed(mShowRunable, delay);
		} else {
			setText(text);
			startAnimation(mFadeInAnim);

			if (duration > 0)
				mHideDelayHandler.postDelayed(mHideRunable, duration);
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
		clearScheduledTask();

		if (delay > 0) {
			mHideDelayHandler.postDelayed(mHideRunable, delay);
		} else if (getVisibility() == View.VISIBLE) {
			startAnimation(mFadeOutAnim);
		}
	}

	public void hide() {
		hide(0);
	}

	public void hideNow() {
		mVisibleInvalidate = false;
		clearScheduledTask();
		if (getVisibility() == View.VISIBLE) {
			clearAnimation();
			setVisibility(View.INVISIBLE);
		}
	}

	private void clearScheduledTask() {
		mDurationHandler.removeCallbacksAndMessages(null);
		mHideDelayHandler.removeCallbacksAndMessages(null);
		mShowDelayHandler.removeCallbacksAndMessages(null);
	}

}

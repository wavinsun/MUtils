package cn.mutils.app.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class StateViewFlipper extends FragmenterFlipper {

	public StateViewFlipper(Context context) {
		super(context);
	}

	public StateViewFlipper(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	protected void afterSelectedFirstTime() {
		this.addView(mCachedViews.get(mSelectedIndex));
	}

	@Override
	protected void afterSelected() {
		View view = mCachedViews.get(mSelectedIndex);
		setInAnimation(null);
		setOutAnimation(null);
		for (int i = this.getChildCount() - 1; i >= 0; i--) {
			View childView = this.getChildAt(i);
			if (childView.equals(view)) {
				continue;
			}
			this.removeView(childView);
		}
	}

	@Override
	protected void afterCreated() {
		this.removeAllViews();
	}

}

package cn.o.app.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class StateViewFrame extends FragmenterFrame {

	public StateViewFrame(Context context) {
		super(context);
	}

	public StateViewFrame(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public StateViewFrame(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void afterSelectedFirstTime() {
		this.addView(mCachedViews.get(mSelectedIndex));
	}

	@Override
	protected void afterSelected() {
		View view = mCachedViews.get(mSelectedIndex);
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

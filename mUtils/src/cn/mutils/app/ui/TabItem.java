package cn.mutils.app.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class TabItem extends TextView {

	protected int mIndex = -1;

	public TabItem(Context context) {
		super(context);
	}

	public TabItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TabItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public int getIndex() {
		return mIndex;
	}

	public void setIndex(int index) {
		mIndex = index;
	}

}

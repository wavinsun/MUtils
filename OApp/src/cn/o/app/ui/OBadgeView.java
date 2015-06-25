package cn.o.app.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TabWidget;

import com.readystatesoftware.viewbadger.BadgeView;

public class OBadgeView extends BadgeView {

	public OBadgeView(Context context) {
		super(context);
	}

	public OBadgeView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public OBadgeView(Context context, View target) {
		super(context, target);
	}

	public OBadgeView(Context context, TabWidget target, int index) {
		super(context, target, index);
	}

	public OBadgeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public OBadgeView(Context context, AttributeSet attrs, int defStyle,
			View target, int tabIndex) {
		super(context, attrs, defStyle, target, tabIndex);
	}

}

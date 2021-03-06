package cn.mutils.app.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TabWidget;

import com.readystatesoftware.viewbadger.BadgeView;

/**
 * Badge view: red point
 */
@SuppressWarnings("unused")
public class BadgeViewer extends BadgeView {

    public BadgeViewer(Context context) {
        super(context);
    }

    public BadgeViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BadgeViewer(Context context, View target) {
        super(context, target);
    }

    public BadgeViewer(Context context, TabWidget target, int index) {
        super(context, target, index);
    }

    public BadgeViewer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public BadgeViewer(Context context, AttributeSet attrs, int defStyle, View target, int tabIndex) {
        super(context, attrs, defStyle, target, tabIndex);
    }

}

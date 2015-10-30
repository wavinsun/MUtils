package cn.mutils.app.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import cn.mutils.app.ui.core.IContentViewOwner;
import cn.mutils.app.ui.core.IView;
import cn.mutils.app.ui.core.IViewFinder;
import cn.mutils.app.ui.core.UICore;

/**
 * View of framework
 */
public class Viewer extends RelativeLayout implements IView, IViewFinder, IContentViewOwner {

	public Viewer(Context context) {
		super(context);
		init(context, null);
	}

	public Viewer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public Viewer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	protected void init(Context context, AttributeSet attrs) {
		UICore.injectContentView(this);
	}

	public void setContentView(View view) {
		this.removeAllViews();
		this.addView(view);
		UICore.injectResources(this);
		UICore.injectEvents(this);
	}

	public void setContentView(int layoutResID) {
		this.removeAllViews();
		LayoutInflater.from(getContext()).inflate(layoutResID, this);
		UICore.injectResources(this);
		UICore.injectEvents(this);
	}

	@Override
	public <T extends View> T findViewById(int id, Class<T> viewClass) {
		return UICore.findViewById(this, id, viewClass);
	}

	@Override
	public View toView() {
		return this;
	}

}

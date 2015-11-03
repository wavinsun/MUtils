package cn.mutils.app.ui.adapter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import cn.mutils.app.ui.core.UICore;

/**
 * RelativeLayout implements {@link IItemView}
 */
public abstract class ItemView<DATA_ITEM> extends RelativeLayout implements IItemView<DATA_ITEM> {

	protected int mPosition = -1;

	protected DATA_ITEM mDataProvider;

	protected UIAdapter<DATA_ITEM> mAdapter;

	public ItemView(Context context) {
		super(context);
	}

	public ItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setAdapter(UIAdapter<DATA_ITEM> adapter) {
		mAdapter = adapter;
	}

	public UIAdapter<DATA_ITEM> getAdapter() {
		return mAdapter;
	}

	public int getPosition() {
		return mPosition;
	}

	public void setPosition(int position) {
		mPosition = position;
	}

	public DATA_ITEM getDataProvider() {
		return mDataProvider;
	}

	public void setDataProvider(DATA_ITEM dataProvider) {
		mDataProvider = dataProvider;
	}

	public void notifyDataSetChanged() {
		if (this.mAdapter == null) {
			return;
		}
		this.mAdapter.notifyDataSetChanged();
	}

	public void setContentView(View view) {
		this.removeAllViews();
		this.addView(view);
		UICore.injectResources(this);
		UICore.injectEvents(this);
	}

	public void setContentView(int layoutResID) {
		this.removeAllViews();
		LayoutInflater.from(this.getContext()).inflate(layoutResID, this);
		UICore.injectResources(this);
		UICore.injectEvents(this);
	}

	public <T extends View> T findViewById(int id, Class<T> viewClass) {
		return UICore.findViewById(this, id, viewClass);
	}

	@Override
	public View toView() {
		return this;
	}

	@Override
	public void onCreate() {
		UICore.injectContentView(this);
	}

}

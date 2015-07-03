package cn.o.app.adapter;

import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

/**
 * Adapter for {@link LinearLayout} whose orientation is
 * {@link LinearLayout#VERTICAL}
 * 
 * @see LinearLayout#VERTICAL
 * @see OAdapter
 * 
 */
public abstract class OVLinearAdapter<DATA_ITEM> extends OCacheAdapter<DATA_ITEM, IItemView<DATA_ITEM>> {

	@Override
	public void setContainer(ViewGroup container) {
		if (container != null) {
			if (!(container instanceof LinearLayout)) {
				throw new IllegalArgumentException("container is not LinearLayout");
			}
			LinearLayout linearLayout = (LinearLayout) container;
			if (linearLayout.getOrientation() != LinearLayout.VERTICAL) {
				linearLayout.setOrientation(LinearLayout.VERTICAL);
			}
		}
		super.setContainer(container);
	}

	@Override
	protected boolean freeCacheNoUse() {
		return Math.random() > 0.3 && mCacheViews.size() / getCacheSizeRequired() > 3;
	}

	@Override
	protected int getCacheSizeMore() {
		return getCacheSizeRequired();
	}

	@Override
	protected void freeCacheAt(int index) {
		IItemView<DATA_ITEM> itemView = mCacheViews.get(index);
		itemView.setAdapter(null);
		itemView.setDataProvider(null);
		mCacheViews.remove(index);
	}

	@Override
	protected void allocCacheAt(int index) {
		IItemView<DATA_ITEM> itemView = getItemView();
		itemView.setAdapter(this);
		itemView.onCreate();
		fixItemViewParams(itemView);
		mCacheViews.add(index, itemView);
	}

	@Override
	protected IItemView<DATA_ITEM> getItemViewAt(int index) {
		return mCacheViews.get(index);
	}

	protected void fixContainerSize() {
		int requiredViews = getCacheSizeRequired();
		if (requiredViews == 0) {
			mContainer.removeAllViews();
			return;
		}
		int containerViews = mContainer.getChildCount();
		if (requiredViews < containerViews) {
			mContainer.removeViews(requiredViews, containerViews - requiredViews);
		} else {
			for (int i = containerViews; i < requiredViews; i++) {
				mContainer.addView(mCacheViews.get(i).toView());
			}
		}
	}

	protected void fixItemViewParams(IItemView<DATA_ITEM> itemView) {
		ViewGroup.LayoutParams itemParams = itemView.toView().getLayoutParams();
		if (itemParams != null) {
			if (itemParams.width <= 0 && itemParams.width != LayoutParams.MATCH_PARENT) {
				itemParams.width = LayoutParams.MATCH_PARENT;
			}
		} else {
			itemView.toView().setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));
		}
	}
}

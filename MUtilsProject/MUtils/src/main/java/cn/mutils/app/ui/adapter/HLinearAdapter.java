package cn.mutils.app.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

/**
 * Adapter for {@link LinearLayout} whose orientation is
 * {@link LinearLayout#HORIZONTAL}
 *
 * @see LinearLayout#HORIZONTAL
 * @see UIAdapter
 */
public class HLinearAdapter<DATA_ITEM> extends CacheAdapter<DATA_ITEM, IItemView<DATA_ITEM>> {

    @Override
    public void setContainer(ViewGroup container) {
        if (container != null) {
            if (!(container instanceof LinearLayout)) {
                throw new IllegalArgumentException("container is not LinearLayout");
            }
            LinearLayout linearLayout = (LinearLayout) container;
            if (linearLayout.getOrientation() != LinearLayout.HORIZONTAL) {
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
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
        IItemView<DATA_ITEM> itemView = getItemView(index);
        itemView.setAdapter(this);
        itemView.onCreate();
        fixItemViewParams(itemView);
        mCacheViews.add(index, itemView);
    }

    @Override
    protected IItemView<DATA_ITEM> getItemViewAt(int index) {
        return mCacheViews.get(index);
    }

    @Override
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
        View v = itemView.toView();
        boolean changed = false;
        LayoutParams itemParams = v.getLayoutParams();
        if (itemParams != null) {
            if (itemParams.height <= 0 && itemParams.height != LayoutParams.MATCH_PARENT) {
                itemParams.height = LayoutParams.MATCH_PARENT;
                changed = true;
            }
        } else {
            itemParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
            changed = true;
        }
        if (changed) {
            v.setLayoutParams(itemParams);
        }
    }

}

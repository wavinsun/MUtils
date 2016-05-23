package cn.mutils.app.ui.adapter;

import android.view.ViewGroup;
import android.widget.AdapterView;

import java.util.LinkedList;
import java.util.List;

/**
 * Cache Adapter who provide ability of {@link AdapterView} to general view
 *
 * @see AdapterView
 * @see ItemAdapter
 */
@SuppressWarnings("unused")
public abstract class CacheAdapter<DATA_ITEM, CACHE_ITEM> extends ItemAdapter<DATA_ITEM> {

    /**
     * Cache views
     */
    protected List<CACHE_ITEM> mCacheViews;

    protected boolean mCacheInvalidate;

    /**
     * Auto allocate more views
     */
    protected boolean mAutoAllocMore;

    public CacheAdapter() {
        super();
        mCacheViews = new LinkedList<CACHE_ITEM>();
        mAutoAllocMore = true;
    }

    @Override
    public void setDataProvider(List<DATA_ITEM> dataProvider) {
        mCacheInvalidate = true;
        super.setDataProvider(dataProvider);
    }

    public void setContainer(ViewGroup container) {
        if (mContainer != null) {
            if (mContainer.equals(container)) {
                return;
            }
            mContainer.removeAllViews();
        }
        this.mContainer = container;
        onDataSetChanged();
    }

    /**
     * Get required cache size for data provider
     */
    protected int getCacheSizeRequired() {
        if (mDataProvider == null) {
            return 0;
        }
        return mDataProvider.size();
    }

    /**
     * Get allocate more size for cache
     */
    protected int getCacheSizeMore() {
        return 1;
    }

    /**
     * Used to know is need to free no use cache
     */
    protected boolean freeCacheNoUse() {
        return Math.random() > 0.7;
    }

    /**
     * Used to know is need to allocate more cache
     */
    protected boolean allocCacheMore() {
        return false;
    }

    /**
     * Free cache at index of mCacheViews
     */
    protected abstract void freeCacheAt(int index);

    /**
     * Allocate cache at index of mCacheViews
     */
    protected abstract void allocCacheAt(int index);

    /**
     * Fix container child views when data provider changed or other things
     * happen
     */
    protected abstract void fixContainerSize();

    /**
     * Used to get cached IItemView to hold index of data provider
     */
    protected abstract IItemView<DATA_ITEM> getItemViewAt(int index);

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        this.onDataSetChanged();
    }

    public boolean isAutoAllocMore() {
        return mAutoAllocMore;
    }

    public void setAutoAllocMore(boolean autoAllocMore) {
        mAutoAllocMore = autoAllocMore;
    }

    protected void onDataSetChanged() {
        if (mContainer == null) {
            return;
        }
        if (mDataProvider == null) {
            return;
        }
        if (mCacheInvalidate) {
            int requiredViews = getCacheSizeRequired();
            if (requiredViews != 0) {
                if (requiredViews < mCacheViews.size()) {
                    fixContainerSize();
                    if (freeCacheNoUse()) {
                        for (int i = mCacheViews.size() - 1, startIndex = mCacheViews.size()
                                - requiredViews; i >= startIndex; i--) {
                            freeCacheAt(i);
                        }
                    }
                } else if (requiredViews > mCacheViews.size()) {
                    for (int i = mCacheViews.size(); i < requiredViews; i++) {
                        allocCacheAt(i);
                    }
                    if (mAutoAllocMore) {
                        if (allocCacheMore()) {
                            for (int i = mCacheViews.size(), size = mCacheViews.size()
                                    + getCacheSizeMore(); i < size; i++) {
                                allocCacheAt(i);
                            }
                        }
                    }
                    fixContainerSize();
                } else {
                    fixContainerSize();
                }
            } else {
                fixContainerSize();
            }
            mCacheInvalidate = false;
        }
        for (int i = 0, size = mDataProvider.size(); i < size; i++) {
            IItemView<DATA_ITEM> itemView = getItemViewAt(i);
            itemView.setPosition(i);
            itemView.setDataProvider(mDataProvider.get(i));
            itemView.onResume();
        }
    }
}

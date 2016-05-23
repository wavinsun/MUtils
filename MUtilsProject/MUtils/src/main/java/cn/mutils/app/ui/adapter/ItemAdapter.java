package cn.mutils.app.ui.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.lang.reflect.Constructor;
import java.util.List;

import cn.mutils.app.os.IContextProvider;
import cn.mutils.core.reflect.ReflectUtil;

/**
 * Provide object-oriented item view
 */
@SuppressWarnings({"unchecked", "unused"})
public class ItemAdapter<DATA_ITEM> extends BaseAdapter implements IItemAdapter<DATA_ITEM> {

    /**
     * Container who hold item views
     */
    protected ViewGroup mContainer;

    /**
     * Data provider for item views
     */
    protected List<DATA_ITEM> mDataProvider;

    /**
     * Constructor for item views
     */
    protected Constructor<? extends IItemView<DATA_ITEM>> mItemViewConstructor;

    /**
     * Set type for item views to create by reflection
     *
     * @see #getItemView(int)
     */
    public void setItemViewClass(Class<? extends IItemView<DATA_ITEM>> itemViewClass) {
        mItemViewConstructor = ReflectUtil.getConstructor(itemViewClass, Context.class);
    }

    /**
     * Get context
     *
     * @return Return null if container is null
     */
    public Context getContext() {
        if (this.mContainer == null) {
            return null;
        }
        return this.mContainer.getContext();
    }

    /**
     * Get container
     *
     * @see #mContainer
     */
    public ViewGroup getContainer() {
        return this.mContainer;
    }

    /**
     * Call back for container change
     */
    protected void onContainerChanged() {

    }

    /**
     * Get data provider for item views
     */
    public List<DATA_ITEM> getDataProvider() {
        return mDataProvider;
    }

    /**
     * Set data provider for item views
     */
    public void setDataProvider(List<DATA_ITEM> dataProvider) {
        this.mDataProvider = dataProvider;
        this.notifyDataSetChanged();
    }

    /**
     * Get count of item views
     *
     * @return count of item views
     */
    @Override
    public int getCount() {
        if (this.mDataProvider == null) {
            return 0;
        }
        return this.mDataProvider.size();
    }

    /**
     * Get item view data provider for index
     *
     * @return item view data provider
     * @see IItemView#getDataProvider()
     */
    @Override
    public DATA_ITEM getItem(int position) {
        if (this.mDataProvider == null) {
            return null;
        }
        return this.mDataProvider.get(position);
    }

    /**
     * Get item id
     */
    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * Subclass need to override this method
     *
     * @see IItemView
     */
    public IItemView<DATA_ITEM> getItemView(int itemViewType) {
        if (mItemViewConstructor == null) {
            return null;
        }
        return ReflectUtil.newInstance(mItemViewConstructor, getContext());
    }

    /**
     * Object-orient item view creation and reuse
     *
     * @see BaseAdapter#getView(int, View, ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (this.mContainer == null) {
            this.mContainer = parent;
            this.onContainerChanged();
        }
        IItemView<DATA_ITEM> itemView = (IItemView<DATA_ITEM>) convertView;
        boolean isCreated = itemView != null;
        if (!isCreated) {
            itemView = this.getItemView(this.getItemViewType(position));
            itemView.setAdapter(this);
        }
        itemView.setPosition(position);
        itemView.setDataProvider(this.getItem(position));
        if (!isCreated) {
            itemView.onCreate();
        }
        itemView.onResume();
        return itemView.toView();
    }
}

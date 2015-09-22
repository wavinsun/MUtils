package cn.o.app.ui.adapter;

import java.lang.reflect.Constructor;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import cn.o.app.core.reflect.ReflectUtil;
import cn.o.app.os.IContextProvider;

/**
 * Provide object-oriented item view
 */
@SuppressWarnings("unchecked")
public class UIAdapter<DATA_ITEM> extends BaseAdapter implements IContextProvider {

	/** Container who hold item views */
	protected ViewGroup mContainer;

	/** Data provider for item views */
	protected List<DATA_ITEM> mDataProvider;

	/** Constructor for item views */
	protected Constructor<? extends IItemView<DATA_ITEM>> mItemViewConstructor;

	/**
	 * Set type for item views to create by reflection
	 * 
	 * @see #getItemView()
	 * 
	 * @param itemViewClass
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
	 * 
	 * @return
	 */
	public View getContainer() {
		return this.mContainer;
	}

	/**
	 * Call back for container change
	 */
	protected void onContainerChanged() {

	}

	/**
	 * Get data provider for item views
	 * 
	 * @return
	 */
	public List<DATA_ITEM> getDataProvider() {
		return mDataProvider;
	}

	/**
	 * Set data provider for item views
	 * 
	 * @param dataProvider
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
	 * @see IItemView#getDataProvider()
	 * 
	 * @return item view data provider
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
	 * 
	 * @return
	 */
	public IItemView<DATA_ITEM> getItemView() {
		if (mItemViewConstructor == null) {
			return null;
		}
		return ReflectUtil.newInstance(mItemViewConstructor, getContext());
	}

	/**
	 * Object-orient item view creation and reuse
	 * 
	 * @see BaseAdapter#getView(int, View, ViewGroup)
	 * 
	 * @param position
	 * @param convertView
	 * @param parent
	 * @return {@link IItemView}
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
			itemView = this.getItemView();
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

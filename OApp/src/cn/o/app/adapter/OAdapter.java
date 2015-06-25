package cn.o.app.adapter;

import java.lang.reflect.Constructor;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import cn.o.app.context.IContextProvider;
import cn.o.app.runtime.ReflectUtil;

@SuppressWarnings("unchecked")
public class OAdapter<DATA_ITEM> extends BaseAdapter implements
		IContextProvider {

	protected ViewGroup mContainer;

	protected List<DATA_ITEM> mDataProvider;

	protected Constructor<? extends IItemView<DATA_ITEM>> mItemViewConstructor;

	public void setItemViewClass(
			Class<? extends IItemView<DATA_ITEM>> itemViewClass) {
		mItemViewConstructor = ReflectUtil.getConstructor(itemViewClass,
				Context.class);
	}

	public Context getContext() {
		if (this.mContainer == null) {
			return null;
		}
		return this.mContainer.getContext();
	}

	public View getContainer() {
		return this.mContainer;
	}

	protected void onContainerChanged() {

	}

	public List<DATA_ITEM> getDataProvider() {
		return mDataProvider;
	}

	public void setDataProvider(List<DATA_ITEM> dataProvider) {
		this.mDataProvider = dataProvider;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (this.mDataProvider == null) {
			return 0;
		}
		return this.mDataProvider.size();
	}

	@Override
	public DATA_ITEM getItem(int position) {
		if (this.mDataProvider == null) {
			return null;
		}
		return this.mDataProvider.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	// 子类需要覆盖该方法
	public IItemView<DATA_ITEM> getItemView() {
		if (mItemViewConstructor == null) {
			return null;
		}
		return ReflectUtil.newInstance(mItemViewConstructor, getContext());
	}

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

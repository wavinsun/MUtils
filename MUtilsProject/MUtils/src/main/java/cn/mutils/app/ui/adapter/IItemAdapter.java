package cn.mutils.app.ui.adapter;

import android.widget.ListAdapter;
import android.widget.SpinnerAdapter;

import java.util.List;

import cn.mutils.app.os.IContextProvider;

/**
 * IItemAdapter
 */
public interface IItemAdapter<DATA_ITEM> extends ListAdapter, SpinnerAdapter,IContextProvider {

    List<DATA_ITEM> getDataProvider();

    void setDataProvider(List<DATA_ITEM> dataProvider);

    IItemView<DATA_ITEM> getItemView(int itemViewType);

    void notifyDataSetChanged();

}

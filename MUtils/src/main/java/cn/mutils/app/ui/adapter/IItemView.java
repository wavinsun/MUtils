package cn.mutils.app.ui.adapter;

import android.view.View;
import android.widget.BaseAdapter;

import cn.mutils.app.os.IContextProvider;
import cn.mutils.app.ui.core.IContentViewOwner;
import cn.mutils.app.ui.core.IView;
import cn.mutils.app.ui.core.IViewFinder;

/**
 * Object-oriented for
 * {@link BaseAdapter#getView(int, View, android.view.ViewGroup)}
 *
 * @see IItemAdapter#getItemView(int)
 * @see ItemAdapter#getView(int, View, android.view.ViewGroup)
 */
@SuppressWarnings("unused")
public interface IItemView<DATA_ITEM> extends IView, IViewFinder, IContentViewOwner, IContextProvider {

    /**
     * Get Adapter for IItemView.
     *
     * @return {@link ItemAdapter}
     */
    IItemAdapter<DATA_ITEM> getAdapter();

    /**
     * Set Adapter for IItemView.
     * <p>
     * It called by framework.
     *
     * @param adapter Adapter
     * @see ItemAdapter#getView(int, View, android.view.ViewGroup)
     */
    void setAdapter(IItemAdapter<DATA_ITEM> adapter);

    /**
     * Get position for IItemView.
     */
    int getPosition();

    /**
     * Set position for IItemView.<br>
     * It called by framework.
     *
     * @see ItemAdapter#getView(int, View, android.view.ViewGroup)
     */
    void setPosition(int position);

    /**
     * Get data provider for IItemView.
     */
    DATA_ITEM getDataProvider();

    /**
     * Set data provider for IItemView.
     *
     * @see ItemAdapter#getView(int, View, android.view.ViewGroup)
     */
    void setDataProvider(DATA_ITEM dataProvider);

    /**
     * Notify data set changed by IItemView
     *
     * @see ItemAdapter#notifyDataSetChanged()
     */
    void notifyDataSetChanged();

    /**
     * IItemView creation
     *
     * @see ItemAdapter#getView(int, View, android.view.ViewGroup)
     */
    void onCreate();

    /**
     * IItemView reuse
     *
     * @see ItemAdapter#getView(int, View, android.view.ViewGroup)
     */
    void onResume();

    /**
     * Set content view for IItemView by view
     */
    void setContentView(View view);

    /**
     * Set content view for IItemView by view layout resource
     */
    void setContentView(int layoutResID);

}

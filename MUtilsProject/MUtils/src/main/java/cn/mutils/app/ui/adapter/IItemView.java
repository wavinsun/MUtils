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
 * @see UIAdapter#getItemView(int)
 * @see UIAdapter#getView(int, View, android.view.ViewGroup)
 */
@SuppressWarnings("unused")
public interface IItemView<DATA_ITEM> extends IView, IViewFinder, IContentViewOwner, IContextProvider {

    /**
     * Get Adapter for IItemView.
     *
     * @return {@link UIAdapter}
     */
    UIAdapter<DATA_ITEM> getAdapter();

    /**
     * Set Adapter for IItemView.
     * <p>
     * It called by framework.
     *
     * @param adapter Adapter
     * @see UIAdapter#getView(int, View, android.view.ViewGroup)
     */
    void setAdapter(UIAdapter<DATA_ITEM> adapter);

    /**
     * Get position for IItemView.
     */
    int getPosition();

    /**
     * Set position for IItemView.<br>
     * It called by framework.
     *
     * @see UIAdapter#getView(int, View, android.view.ViewGroup)
     */
    void setPosition(int position);

    /**
     * Get data provider for IItemView.
     */
    DATA_ITEM getDataProvider();

    /**
     * Set data provider for IItemView.
     *
     * @see UIAdapter#getView(int, View, android.view.ViewGroup)
     */
    void setDataProvider(DATA_ITEM dataProvider);

    /**
     * Notify data set changed by IItemView
     *
     * @see UIAdapter#notifyDataSetChanged()
     */
    void notifyDataSetChanged();

    /**
     * IItemView creation
     *
     * @see UIAdapter#getView(int, View, android.view.ViewGroup)
     */
    void onCreate();

    /**
     * IItemView reuse
     *
     * @see UIAdapter#getView(int, View, android.view.ViewGroup)
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

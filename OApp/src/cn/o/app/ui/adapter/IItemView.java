package cn.o.app.ui.adapter;

import android.view.View;
import android.widget.BaseAdapter;
import cn.o.app.os.IContextProvider;
import cn.o.app.ui.core.IContentViewOwner;
import cn.o.app.ui.core.IView;
import cn.o.app.ui.core.IViewFinder;

/**
 * Object-oriented for
 * {@link BaseAdapter#getView(int, View, android.view.ViewGroup)}
 * 
 * @see UIAdapter#getItemView()
 * @see UIAdapter#getView(int, View, android.view.ViewGroup)
 */
public interface IItemView<DATA_ITEM> extends IView, IViewFinder, IContentViewOwner, IContextProvider {

	/**
	 * Get Adapter for IItemView.
	 * 
	 * @return {@link UIAdapter}
	 */
	public UIAdapter<DATA_ITEM> getAdapter();

	/**
	 * Set Adapter for IItemView.
	 * 
	 * It called by framework.
	 * 
	 * @see UIAdapter#getView(int, View, android.view.ViewGroup)
	 * 
	 * @param dapter
	 * 
	 */
	public void setAdapter(UIAdapter<DATA_ITEM> dapter);

	/**
	 * Get position for IItemView.
	 * 
	 * @return
	 */
	public int getPosition();

	/**
	 * Set position for IItemView.<br>
	 * It called by framework.
	 * 
	 * @see UIAdapter#getView(int, View, android.view.ViewGroup)
	 * 
	 * @param position
	 */
	public void setPosition(int position);

	/**
	 * Get data provider for IItemView.
	 * 
	 * @return
	 */
	public DATA_ITEM getDataProvider();

	/**
	 * Set data provider for IItemView.
	 * 
	 * @see UIAdapter#getView(int, View, android.view.ViewGroup)
	 * 
	 * @param dataProvider
	 */
	public void setDataProvider(DATA_ITEM dataProvider);

	/**
	 * Notify data set changed by IItemView
	 * 
	 * @see UIAdapter#notifyDataSetChanged()
	 */
	public void notifyDataSetChanged();

	/**
	 * IItemView creation
	 * 
	 * @see UIAdapter#getView(int, View, android.view.ViewGroup)
	 */
	public void onCreate();

	/**
	 * IItemView reuse
	 * 
	 * @see UIAdapter#getView(int, View, android.view.ViewGroup)
	 */
	public void onResume();

	/**
	 * Set content view for IItemView by view
	 */
	public void setContentView(View view);

	/**
	 * Set content view for IItemView by view layout resource
	 */
	public void setContentView(int layoutResID);

}

package cn.o.app.adapter;

import android.view.View;
import android.widget.BaseAdapter;
import cn.o.app.context.IContextProvider;
import cn.o.app.ui.core.IContentViewOwner;
import cn.o.app.ui.core.IView;
import cn.o.app.ui.core.IViewFinder;

/**
 * Object-oriented for
 * {@link BaseAdapter#getView(int, View, android.view.ViewGroup)}
 * 
 * @see OAdapter#getItemView()
 * @see OAdapter#getView(int, View, android.view.ViewGroup)
 */
public interface IItemView<DATA_ITEM> extends IView, IViewFinder, IContentViewOwner, IContextProvider {

	/**
	 * Get Adapter for IItemView.
	 * 
	 * @return {@link OAdapter}
	 */
	public OAdapter<DATA_ITEM> getAdapter();

	/**
	 * Set Adapter for IItemView.
	 * 
	 * It called by framework.
	 * 
	 * @see OAdapter#getView(int, View, android.view.ViewGroup)
	 * 
	 * @param dapter
	 * 
	 */
	public void setAdapter(OAdapter<DATA_ITEM> dapter);

	/**
	 * Get position for IItemView.
	 * 
	 * @return
	 */
	public int getPosition();

	/**
	 * Set position for IItemView
	 * 
	 * It called by framework.
	 * 
	 * @see OAdapter#getView(int, View, android.view.ViewGroup)
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
	 * @see OAdapter#getView(int, View, android.view.ViewGroup)
	 * 
	 * @param dataProvider
	 */
	public void setDataProvider(DATA_ITEM dataProvider);

	/**
	 * Notify data set changed by IItemView
	 * 
	 * @see OAdapter#notifyDataSetChanged()
	 */
	public void notifyDataSetChanged();

	/**
	 * IItemView creation
	 * 
	 * @see OAdapter#getView(int, View, android.view.ViewGroup)
	 */
	public void onCreate();

	/**
	 * IItemView reuse
	 * 
	 * @see OAdapter#getView(int, View, android.view.ViewGroup)
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

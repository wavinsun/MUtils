package cn.o.app.adapter;

import android.view.View;
import cn.o.app.context.IContextProvider;
import cn.o.app.ui.core.IContentViewOwner;
import cn.o.app.ui.core.IView;
import cn.o.app.ui.core.IViewFinder;

public interface IItemView<DATA_ITEM> extends IView, IViewFinder,
		IContentViewOwner, IContextProvider {

	public OAdapter<DATA_ITEM> getAdapter();

	public void setAdapter(OAdapter<DATA_ITEM> dapter);

	public int getPosition();

	public void setPosition(int position);

	public DATA_ITEM getDataProvider();

	public void setDataProvider(DATA_ITEM dataProvider);

	public void notifyDataSetChanged();

	public void onCreate();

	public void onResume();

	public void setContentView(View view);

	public void setContentView(int layoutResID);

}

package cn.o.app.ui.core;

import java.util.List;

public interface IStateViewManager extends IViewFinder {

	public void bind(IStateView stateView);

	public void notify(Object message);

	public List<IStateView> getBindStateViews();

	// 是否生命周期信息直接转发给选中的子View
	public boolean redirectToSelectedView();

	public IStateView getSelectedView();
}

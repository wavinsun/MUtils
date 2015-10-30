package cn.mutils.app.ui.core;

import java.util.List;

public interface IStateViewManager extends IViewFinder {

	public void bind(IStateView stateView);

	public void notify(Object message);

	public List<IStateView> getBindStateViews();

	/**
	 * Whether only forward life cycle message to selected view
	 * 
	 * @return
	 */
	public boolean redirectToSelectedView();

	public IStateView getSelectedView();

}

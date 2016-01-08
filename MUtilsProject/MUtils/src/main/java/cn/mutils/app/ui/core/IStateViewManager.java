package cn.mutils.app.ui.core;

import java.util.List;

public interface IStateViewManager extends IViewFinder {

    void bind(IStateView stateView);

    void notify(Object message);

    List<IStateView> getBindStateViews();

    /**
     * Whether only forward life cycle message to selected view
     */
    boolean redirectToSelectedView();

    IStateView getSelectedView();

}

package cn.o.app.ui.adapter;

import cn.o.app.ui.PopMenu;

/**
 * Adapter for {@link PopMenu}
 * 
 * @see UIAdapter
 */
public abstract class PopMenuAdapter<DATA_ITEM> extends VLinearAdapter<DATA_ITEM> {

	protected PopMenu<DATA_ITEM> mPopMenu;

	public void setPopupMenu(PopMenu<DATA_ITEM> popupMenu) {
		mPopMenu = popupMenu;
	}

	public PopMenu<DATA_ITEM> getPopupMenue() {
		return mPopMenu;
	}

}

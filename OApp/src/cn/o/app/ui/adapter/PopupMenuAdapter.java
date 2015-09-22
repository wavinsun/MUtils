package cn.o.app.ui.adapter;

import cn.o.app.ui.OPopupMenu;

/**
 * Adapter for {@link OPopupMenu}
 * 
 * @see UIAdapter
 */
public abstract class PopupMenuAdapter<DATA_ITEM> extends VLinearAdapter<DATA_ITEM> {

	protected OPopupMenu<DATA_ITEM> mPopMenu;

	public void setPopupMenu(OPopupMenu<DATA_ITEM> popupMenu) {
		mPopMenu = popupMenu;
	}

	public OPopupMenu<DATA_ITEM> getPopupMenue() {
		return mPopMenu;
	}

}

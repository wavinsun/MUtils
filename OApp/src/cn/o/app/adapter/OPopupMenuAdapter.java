package cn.o.app.adapter;

import cn.o.app.ui.OPopupMenu;

/**
 * Adapter for {@link OPopupMenu}
 * 
 * @see OAdapter
 */
public abstract class OPopupMenuAdapter<DATA_ITEM> extends OVLinearAdapter<DATA_ITEM> {

	protected OPopupMenu<DATA_ITEM> mPopMenu;

	public void setPopupMenu(OPopupMenu<DATA_ITEM> popupMenu) {
		mPopMenu = popupMenu;
	}

	public OPopupMenu<DATA_ITEM> getPopupMenue() {
		return mPopMenu;
	}

}

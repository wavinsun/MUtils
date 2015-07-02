package cn.o.app.ui.core;

import android.view.View;

public interface IViewFinder {

	public <T extends View> T findViewById(int id, Class<T> viewClass);

}

package cn.mutils.app.ui.core;

import android.view.View;

public interface IViewFinder {

    <T extends View> T findViewById(int id, Class<T> viewClass);

}

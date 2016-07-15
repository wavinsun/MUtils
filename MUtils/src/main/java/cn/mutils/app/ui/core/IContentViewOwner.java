package cn.mutils.app.ui.core;

import android.view.View;

public interface IContentViewOwner {

    void setContentView(int layoutResID);

    void setContentView(View view);

}

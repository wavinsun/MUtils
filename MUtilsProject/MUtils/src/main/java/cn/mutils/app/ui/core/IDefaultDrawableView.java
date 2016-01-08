package cn.mutils.app.ui.core;

import android.graphics.drawable.Drawable;

public interface IDefaultDrawableView {

    Drawable getDefault();

    void setDefault(Drawable drawable);

    void setDefault(int resId);

    void showDefault();

    void showDefault(Drawable drawable);

    void showDefault(int resId);

}
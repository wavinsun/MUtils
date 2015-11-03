package cn.mutils.app.ui.core;

import android.graphics.drawable.Drawable;

public interface IDefaultDrawableView {

	public Drawable getDefault();

	public void setDefault(Drawable drawable);

	public void setDefault(int resId);

	public void showDefault();

	public void showDefault(Drawable drawable);

	public void showDefault(int resId);

}
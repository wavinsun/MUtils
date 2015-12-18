package cn.mutils.app.ui.core;

import android.support.v4.app.FragmentManager;
import cn.mutils.app.core.ILockable;

public interface IFragmenter extends IStateView, ILockable {

	public FragmentManager getSupportFragmentManagerFromParent();

	public boolean isFragmentVisible();

	public void setFragmentVisible(boolean visible);

}

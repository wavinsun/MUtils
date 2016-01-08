package cn.mutils.app.ui.core;

import android.support.v4.app.FragmentManager;

import cn.mutils.app.core.ILockable;

public interface IFragmenter extends IStateView, ILockable {

    FragmentManager getSupportFragmentManagerFromParent();

    boolean isFragmentVisible();

    void setFragmentVisible(boolean visible);

}

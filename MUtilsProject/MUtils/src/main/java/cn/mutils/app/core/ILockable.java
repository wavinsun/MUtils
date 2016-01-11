package cn.mutils.app.core;

@SuppressWarnings("unused")
public interface ILockable {

    boolean isLocked();

    void setLocked(boolean locked);

}

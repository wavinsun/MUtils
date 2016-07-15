package cn.mutils.core;

@SuppressWarnings("unused")
public interface ILockable {

    boolean isLocked();

    void setLocked(boolean locked);

}

package cn.mutils.core.runtime;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

/**
 * Delegate of framework
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
@Keep
@KeepClassMembers
public class Delegate<T extends Delegate> {

    /**
     * Delegate object
     */
    protected T mDelegate;

    public Delegate() {
        setDelegate(this.classDelegate());
    }

    public T delegate() {
        if (mDelegate != null) {
            return mDelegate;
        }
        try {
            return (T) this;
        } catch (Exception e) {
            return null;
        }
    }

    public String classDelegate() {
        return null;
    }

    public void setDelegate(String delegate) {
        mDelegate = null;
        if (delegate != null && !delegate.equals(this.getClass().getName())) {
            try {
                Class<T> c = (Class<T>) Class.forName(delegate);
                mDelegate = c.newInstance();
            } catch (Exception e) {
                // ClassNotFoundException
            }
        }
    }

}

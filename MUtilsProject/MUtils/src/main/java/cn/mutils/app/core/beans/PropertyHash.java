package cn.mutils.app.core.beans;

/**
 * Hash cache for object property
 */
public class PropertyHash {

    protected long mHash;

    protected Object mPorperty;

    public long getHash() {
        return mHash;
    }

    public void setHash(long value) {
        mHash = value;
    }

    public Object getProperty() {
        return mPorperty;
    }

    public void setProperty(Object value) {
        mPorperty = value;
    }

    /**
     * Whether hash is valid.
     *
     * @return Return false if object property changed by itself
     */
    public boolean isValid() {
        return mHash == HashCode.hashCode(mPorperty);
    }

}

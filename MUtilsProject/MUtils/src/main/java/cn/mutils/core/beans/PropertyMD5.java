package cn.mutils.core.beans;

/**
 * Property MD5 info data
 */
@SuppressWarnings("unused")
public class PropertyMD5 {

    /**
     * MD5 info data for property
     */
    protected ObjectMD5 mMD5;

    /**
     * Property of object
     */
    protected Object mProperty;

    public ObjectMD5 getMD5() {
        return mMD5;
    }

    public void setMD5(ObjectMD5 value) {
        mMD5 = value;
    }

    public Object getProperty() {
        return mProperty;
    }

    public void setProperty(Object value) {
        mProperty = value;
    }

    /**
     * Whether md5 is valid.
     *
     * @return Return false if object property changed by itself
     */
    public boolean isValid() {
        return new ObjectMD5(mProperty).equals(mMD5);
    }

}

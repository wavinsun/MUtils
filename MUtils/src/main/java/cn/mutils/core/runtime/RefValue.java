package cn.mutils.core.runtime;

/**
 * Reference value
 *
 * @param <T>
 */
@SuppressWarnings("unused")
public class RefValue<T> {

    protected T mValue;

    public RefValue() {

    }

    public RefValue(T value) {
        mValue = value;
    }

    public T getValue() {
        return mValue;
    }

    public void setValue(T value) {
        mValue = value;
    }

}

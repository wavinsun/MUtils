package cn.mutils.app.core.runtime;

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

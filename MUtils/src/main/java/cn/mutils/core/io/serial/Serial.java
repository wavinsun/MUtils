package cn.mutils.core.io.serial;

import cn.mutils.core.annotation.Ignore;
import cn.mutils.core.annotation.PrimitiveType;
import cn.mutils.core.json.IJsonItem;
import cn.mutils.core.properties.IPropertyItem;
import cn.mutils.core.xml.IXmlItem;

@SuppressWarnings({"rawtypes", "serial"})
public abstract class Serial<T> implements IJsonItem, IXmlItem, IPropertyItem {

    /**
     * Type of of serialization and deserialization
     */
    protected PrimitiveType mType = PrimitiveType.STRING;

    protected T mValue;

    public Serial() {

    }

    public Serial(T value) {
        mValue = value;
    }

    public T value() {
        return mValue;
    }

    @Ignore
    public T getValue() {
        return mValue;
    }

    public void setValue(T value) {
        if (value != null) {
            mValue = value;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (mValue == null) {
            return false;
        }
        if (o instanceof Serial) {
            return mValue.equals(((Serial) o).value());
        } else {
            return mValue.equals(o);
        }
    }

    @Override
    public String toString() {
        return mValue == null ? null : mValue.toString();
    }

    public PrimitiveType getType() {
        return mType;
    }

    public void setType(PrimitiveType type) {
        mType = type;
    }

}

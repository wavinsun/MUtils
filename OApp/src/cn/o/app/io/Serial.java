package cn.o.app.io;

import cn.o.app.annotation.Ignore;
import cn.o.app.annotation.Primitive.PrimitiveType;
import cn.o.app.json.IJsonItem;
import cn.o.app.properties.IPropertyItem;
import cn.o.app.xml.IXmlItem;

@SuppressWarnings({ "rawtypes", "serial" })
public abstract class Serial<T> implements IJsonItem, IXmlItem, IPropertyItem {

	// 序列化反序列化类型
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

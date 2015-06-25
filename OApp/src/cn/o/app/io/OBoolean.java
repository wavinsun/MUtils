package cn.o.app.io;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import cn.o.app.annotation.False;
import cn.o.app.annotation.Primitive;
import cn.o.app.annotation.Primitive.PrimitiveType;
import cn.o.app.annotation.True;
import cn.o.app.json.IJsonItem;
import cn.o.app.properties.IPropertyItem;
import cn.o.app.runtime.OField;
import cn.o.app.xml.IXmlItem;
import cn.o.app.xml.XmlUtil;

//支持以下两种类型数据{"isOK":true} {"isOK":"true"}
@SuppressWarnings("serial")
public class OBoolean extends Serial<Boolean> {

	protected String mTrue = "true";

	protected String mFalse = "false";

	public OBoolean() {
		mType = PrimitiveType.STRING_BOOL;
		mValue = Boolean.FALSE;
	}

	public OBoolean(boolean value) {
		mType = PrimitiveType.STRING_BOOL;
		mValue = Boolean.valueOf(value);
	}

	public OBoolean(String value) {
		mType = PrimitiveType.STRING_BOOL;
		mValue = Boolean.valueOf(value);
	}

	public boolean booleanValue() {
		return mValue.booleanValue();
	}

	protected void init(OField itemField) {
		if (itemField != null) {
			Primitive type = itemField.getAnnotation(Primitive.class);
			if (type != null) {
				mType = type.value();
			}
			True t = itemField.getAnnotation(True.class);
			if (t != null && !t.value().isEmpty()) {
				mTrue = t.value();
			}
			False f = itemField.getAnnotation(False.class);
			if (f != null && !f.value().isEmpty()) {
				mFalse = f.value();
			}
		}
	}

	@Override
	public IJsonItem fromJson(Object json, OField itemField) {
		init(itemField);
		String str = json.toString();
		if (mType == PrimitiveType.STRING_BOOL) {
			if (str.equals(mTrue)) {
				mValue = Boolean.TRUE;
			} else if (str.equals(mFalse)) {
				mValue = Boolean.FALSE;
			} else {
				return null;
			}
			return this;
		} else {
			try {
				mValue = Boolean.valueOf(str);
			} catch (Exception e) {
				return null;
			}
			return this;
		}
	}

	@Override
	public Object toJson(OField itemField) {
		init(itemField);
		if (mType == PrimitiveType.STRING_BOOL) {
			return mValue ? mTrue : mFalse;
		} else {
			return mValue;
		}
	}

	@Override
	public IXmlItem fromXml(Node xml, OField itemField) {
		init(itemField);
		String str = xml.getTextContent();
		if (str.equals(mTrue)) {
			mValue = Boolean.TRUE;
		} else if (str.equals(mFalse)) {
			mValue = Boolean.FALSE;
		} else {
			return null;
		}
		return this;
	}

	@Override
	public Node toXml(Document doc, OField itemField) {
		init(itemField);
		Node node = XmlUtil.newNode(doc, itemField);
		node.setTextContent(mValue ? mTrue : mFalse);
		return node;
	}

	@Override
	public IPropertyItem fromProperty(String value, OField itemField) {
		init(itemField);
		if (value.equals(mTrue)) {
			mValue = Boolean.TRUE;
		} else if (value.equals(mFalse)) {
			mValue = Boolean.FALSE;
		} else {
			return null;
		}
		return this;
	}

	@Override
	public String toProperty(OField itemField) {
		init(itemField);
		return mValue ? mTrue : mFalse;
	}

}

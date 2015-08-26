package cn.o.app.core.io;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import cn.o.app.core.annotation.Primitive;
import cn.o.app.core.annotation.Primitive.PrimitiveType;
import cn.o.app.core.json.IJsonItem;
import cn.o.app.core.properties.IPropertyItem;
import cn.o.app.core.runtime.BeanField;
import cn.o.app.core.xml.IXmlItem;
import cn.o.app.core.xml.XmlUtil;

/**
 * Support two data types:{"n":"0"} {"n":0}
 */
@SuppressWarnings("serial")
public class OInteger extends Serial<Integer> {

	public OInteger() {
		mType = PrimitiveType.STRING_INT;
		mValue = Integer.valueOf(0);
	}

	public OInteger(int value) {
		mType = PrimitiveType.STRING_INT;
		mValue = Integer.valueOf(value);
	}

	public OInteger(String value) {
		mType = PrimitiveType.STRING_INT;
		mValue = Integer.valueOf(value);
	}

	public int intValue() {
		return mValue.intValue();
	}

	protected void init(BeanField itemField) {
		if (itemField != null) {
			Primitive t = itemField.getAnnotation(Primitive.class);
			if (t != null) {
				mType = t.value();
			}
		}
	}

	@Override
	public IJsonItem fromJson(Object json, BeanField itemField) {
		init(itemField);
		try {
			mValue = Integer.valueOf(json.toString());
		} catch (Exception e) {
			return null;
		}
		return this;
	}

	@Override
	public Object toJson(BeanField itemField) {
		init(itemField);
		if (mType == PrimitiveType.STRING_INT) {
			return mValue.toString();
		} else {
			return mValue;
		}
	}

	@Override
	public IXmlItem fromXml(Node xml, BeanField itemField) {
		init(itemField);
		try {
			mValue = Integer.valueOf(xml.getTextContent());
		} catch (Exception e) {
			return null;
		}
		return this;
	}

	@Override
	public Node toXml(Document doc, BeanField itemField) {
		init(itemField);
		Node node = XmlUtil.newNode(doc, itemField);
		node.setTextContent(mValue.toString());
		return node;
	}

	@Override
	public IPropertyItem fromProperty(String value, BeanField itemField) {
		init(itemField);
		try {
			mValue = Integer.valueOf(value);
		} catch (Exception e) {
			return null;
		}
		return this;
	}

	@Override
	public String toProperty(BeanField itemField) {
		init(itemField);
		return mValue.toString();
	}

}

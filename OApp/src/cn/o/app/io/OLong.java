package cn.o.app.io;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import cn.o.app.annotation.Primitive;
import cn.o.app.annotation.Primitive.PrimitiveType;
import cn.o.app.json.IJsonItem;
import cn.o.app.properties.IPropertyItem;
import cn.o.app.runtime.OField;
import cn.o.app.xml.IXmlItem;
import cn.o.app.xml.XmlUtil;

/**
 * Support two data types:{"n":"0"} {"n":0}
 */
@SuppressWarnings("serial")
public class OLong extends Serial<Long> {

	public OLong() {
		mType = PrimitiveType.STRING_LONG;
		mValue = Long.valueOf(0L);
	}

	public OLong(long value) {
		mType = PrimitiveType.STRING_LONG;
		mValue = Long.valueOf(value);
	}

	public OLong(String value) {
		mType = PrimitiveType.STRING_LONG;
		mValue = Long.valueOf(value);
	}

	public long longValue() {
		return mValue.longValue();
	}

	protected void init(OField itemField) {
		if (itemField != null) {
			Primitive t = itemField.getAnnotation(Primitive.class);
			if (t != null) {
				mType = t.value();
			}
		}
	}

	@Override
	public IJsonItem fromJson(Object json, OField itemField) {
		init(itemField);
		try {
			mValue = Long.valueOf(json.toString());
		} catch (Exception e) {
			return null;
		}
		return this;
	}

	@Override
	public Object toJson(OField itemField) {
		init(itemField);
		if (mType == PrimitiveType.STRING_LONG) {
			return mValue.toString();
		} else {
			return mValue;
		}
	}

	@Override
	public IXmlItem fromXml(Node xml, OField itemField) {
		init(itemField);
		try {
			mValue = Long.valueOf(xml.getTextContent());
		} catch (Exception e) {
			return null;
		}
		return this;
	}

	@Override
	public Node toXml(Document doc, OField itemField) {
		init(itemField);
		Node node = XmlUtil.newNode(doc, itemField);
		node.setTextContent(mValue.toString());
		return node;
	}

	@Override
	public IPropertyItem fromProperty(String value, OField itemField) {
		init(itemField);
		try {
			mValue = Long.valueOf(value);
		} catch (Exception e) {
			return null;
		}
		return this;
	}

	@Override
	public String toProperty(OField itemField) {
		init(itemField);
		return mValue.toString();
	}

}

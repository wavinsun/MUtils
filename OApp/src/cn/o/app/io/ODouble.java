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
 * Support two data types:{"n":3.14} {"n":"3.14"}
 */
@SuppressWarnings("serial")
public class ODouble extends Serial<Double> {

	public ODouble() {
		mType = PrimitiveType.STRING_DOUBLE;
		mValue = Double.valueOf(0D);
	}

	public ODouble(double value) {
		mType = PrimitiveType.STRING_DOUBLE;
		mValue = Double.valueOf(value);
	}

	public ODouble(String value) {
		mType = PrimitiveType.STRING_DOUBLE;
		mValue = Double.valueOf(value);
	}

	public double doubleValue() {
		return mValue.doubleValue();
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
			mValue = Double.valueOf(json.toString());
		} catch (Exception e) {
			return null;
		}
		return this;
	}

	@Override
	public Object toJson(OField itemField) {
		init(itemField);
		if (mType == PrimitiveType.STRING_DOUBLE) {
			return mValue.toString();
		} else {
			return mValue;
		}
	}

	@Override
	public IXmlItem fromXml(Node xml, OField itemField) {
		init(itemField);
		try {
			mValue = Double.valueOf(xml.getTextContent());
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
			mValue = Double.valueOf(value);
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

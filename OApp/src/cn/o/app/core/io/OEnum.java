package cn.o.app.core.io;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import cn.o.app.core.annotation.Primitive;
import cn.o.app.core.annotation.Primitive.PrimitiveType;
import cn.o.app.core.json.IJsonItem;
import cn.o.app.core.properties.IPropertyItem;
import cn.o.app.core.runtime.OField;
import cn.o.app.core.runtime.ReflectUtil;
import cn.o.app.core.xml.IXmlItem;
import cn.o.app.core.xml.XmlUtil;

/**
 * Support three data types:{"state":"INIT"} {"state":"0"} {"state":0}
 */
@SuppressWarnings({ "serial", "unchecked" })
public abstract class OEnum<E extends Enum<E>> extends Serial<Enum<E>> {

	public OEnum() {
		mType = PrimitiveType.INT;
		mValue = valueOf(0);
	}

	public OEnum(int value) {
		mType = PrimitiveType.INT;
		mValue = valueOf(value);
	}

	public OEnum(String name) {
		mType = PrimitiveType.INT;
		mValue = (E) ReflectUtil.valueOfEnum(valueOf(0).getClass(), name);
	}

	public OEnum(E value) {
		mType = PrimitiveType.INT;
		mValue = value;
	}

	/**
	 * Override this method if you want to get integer
	 * value:@Primitive(PrimitiveType.INT) @Primitive(PrimitiveType.STRING_INT)
	 * 
	 * @return
	 */
	public abstract int intValue();

	/**
	 * Override this method if you want to get integer
	 * value:@Primitive(PrimitiveType.INT) @Primitive(PrimitiveType.STRING_INT)
	 * 
	 * @return
	 */
	public abstract E valueOf(int value);

	public E toEnum() {
		return (E) mValue;
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
			if (mType == PrimitiveType.STRING) {
				mValue = (E) ReflectUtil.valueOfEnum(valueOf(0).getClass(), json.toString());
			} else {
				mValue = valueOf(Integer.parseInt(json.toString()));
			}
		} catch (Exception e) {
			return null;
		}
		return this;
	}

	@Override
	public Object toJson(OField itemField) {
		init(itemField);
		if (mType == PrimitiveType.STRING) {
			return mValue == null ? null : mValue.toString();
		} else {
			int intValue = this.intValue();
			return mType == PrimitiveType.INT ? intValue : (intValue + "");
		}
	}

	@Override
	public IXmlItem fromXml(Node xml, OField itemField) {
		init(itemField);
		try {
			if (mType == PrimitiveType.STRING) {
				mValue = (E) ReflectUtil.valueOfEnum(valueOf(0).getClass(), xml.getTextContent());
			} else {
				mValue = valueOf(Integer.parseInt(xml.getTextContent()));
			}
		} catch (Exception e) {
			return null;
		}
		return this;
	}

	@Override
	public Node toXml(Document doc, OField itemField) {
		init(itemField);
		if (mType == PrimitiveType.STRING) {
			if (mValue == null) {
				return doc.createElement(XmlUtil.TAG_NULL);
			} else {
				Node node = XmlUtil.newNode(doc, itemField);
				node.setTextContent(mValue.toString());
				return node;
			}
		} else {
			Node node = XmlUtil.newNode(doc, itemField);
			node.setTextContent(this.intValue() + "");
			return node;
		}
	}

	@Override
	public IPropertyItem fromProperty(String value, OField itemField) {
		init(itemField);
		try {
			if (mType == PrimitiveType.STRING) {
				mValue = (E) ReflectUtil.valueOfEnum(valueOf(0).getClass(), value);
			} else {
				mValue = valueOf(Integer.parseInt(value));
			}
		} catch (Exception e) {
			return null;
		}
		return this;
	}

	@Override
	public String toProperty(OField itemField) {
		if (mType == PrimitiveType.STRING) {
			return mValue == null ? null : mValue.toString();
		} else {
			return this.intValue() + "";
		}
	}

}

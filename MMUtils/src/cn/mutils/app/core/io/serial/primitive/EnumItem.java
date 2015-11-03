package cn.mutils.app.core.io.serial.primitive;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import cn.mutils.app.core.annotation.Primitive;
import cn.mutils.app.core.annotation.Primitive.PrimitiveType;
import cn.mutils.app.core.beans.BeanField;
import cn.mutils.app.core.io.serial.Serial;
import cn.mutils.app.core.json.IJsonItem;
import cn.mutils.app.core.properties.IPropertyItem;
import cn.mutils.app.core.reflect.ReflectUtil;
import cn.mutils.app.core.xml.IXmlItem;
import cn.mutils.app.core.xml.XmlUtil;

/**
 * Support three data types:{"state":"INIT"} {"state":"0"} {"state":0}
 */
@SuppressWarnings({ "serial", "unchecked" })
public abstract class EnumItem<E extends Enum<E>> extends Serial<Enum<E>> {

	public EnumItem() {
		mType = PrimitiveType.INT;
		mValue = valueOf(0);
	}

	public EnumItem(int value) {
		mType = PrimitiveType.INT;
		mValue = valueOf(value);
	}

	public EnumItem(String name) {
		mType = PrimitiveType.INT;
		mValue = (E) ReflectUtil.valueOfEnum(valueOf(0).getClass(), name);
	}

	public EnumItem(E value) {
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
	public Object toJson(BeanField itemField) {
		init(itemField);
		if (mType == PrimitiveType.STRING) {
			return mValue == null ? null : mValue.toString();
		} else {
			int intValue = this.intValue();
			return mType == PrimitiveType.INT ? intValue : (intValue + "");
		}
	}

	@Override
	public IXmlItem fromXml(Node xml, BeanField itemField) {
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
	public Node toXml(Document doc, BeanField itemField) {
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
	public IPropertyItem fromProperty(String value, BeanField itemField) {
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
	public String toProperty(BeanField itemField) {
		if (mType == PrimitiveType.STRING) {
			return mValue == null ? null : mValue.toString();
		} else {
			return this.intValue() + "";
		}
	}

}

package cn.o.app.io;

import java.lang.reflect.Type;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import cn.o.app.json.IJsonItem;
import cn.o.app.json.JsonUtil;
import cn.o.app.properties.IPropertyItem;
import cn.o.app.runtime.OField;
import cn.o.app.runtime.ReflectUtil;
import cn.o.app.xml.IXmlItem;
import cn.o.app.xml.XmlUtil;

/**
 * Support JSON string:{"k":"{"k":0}"}
 */
@SuppressWarnings({ "serial", "unchecked" })
public class StringJson<T> extends Serial<T> {

	public StringJson() {
		super();
	}

	public StringJson(T value) {
		super(value);
	}

	@Override
	public String toString() {
		try {
			return JsonUtil.convert(mValue);
		} catch (Exception e) {
			return null;
		}
	}

	protected T valueOf(String json, OField itemField) {
		try {
			Class<?> itemClass = itemField == null ? this.getClass() : itemField.getType();
			Type genericType = itemField == null ? null : itemField.getGenericType();
			Class<T> targetClass = (Class<T>) ReflectUtil.getParameterizedClass(itemClass, genericType, 0);
			Type targetType = ReflectUtil.getParameterizedType(itemClass, genericType, 0);
			return JsonUtil.convert(json.toString(), targetClass, targetType);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public IJsonItem fromJson(Object json, OField itemField) {
		mValue = valueOf(json.toString(), itemField);
		if (mValue == null) {
			return null;
		}
		return this;
	}

	@Override
	public Object toJson(OField itemField) {
		return toString();
	}

	@Override
	public IXmlItem fromXml(Node xml, OField itemField) {
		mValue = valueOf(xml.getTextContent(), itemField);
		if (mValue == null) {
			return null;
		}
		return this;
	}

	@Override
	public Node toXml(Document doc, OField itemField) {
		String str = toString();
		if (str == null) {
			return doc.createElement(XmlUtil.TAG_NULL);
		}
		Node node = XmlUtil.newNode(doc, itemField);
		node.setTextContent(str);
		return node;
	}

	@Override
	public IPropertyItem fromProperty(String value, OField itemField) {
		mValue = valueOf(value, itemField);
		return this;
	}

	@Override
	public String toProperty(OField itemField) {
		return toString();
	}

}

package cn.mutils.core.io.serial;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.lang.reflect.Type;

import cn.mutils.core.beans.BeanField;
import cn.mutils.core.json.IJsonItem;
import cn.mutils.core.json.JsonUtil;
import cn.mutils.core.properties.IPropertyItem;
import cn.mutils.core.reflect.ReflectUtil;
import cn.mutils.core.xml.IXmlItem;
import cn.mutils.core.xml.XmlUtil;

/**
 * Support JSON string:{"k":"{"k":0}"}
 */
@SuppressWarnings({"serial", "unchecked", "unused"})
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
            return JsonUtil.toString(mValue);
        } catch (Exception e) {
            return null;
        }
    }

    protected T valueOf(String json, BeanField itemField) {
        try {
            Class<?> itemClass = itemField == null ? this.getClass() : itemField.getRawType();
            Type genericType = itemField == null ? null : itemField.getGenericType();
            Class<T> targetClass = (Class<T>) ReflectUtil.getParamRawType(itemClass, genericType, 0);
            Type targetType = ReflectUtil.getParamGenericType(itemClass, genericType, 0);
            return JsonUtil.fromString(json, targetClass, targetType);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public IJsonItem fromJson(Object json, BeanField itemField) {
        mValue = valueOf(json.toString(), itemField);
        if (mValue == null) {
            return null;
        }
        return this;
    }

    @Override
    public Object toJson(BeanField itemField) {
        return toString();
    }

    @Override
    public IXmlItem fromXml(Node xml, BeanField itemField) {
        mValue = valueOf(xml.getTextContent(), itemField);
        if (mValue == null) {
            return null;
        }
        return this;
    }

    @Override
    public Node toXml(Document doc, BeanField itemField) {
        String str = toString();
        if (str == null) {
            return doc.createElement(XmlUtil.TAG_NULL);
        }
        Node node = XmlUtil.newNode(doc, itemField);
        node.setTextContent(str);
        return node;
    }

    @Override
    public IPropertyItem fromProperty(String value, BeanField itemField) {
        mValue = valueOf(value, itemField);
        return this;
    }

    @Override
    public String toProperty(BeanField itemField) {
        return toString();
    }

}

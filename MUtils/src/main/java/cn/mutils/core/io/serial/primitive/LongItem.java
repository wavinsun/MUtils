package cn.mutils.core.io.serial.primitive;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import cn.mutils.core.annotation.Primitive;
import cn.mutils.core.annotation.PrimitiveType;
import cn.mutils.core.beans.BeanField;
import cn.mutils.core.io.serial.Serial;
import cn.mutils.core.json.IJsonItem;
import cn.mutils.core.properties.IPropertyItem;
import cn.mutils.core.xml.IXmlItem;
import cn.mutils.core.xml.XmlUtil;

/**
 * Support two data types:{"n":"0"} {"n":0}
 */
@SuppressWarnings({"serial", "UnnecessaryBoxing", "unused", "UnnecessaryUnboxing"})
public class LongItem extends Serial<Long> {

    public LongItem() {
        mType = PrimitiveType.STRING_LONG;
        mValue = Long.valueOf(0L);
    }

    public LongItem(long value) {
        mType = PrimitiveType.STRING_LONG;
        mValue = Long.valueOf(value);
    }

    public LongItem(String value) {
        mType = PrimitiveType.STRING_LONG;
        mValue = Long.valueOf(value);
    }

    public long longValue() {
        return mValue.longValue();
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
            mValue = Long.valueOf(json.toString());
        } catch (Exception e) {
            return null;
        }
        return this;
    }

    @Override
    public Object toJson(BeanField itemField) {
        init(itemField);
        if (mType == PrimitiveType.STRING_LONG) {
            return mValue.toString();
        } else {
            return mValue;
        }
    }

    @Override
    public IXmlItem fromXml(Node xml, BeanField itemField) {
        init(itemField);
        try {
            mValue = Long.valueOf(xml.getTextContent());
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
            mValue = Long.valueOf(value);
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

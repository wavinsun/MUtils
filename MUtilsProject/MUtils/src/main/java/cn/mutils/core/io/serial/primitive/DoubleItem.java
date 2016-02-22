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
 * Support two data types:{"n":3.14} {"n":"3.14"}
 */
@SuppressWarnings({"serial", "UnnecessaryBoxing", "UnnecessaryUnboxing", "unused"})
public class DoubleItem extends Serial<Double> {

    public DoubleItem() {
        mType = PrimitiveType.STRING_DOUBLE;
        mValue = Double.valueOf(0D);
    }

    public DoubleItem(double value) {
        mType = PrimitiveType.STRING_DOUBLE;
        mValue = Double.valueOf(value);
    }

    public DoubleItem(String value) {
        mType = PrimitiveType.STRING_DOUBLE;
        mValue = Double.valueOf(value);
    }

    public double doubleValue() {
        return mValue.doubleValue();
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
            mValue = Double.valueOf(json.toString());
        } catch (Exception e) {
            return null;
        }
        return this;
    }

    @Override
    public Object toJson(BeanField itemField) {
        init(itemField);
        if (mType == PrimitiveType.STRING_DOUBLE) {
            return mValue.toString();
        } else {
            return mValue;
        }
    }

    @Override
    public IXmlItem fromXml(Node xml, BeanField itemField) {
        init(itemField);
        try {
            mValue = Double.valueOf(xml.getTextContent());
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
            mValue = Double.valueOf(value);
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

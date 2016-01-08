package cn.mutils.app.core.io.serial.primitive;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import cn.mutils.app.core.annotation.False;
import cn.mutils.app.core.annotation.Primitive;
import cn.mutils.app.core.annotation.Primitive.PrimitiveType;
import cn.mutils.app.core.annotation.True;
import cn.mutils.app.core.beans.BeanField;
import cn.mutils.app.core.io.serial.Serial;
import cn.mutils.app.core.json.IJsonItem;
import cn.mutils.app.core.properties.IPropertyItem;
import cn.mutils.app.core.xml.IXmlItem;
import cn.mutils.app.core.xml.XmlUtil;

/**
 * Support two data types: {"isOK":true} {"isOK":"true"}
 */
@SuppressWarnings({"serial", "UnnecessaryBoxing"})
public class BoolItem extends Serial<Boolean> {

    protected String mTrue = "true";

    protected String mFalse = "false";

    public BoolItem() {
        mType = PrimitiveType.STRING_BOOL;
        mValue = Boolean.FALSE;
    }

    public BoolItem(boolean value) {
        mType = PrimitiveType.STRING_BOOL;
        mValue = Boolean.valueOf(value);
    }

    public BoolItem(String value) {
        mType = PrimitiveType.STRING_BOOL;
        mValue = Boolean.valueOf(value);
    }

    public boolean booleanValue() {
        return mValue.booleanValue();
    }

    protected void init(BeanField itemField) {
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
    public IJsonItem fromJson(Object json, BeanField itemField) {
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
    public Object toJson(BeanField itemField) {
        init(itemField);
        if (mType == PrimitiveType.STRING_BOOL) {
            return mValue ? mTrue : mFalse;
        } else {
            return mValue;
        }
    }

    @Override
    public IXmlItem fromXml(Node xml, BeanField itemField) {
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
    public Node toXml(Document doc, BeanField itemField) {
        init(itemField);
        Node node = XmlUtil.newNode(doc, itemField);
        node.setTextContent(mValue ? mTrue : mFalse);
        return node;
    }

    @Override
    public IPropertyItem fromProperty(String value, BeanField itemField) {
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
    public String toProperty(BeanField itemField) {
        init(itemField);
        return mValue ? mTrue : mFalse;
    }

}

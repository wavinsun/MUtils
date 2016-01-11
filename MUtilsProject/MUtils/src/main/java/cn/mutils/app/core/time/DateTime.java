package cn.mutils.app.core.time;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import cn.mutils.app.core.annotation.Format;
import cn.mutils.app.core.annotation.Primitive;
import cn.mutils.app.core.annotation.Primitive.PrimitiveType;
import cn.mutils.app.core.beans.BeanField;
import cn.mutils.app.core.json.IJsonItem;
import cn.mutils.app.core.properties.IPropertyItem;
import cn.mutils.app.core.xml.IXmlItem;
import cn.mutils.app.core.xml.XmlUtil;

/**
 * Support two data types:{"date":"2015-05-21 13:39:40"} {"date":1432186780000}
 */
@SuppressWarnings({"serial", "unused"})
public class DateTime extends Date implements IJsonItem, IXmlItem, IPropertyItem {

    /**
     * Used to give format of toString() output
     */
    protected String mFormat = "yyyy-MM-dd HH:mm:ss";

    protected PrimitiveType mSerialType = PrimitiveType.STRING;

    /**
     * Used to give format of serialization and deserialization
     */
    protected String mSerialFormat = "yyyy-MM-dd HH:mm:ss";

    public DateTime() {
        super();
    }

    public DateTime(long milliseconds) {
        super(milliseconds);
    }

    public DateTime(String format) {
        this.setFormat(format);
    }

    public DateTime(String format, String text) {
        try {
            this.setTime(new SimpleDateFormat(format, Locale.getDefault()).parse(text).getTime());
            this.setFormat(format);
        } catch (Exception e) {
            //  ParseException
        }
    }

    public String getFormat() {
        return mFormat;
    }

    public void setFormat(String format) {
        mFormat = format;
    }

    @Override
    public String toString() {
        if (mFormat == null) {
            return super.toString();
        } else {
            return DateTimeFormat.format(this, mFormat);
        }
    }

    public PrimitiveType getSerialType() {
        return mSerialType;
    }

    public void setSerialType(PrimitiveType type) {
        mSerialType = type;
    }

    public String getSerialFormat() {
        return mSerialFormat;
    }

    public void setSerialFormat(String format) {
        mSerialFormat = format;
    }

    protected void init(BeanField itemField) {
        if (itemField != null) {
            Primitive t = itemField.getAnnotation(Primitive.class);
            if (t != null) {
                mSerialType = t.value();
            }
            Format f = itemField.getAnnotation(Format.class);
            if (f != null && !f.value().isEmpty()) {
                mSerialFormat = f.value();
            }
        }
    }

    @Override
    public IJsonItem fromJson(Object json, BeanField itemField) {
        init(itemField);
        try {
            if (mSerialType == PrimitiveType.STRING) {
                this.setTime(DateTimeFormat.parse(json.toString(), mSerialFormat).getTime());
            } else {
                this.setTime(Long.parseLong(json.toString()));
            }
        } catch (Exception e) {
            return null;
        }
        return this;
    }

    @Override
    public Object toJson(BeanField itemField) {
        init(itemField);
        if (mSerialType == PrimitiveType.STRING) {
            return DateTimeFormat.format(this, mSerialFormat);
        } else {
            return this.getTime();
        }
    }

    @Override
    public IXmlItem fromXml(Node xml, BeanField itemField) {
        init(itemField);
        try {
            if (mSerialType == PrimitiveType.STRING) {
                this.setTime(DateTimeFormat.parse(xml.getTextContent(), mSerialFormat).getTime());
            } else {
                this.setTime(Long.parseLong(xml.getTextContent()));
            }
        } catch (Exception e) {
            return null;
        }
        return this;
    }

    @Override
    public Node toXml(Document doc, BeanField itemField) {
        init(itemField);
        Node node = XmlUtil.newNode(doc, itemField);
        if (mSerialType == PrimitiveType.STRING) {
            node.setTextContent(DateTimeFormat.format(this, mSerialFormat));
        } else {
            node.setTextContent(this.getTime() + "");
        }
        return node;
    }

    @Override
    public IPropertyItem fromProperty(String value, BeanField itemField) {
        init(itemField);
        try {
            if (mSerialType == PrimitiveType.STRING) {
                this.setTime(DateTimeFormat.parse(value, mSerialFormat).getTime());
            } else {
                this.setTime(Long.parseLong(value));
            }
        } catch (Exception e) {
            return null;
        }
        return this;
    }

    @Override
    public String toProperty(BeanField itemField) {
        init(itemField);
        if (mSerialType == PrimitiveType.STRING) {
            return DateTimeFormat.format(this, mSerialFormat);
        } else {
            return this.getTime() + "";
        }
    }

}

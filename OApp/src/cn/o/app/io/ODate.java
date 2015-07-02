package cn.o.app.io;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Node;

import cn.o.app.annotation.Format;
import cn.o.app.annotation.Primitive;
import cn.o.app.annotation.Primitive.PrimitiveType;
import cn.o.app.json.IJsonItem;
import cn.o.app.properties.IPropertyItem;
import cn.o.app.runtime.OField;
import cn.o.app.text.ODateFormat;
import cn.o.app.xml.IXmlItem;
import cn.o.app.xml.XmlUtil;

/**
 * Support two data types:{"date":"2015-05-21 13:39:40"} {"date":1432186780000}
 */
@SuppressWarnings("serial")
public class ODate extends Date implements IJsonItem, IXmlItem, IPropertyItem {

	/** Used to give format of toString() output */
	protected String mFormat = "yyyy-MM-dd HH:mm:ss";

	protected PrimitiveType mSerialType = PrimitiveType.STRING;

	/** Used to give format of serialization and deserialization */
	protected String mSerialFormat = "yyyy-MM-dd HH:mm:ss";

	public ODate() {
		super();
	}

	public ODate(long milliseconds) {
		super(milliseconds);
	}

	public ODate(String format) {
		this.setFormat(format);
	}

	public ODate(String format, String text) {
		try {
			this.setTime(new SimpleDateFormat(format, Locale.getDefault()).parse(text).getTime());
			this.setFormat(format);
		} catch (Exception e) {

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
			return ODateFormat.format(this, mFormat);
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

	protected void init(OField itemField) {
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
	public IJsonItem fromJson(Object json, OField itemField) {
		init(itemField);
		try {
			if (mSerialType == PrimitiveType.STRING) {
				this.setTime(ODateFormat.parse(json.toString(), mSerialFormat).getTime());
			} else {
				this.setTime(Long.parseLong(json.toString()));
			}
		} catch (Exception e) {
			return null;
		}
		return this;
	}

	@Override
	public Object toJson(OField itemField) {
		init(itemField);
		if (mSerialType == PrimitiveType.STRING) {
			return ODateFormat.format(this, mSerialFormat);
		} else {
			return this.getTime();
		}
	}

	@Override
	public IXmlItem fromXml(Node xml, OField itemField) {
		init(itemField);
		try {
			if (mSerialType == PrimitiveType.STRING) {
				this.setTime(ODateFormat.parse(xml.getTextContent(), mSerialFormat).getTime());
			} else {
				this.setTime(Long.parseLong(xml.getTextContent()));
			}
		} catch (Exception e) {
			return null;
		}
		return this;
	}

	@Override
	public Node toXml(Document doc, OField itemField) {
		init(itemField);
		Node node = XmlUtil.newNode(doc, itemField);
		if (mSerialType == PrimitiveType.STRING) {
			node.setTextContent(ODateFormat.format(this, mSerialFormat));
		} else {
			node.setTextContent(this.getTime() + "");
		}
		return node;
	}

	@Override
	public IPropertyItem fromProperty(String value, OField itemField) {
		init(itemField);
		try {
			if (mSerialType == PrimitiveType.STRING) {
				this.setTime(ODateFormat.parse(value, mSerialFormat).getTime());
			} else {
				this.setTime(Long.parseLong(value));
			}
		} catch (Exception e) {
			return null;
		}
		return this;
	}

	@Override
	public String toProperty(OField itemField) {
		init(itemField);
		if (mSerialType == PrimitiveType.STRING) {
			return ODateFormat.format(this, mSerialFormat);
		} else {
			return this.getTime() + "";
		}
	}

}

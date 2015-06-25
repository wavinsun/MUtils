package cn.o.app.xml;

import cn.o.app.io.INoProguard;

@SuppressWarnings("serial")
public class Xml implements INoProguard {

	public Xml fromString(String xml) {
		try {
			XmlUtil.convert(xml, this);
		} catch (Exception e) {

		}
		return this;
	}

	@Override
	public String toString() {
		try {
			return XmlUtil.toString(XmlUtil.convertToDoc(this));
		} catch (Exception e) {
			return super.toString();
		}
	}

}

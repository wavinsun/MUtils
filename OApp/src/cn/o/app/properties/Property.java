package cn.o.app.properties;

import cn.o.app.io.INoProguard;

@SuppressWarnings("serial")
public class Property implements INoProguard {

	public Property fromString(String properties) {
		try {
			PropertiesUtil.convert(properties, this);
		} catch (Exception e) {

		}
		return this;
	}

	@Override
	public String toString() {
		try {
			return PropertiesUtil.convert(this);
		} catch (Exception e) {
			return super.toString();
		}
	}
}

package cn.o.app.properties;

import cn.o.app.io.INoProguard;
import cn.o.app.runtime.OField;

public interface IPropertyItem extends INoProguard {
	public IPropertyItem fromProperty(String value, OField itemField);

	public String toProperty(OField itemField);
}

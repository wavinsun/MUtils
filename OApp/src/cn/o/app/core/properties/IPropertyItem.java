package cn.o.app.core.properties;

import cn.o.app.core.io.INoProguard;
import cn.o.app.core.runtime.OField;

public interface IPropertyItem extends INoProguard {

	public IPropertyItem fromProperty(String value, OField itemField);

	public String toProperty(OField itemField);

}

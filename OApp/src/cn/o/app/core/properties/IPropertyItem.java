package cn.o.app.core.properties;

import cn.o.app.core.io.INoProguard;
import cn.o.app.core.runtime.BeanField;

public interface IPropertyItem extends INoProguard {

	public IPropertyItem fromProperty(String value, BeanField itemField);

	public String toProperty(BeanField itemField);

}

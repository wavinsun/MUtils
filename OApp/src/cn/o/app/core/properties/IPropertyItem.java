package cn.o.app.core.properties;

import cn.o.app.core.INoProguard;
import cn.o.app.core.beans.BeanField;

public interface IPropertyItem extends INoProguard {

	public IPropertyItem fromProperty(String value, BeanField itemField);

	public String toProperty(BeanField itemField);

}

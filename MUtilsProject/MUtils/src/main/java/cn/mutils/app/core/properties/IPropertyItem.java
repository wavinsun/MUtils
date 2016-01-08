package cn.mutils.app.core.properties;

import cn.mutils.app.core.INoProguard;
import cn.mutils.app.core.beans.BeanField;

public interface IPropertyItem extends INoProguard {

    IPropertyItem fromProperty(String value, BeanField itemField);

    String toProperty(BeanField itemField);

}

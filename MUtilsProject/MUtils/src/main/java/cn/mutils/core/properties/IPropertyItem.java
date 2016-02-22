package cn.mutils.core.properties;

import cn.mutils.core.INoProguard;
import cn.mutils.core.beans.BeanField;

public interface IPropertyItem extends INoProguard {

    IPropertyItem fromProperty(String value, BeanField itemField);

    String toProperty(BeanField itemField);

}

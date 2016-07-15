package cn.mutils.core.properties;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;
import proguard.annotation.KeepImplementations;

import cn.mutils.core.beans.BeanField;

@Keep
@KeepClassMembers
@KeepImplementations
public interface IPropertyItem {

    IPropertyItem fromProperty(String value, BeanField itemField);

    String toProperty(BeanField itemField);

}

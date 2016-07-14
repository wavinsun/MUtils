package cn.mutils.core.json;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;
import proguard.annotation.KeepImplementations;

import cn.mutils.core.beans.BeanField;

@Keep
@KeepClassMembers
@KeepImplementations
public interface IJsonItem {

    IJsonItem fromJson(Object json, BeanField itemField);

    Object toJson(BeanField itemField);

}

package cn.mutils.core.json;

import cn.mutils.core.INoProguard;
import cn.mutils.core.beans.BeanField;

public interface IJsonItem extends INoProguard {

    IJsonItem fromJson(Object json, BeanField itemField);

    Object toJson(BeanField itemField);

}

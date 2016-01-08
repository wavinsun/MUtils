package cn.mutils.app.core.json;

import cn.mutils.app.core.INoProguard;
import cn.mutils.app.core.beans.BeanField;

public interface IJsonItem extends INoProguard {

    IJsonItem fromJson(Object json, BeanField itemField);

    Object toJson(BeanField itemField);

}

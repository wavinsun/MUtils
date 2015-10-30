package cn.mutils.app.core.json;

import cn.mutils.app.core.INoProguard;
import cn.mutils.app.core.beans.BeanField;

public interface IJsonItem extends INoProguard {

	public IJsonItem fromJson(Object json, BeanField itemField);

	public Object toJson(BeanField itemField);

}

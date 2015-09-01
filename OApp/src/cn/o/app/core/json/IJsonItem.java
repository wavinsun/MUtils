package cn.o.app.core.json;

import cn.o.app.core.INoProguard;
import cn.o.app.core.beans.BeanField;

public interface IJsonItem extends INoProguard {

	public IJsonItem fromJson(Object json, BeanField itemField);

	public Object toJson(BeanField itemField);

}

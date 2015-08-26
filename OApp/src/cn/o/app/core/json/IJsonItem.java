package cn.o.app.core.json;

import cn.o.app.core.io.INoProguard;
import cn.o.app.core.runtime.BeanField;

public interface IJsonItem extends INoProguard {

	public IJsonItem fromJson(Object json, BeanField itemField);

	public Object toJson(BeanField itemField);

}

package cn.o.app.json;

import cn.o.app.io.INoProguard;
import cn.o.app.runtime.OField;

public interface IJsonItem extends INoProguard {

	public IJsonItem fromJson(Object json, OField itemField);

	public Object toJson(OField itemField);

}

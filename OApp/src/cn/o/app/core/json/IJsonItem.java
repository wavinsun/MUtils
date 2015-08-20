package cn.o.app.core.json;

import cn.o.app.core.io.INoProguard;
import cn.o.app.core.runtime.OField;

public interface IJsonItem extends INoProguard {

	public IJsonItem fromJson(Object json, OField itemField);

	public Object toJson(OField itemField);

}

package cn.o.app.core.runtime;

import java.lang.reflect.Type;

public class GenericInfo implements IClearable {

	public Class<?> rawType;

	public Type parameterizedType;

	public void clear() {
		rawType = null;
		parameterizedType = null;
	}

}

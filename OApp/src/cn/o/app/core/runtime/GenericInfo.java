package cn.o.app.core.runtime;

import java.lang.reflect.Type;

public class GenericInfo {

	public Class<?> rawType;

	public Type parameterizedType;

	public void clear() {
		rawType = null;
		parameterizedType = null;
	}

}

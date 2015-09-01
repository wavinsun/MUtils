package cn.o.app.core.reflect;

import java.lang.reflect.Type;

import cn.o.app.core.IClearable;

public class GenericInfo implements IClearable {

	public Class<?> rawType;

	public Type parameterizedType;

	public void clear() {
		rawType = null;
		parameterizedType = null;
	}

}

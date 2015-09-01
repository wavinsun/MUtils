package cn.o.app.core.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cn.o.app.core.IClearable;

/**
 * Generic information
 */
public class GenericInfo implements IClearable {

	/**
	 * Raw type
	 */
	public Class<?> rawType;

	/**
	 * Generic type of {@link ParameterizedType}
	 */
	public Type parameterizedType;

	public void clear() {
		rawType = null;
		parameterizedType = null;
	}

}

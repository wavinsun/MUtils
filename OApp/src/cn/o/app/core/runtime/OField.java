package cn.o.app.core.runtime;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.o.app.core.annotation.Ignore;
import cn.o.app.core.annotation.Name;

/**
 * Bean field of framework reflection
 */
public class OField {

	protected static Map<Class<?>, OField[]> sFieldsCache = new ConcurrentHashMap<Class<?>, OField[]>();

	protected Field mField;
	protected Method mGetMethod;
	protected Method mSetMethod;
	protected Class<?> mType;
	protected String mName;
	protected Type mGenericType;

	public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
		if (mField != null) {
			return mField.getAnnotation(annotationType);
		} else {
			return mGetMethod.getAnnotation(annotationType);
		}
	}

	public Type getGenericType() {
		return mGenericType;
	}

	protected void setGenericType(Type genericType) {
		mGenericType = genericType;
	}

	public Class<?> getType() {
		return mType;
	}

	protected void setType(Class<?> cls) {
		mType = cls;
	}

	public String getName() {
		return mName;
	}

	protected void setName(String name) {
		mName = name;
	}

	public Object get(Object object) throws Exception {
		if (mField != null) {
			return mField.get(object);
		} else {
			return mGetMethod.invoke(object);
		}
	}

	public void set(Object object, Object value) throws Exception {
		if (mField != null) {
			mField.set(object, value);
		} else {
			mSetMethod.invoke(object, value);
		}
	}

	protected void setField(Field field) {
		mField = field;
	}

	protected void setGetMethod(Method method) {
		mGetMethod = method;
	}

	protected void setSetMethod(Method method) {
		mSetMethod = method;
	}

	protected static String getFieldNameFromMethod(String method, int startIndex) {
		char[] cs = new char[method.length() - startIndex];
		for (int i = startIndex, j = 0, size = method.length(); i < size; i++, j++) {
			char c = method.charAt(i);
			if (i == startIndex) {
				c = Character.toLowerCase(c);
			}
			cs[j] = c;
		}
		return new String(cs);
	}

	protected static OField[] generateFields(Class<?> cls) {
		List<OField> fields = new ArrayList<OField>();
		Map<String, Object> fieldsMap = new HashMap<String, Object>();
		for (Field f : cls.getFields()) {
			int modifiers = f.getModifiers();
			if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)) {
				continue;
			}
			Ignore ignore = f.getAnnotation(Ignore.class);
			if (ignore != null) {
				continue;
			}
			String name = f.getName();
			Name n = f.getAnnotation(Name.class);
			if (n != null && !n.value().isEmpty()) {
				name = n.value();
			}
			fieldsMap.put(name, null);
			OField field = new OField();
			field.setGenericType(f.getGenericType());
			field.setType(f.getType());
			field.setName(name);
			field.setField(f);
			fields.add(field);
		}
		List<Method> setList = new ArrayList<Method>();
		List<Method> getList = new LinkedList<Method>();
		for (Method m : cls.getMethods()) {
			int modifiers = m.getModifiers();
			if (Modifier.isStatic(modifiers) || Modifier.isFinal(modifiers)) {
				continue;
			}
			Ignore ignore = m.getAnnotation(Ignore.class);
			if (ignore != null) {
				continue;
			}
			String name = m.getName();
			Class<?>[] paramTypes = m.getParameterTypes();
			Class<?> returnType = m.getReturnType();
			if (name.startsWith("set")) {
				if (paramTypes.length != 1 || returnType != Void.TYPE) {
					continue;
				}
				setList.add(m);
			}
			if (name.startsWith("get")) {
				if (paramTypes.length != 0 || returnType == Void.TYPE) {
					continue;
				}
				getList.add(m);
			}
			if (name.startsWith("is")) {
				if (paramTypes.length != 0 || (returnType != Boolean.TYPE && returnType != Boolean.class)) {
					continue;
				}
				getList.add(m);
			}
		}
		for (Method sm : setList) {
			String sName = sm.getName();
			sName = getFieldNameFromMethod(sName, 3);
			Class<?> sType = sm.getParameterTypes()[0];
			for (Method gm : getList) {
				Class<?> gType = gm.getReturnType();
				if (gType != sType) {
					continue;
				}
				String gName = gm.getName();
				if (gType == Boolean.TYPE || gType == Boolean.class) {
					gName = getFieldNameFromMethod(gName, 2);
				} else {
					gName = getFieldNameFromMethod(gName, 3);
				}
				if (sName.equals(gName)) {
					String name = gName;
					Name n = gm.getAnnotation(Name.class);
					if (n != null && !n.value().isEmpty()) {
						name = n.value();
					}
					if (!fieldsMap.containsKey(name)) {
						OField field = new OField();
						field.setGenericType(gm.getGenericReturnType());
						field.setName(name);
						field.setType(gType);
						field.setGetMethod(gm);
						field.setSetMethod(sm);
						fields.add(field);
					}
					getList.remove(gm);
					break;
				}
			}
		}
		setList.clear();
		getList.clear();
		fieldsMap.clear();
		OField[] fieldsArray = new OField[fields.size()];
		fields.toArray(fieldsArray);
		fields.clear();
		return fieldsArray;
	}

	public static OField[] getFields(Class<?> cls) {
		OField[] fields = sFieldsCache.get(cls);
		if (fields == null) {
			fields = generateFields(cls);
			sFieldsCache.put(cls, fields);
		}
		return fields;
	}

	public static OField getField(Class<?> cls, String property) {
		for (OField f : getFields(cls)) {
			String name = f.getName();
			if (name.equals(property)) {
				return f;
			}
		}
		return null;
	}

	public static OField getField(Object obj, String property) {
		if (obj == null) {
			return null;
		}
		return getField(obj.getClass(), property);
	}
}

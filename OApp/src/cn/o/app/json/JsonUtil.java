package cn.o.app.json;

import java.lang.reflect.Type;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import cn.o.app.runtime.OField;
import cn.o.app.runtime.ReflectUtil;

/**
 * JSON serializer of framework
 * 
 * Reflection:int->Integer.TYPE Integer->Integer.class
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class JsonUtil {

	public static <T> T convertFromJson(Object json, Class<T> targetClass) throws Exception {
		return convertFromJson(json, targetClass, null);
	}

	protected static <T> T convertFromJson(Object json, Class<T> targetClass, Type genericType) throws Exception {
		if (JSONObject.NULL.equals(json)) {
			return null;
		}
		if (targetClass == String.class) {
			return (T) ((json instanceof String) ? json : json.toString());
		} else if (targetClass == Integer.TYPE || targetClass == Integer.class) {
			return (T) ((json instanceof Integer) ? json : Integer.valueOf(json.toString()));
		} else if (targetClass == Long.TYPE || targetClass == Long.class) {
			return (T) ((json instanceof Long) ? json : Long.valueOf(json.toString()));
		} else if (targetClass == Double.TYPE || targetClass == Double.class) {
			return (T) ((json instanceof Double) ? json : Double.valueOf(json.toString()));
		} else if (targetClass == Boolean.TYPE || targetClass == Boolean.class) {
			return (T) ((json instanceof Boolean) ? json : Boolean.valueOf(json.toString()));
		} else if (Enum.class.isAssignableFrom(targetClass)) {
			return (T) ReflectUtil.valueOfEnum(targetClass, (json instanceof String) ? (String) json : json.toString());
		} else if (targetClass == JSONObject.class || targetClass == JSONArray.class) {
			return (T) json;
		}
		return convertFromJson(json, targetClass.newInstance(), genericType);
	}

	protected static <T> T convertFromJson(Object json, T target, Type genericType) throws Exception {
		if (JSONObject.NULL.equals(json)) {
			return null;
		}
		if (target instanceof String) {
			return (T) ((json instanceof String) ? json : json.toString());
		} else if (target instanceof Integer) {
			return (T) ((json instanceof Integer) ? json : Integer.valueOf(json.toString()));
		} else if (target instanceof Long) {
			return (T) ((json instanceof Long) ? json : Long.valueOf(json.toString()));
		} else if (target instanceof Double) {
			return (T) ((json instanceof Double) ? json : Double.valueOf(json.toString()));
		} else if (target instanceof Boolean) {
			return (T) ((json instanceof Boolean) ? json : Boolean.valueOf(json.toString()));
		} else if (target instanceof Enum) {
			return (T) ReflectUtil.valueOfEnum(target.getClass(),
					(json instanceof String) ? (String) json : json.toString());
		} else if (target instanceof JSONObject || target instanceof JSONArray) {
			return (T) json;
		} else if (target instanceof List) {
			return (T) convertArrayFromJson(json, (List<?>) target, genericType);
		} else if (target instanceof Map) {
			return (T) convertMapFromJson(json, (Map<?, ?>) target, genericType);
		} else if (target instanceof IJsonItem) {
			return (T) ((IJsonItem) target).fromJson(json, null);
		}
		JSONObject jsonObject = (JSONObject) json;
		for (OField f : OField.getFields(target.getClass())) {
			Object sub = jsonObject.opt(f.getName());
			if (sub == null) {
				continue;
			}
			Class<?> fClass = f.getType();
			f.set(target,
					IJsonItem.class.isAssignableFrom(fClass) ? (((IJsonItem) fClass.newInstance()).fromJson(sub, f))
							: convertFromJson(sub, fClass, f.getGenericType()));
		}
		return target;
	}

	protected static <T extends List> T convertArrayFromJson(Object json, T target, Type genericType) throws Exception {
		Class<?> targetClass = target.getClass();
		Class<?> eClass = ReflectUtil.getListElementClass(targetClass, genericType);
		Type eType = ReflectUtil.getListElementType(targetClass, genericType);
		JSONArray jsonArray = (JSONArray) json;
		for (int i = 0, size = jsonArray.length(); i < size; i++) {
			target.add(convertFromJson(jsonArray.opt(i), eClass, eType));
		}
		return target;
	}

	protected static <T extends Map> T convertMapFromJson(Object json, T target, Type genericType) throws Exception {
		Class<? extends Map> targetClass = target.getClass();
		Class<?> keyClass = ReflectUtil.getMapKeyClass(targetClass, genericType);
		if (!String.class.isAssignableFrom(keyClass)) {
			throw new Exception();
		}
		Class<?> vClass = ReflectUtil.getMapValueClass(targetClass, genericType);
		Type vType = ReflectUtil.getMapValueType(targetClass, genericType);
		JSONObject jsonObject = (JSONObject) json;
		Iterator<String> keys = jsonObject.keys();
		while (keys.hasNext()) {
			String k = keys.next();
			target.put(k, convertFromJson(jsonObject.opt(k), vClass, vType));
		}
		return target;
	}

	public static <T> T convert(String str, Class<T> targetClass) throws Exception {
		return convert(str, targetClass, null);
	}

	public static <T> T convert(String str, Class<T> targetClass, Type genericType) throws Exception {
		if (str == null) {
			return null;
		}
		if (targetClass == String.class) {
			return (T) str;
		}
		return convertFromJson(new JSONTokener(str).nextValue(), targetClass, genericType);
	}

	public static <T> T convert(String str, T target) throws Exception {
		if (target instanceof String) {
			return (T) str;
		}
		return convertFromJson(new JSONTokener(str).nextValue(), target, null);
	}

	public static <T> String convert(T target) throws Exception {
		if (target == null) {
			return null;
		}
		return convertToJson(target).toString();
	}

	public static <T> Object convertToJson(T target) throws Exception {
		if (target == null) {
			return JSONObject.NULL;
		} else if (target instanceof List) {
			return convertArrayToJson(target);
		} else if (target instanceof Map) {
			return convertMapToJson(target);
		} else if (target instanceof IJsonItem) {
			return ((IJsonItem) target).toJson(null);
		} else if (target instanceof JSONObject) {
			return target;
		} else if (target instanceof JSONArray) {
			return target;
		} else if (target instanceof String) {
			return target;
		} else if (target instanceof Integer) {
			return target;
		} else if (target instanceof Long) {
			return target;
		} else if (target instanceof Double) {
			return target;
		} else if (target instanceof Boolean) {
			return target;
		} else if (target instanceof Enum) {
			return target.toString();
		}
		JSONObject json = new JSONObject();
		for (OField f : OField.getFields(target.getClass())) {
			Object v = f.get(target);
			if (v == null) {
				continue;
			}
			json.put(f.getName(), (v instanceof IJsonItem) ? (((IJsonItem) v).toJson(f)) : convertToJson(v));
		}
		return json;
	}

	protected static <T> Object convertArrayToJson(T target) throws Exception {
		JSONArray json = new JSONArray();
		for (Object e : (List<?>) target) {
			json.put(convertToJson(e));
		}
		return json;
	}

	protected static <T> Object convertMapToJson(T target) throws Exception {
		JSONObject json = new JSONObject();
		for (Entry<?, ?> entry : ((Map<?, ?>) target).entrySet()) {
			Object k = entry.getKey();
			if (!(k instanceof String)) {
				break;
			}
			Object v = entry.getValue();
			if (v == null) {
				continue;
			}
			json.put((String) k, convertToJson(v));
		}
		return json;
	}

}
package cn.mutils.app.core.properties;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.Enumeration;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import cn.mutils.app.core.beans.BeanField;
import cn.mutils.app.core.json.JsonUtil;
import cn.mutils.app.core.reflect.ReflectUtil;

/**
 * It will throw exception when you call "Properties.setProperty("...",null)"
 */
@SuppressWarnings({"rawtypes", "unchecked", "CaughtExceptionImmediatelyRethrown"})
public class PropertiesUtil {

    public static Properties newProperties(String properties) throws Exception {
        ByteArrayInputStream bis = new ByteArrayInputStream(properties.getBytes("ISO-8859-1"));
        try {
            Properties p = new Properties();
            p.load(bis);
            return p;
        } catch (Exception e) {
            throw e;
        } finally {
            bis.close();
        }
    }

    public static String toString(Properties p) throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            p.store(bos, null);
            return bos.toString("ISO-8859-1");
        } catch (Exception e) {
            throw e;
        } finally {
            bos.close();
        }
    }

    public static <T> T convertFromProperties(Properties p, Class<T> targetClass) throws Exception {
        return convertFromProperties(p, targetClass.newInstance());
    }

    protected static <T> T convertFromProperties(Properties p, T target) throws Exception {
        if (target instanceof Map) {
            return (T) convertMapFromProperties(p, (Map<?, ?>) target);
        }
        for (BeanField f : BeanField.getFields(target.getClass())) {
            String value = p.getProperty(f.getName());
            if (value == null) {
                continue;
            }
            Class<?> fClass = f.getRawType();
            f.set(target,
                    IPropertyItem.class.isAssignableFrom(fClass)
                            ? (((IPropertyItem) fClass.newInstance()).fromProperty(value, f))
                            : JsonUtil.convert(value, fClass, f.getGenericType()));
        }
        return target;
    }

    protected static <T extends Map> T convertMapFromProperties(Properties p, T target) throws Exception {
        Class<? extends Map> targetClass = target.getClass();
        Class<?> keyClass = ReflectUtil.getMapKeyRawType(targetClass, null);
        if (!String.class.isAssignableFrom(keyClass)) {
            throw new Exception();
        }
        Class<?> vClass = ReflectUtil.getMapValueRawType(targetClass, null);
        Type vType = ReflectUtil.getMapValueGenericType(targetClass, null);
        Enumeration<?> enu = p.propertyNames();
        while (enu.hasMoreElements()) {
            Object k = enu.nextElement();
            target.put(k, JsonUtil.convert(p.getProperty((String) k), vClass, vType));
        }
        return target;
    }

    public static <T> T convert(String str, Class<T> targetClass) throws Exception {
        if (str == null) {
            return null;
        }
        return convertFromProperties(newProperties(str), targetClass);
    }

    public static <T> T convert(String str, T target) throws Exception {
        return convertFromProperties(newProperties(str), target);
    }

    public static <T> String convert(T target) throws Exception {
        if (target == null) {
            return null;
        }
        return toString(convertToProperties(target));
    }

    public static <T> Properties convertToProperties(T target) throws Exception {
        if (target instanceof Map) {
            return convertMapToProperties(target);
        }
        Properties p = new Properties();
        for (BeanField f : BeanField.getFields(target.getClass())) {
            Object v = f.get(target);
            if (v == null) {
                continue;
            }
            p.setProperty(f.getName(),
                    (v instanceof IPropertyItem) ? (((IPropertyItem) v).toProperty(f)) : JsonUtil.convert(v));
        }
        return p;
    }

    protected static <T> Properties convertMapToProperties(T target) throws Exception {
        Properties p = new Properties();
        for (Entry<?, ?> entry : ((Map<?, ?>) target).entrySet()) {
            Object k = entry.getKey();
            if (!(k instanceof String)) {
                break;
            }
            Object v = entry.getValue();
            if (v == null) {
                continue;
            }
            p.setProperty((String) k, JsonUtil.convert(v));
        }
        return p;
    }
}

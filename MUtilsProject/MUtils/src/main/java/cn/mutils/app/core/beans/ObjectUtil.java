package cn.mutils.app.core.beans;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import cn.mutils.app.core.io.IOUtil;
import cn.mutils.app.core.json.JsonUtil;

/**
 * Object utility of framework
 */
@SuppressWarnings("unchecked")
public class ObjectUtil {

    public static Object get(Object object, String property) {
        try {
            return BeanField.getField(object, property).get(object);
        } catch (Exception e) {
            return null;
        }
    }

    public static void set(Object object, String property, Object value) {
        try {
            BeanField.getField(object, property).set(object, value);
        } catch (Exception e) {
            // IllegalAccessException
        }
    }

    /**
     * Deep clone
     */
    public static Object clone(Serializable object) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = null;
        ByteArrayInputStream bis = null;
        ObjectInputStream ois = null;
        try {
            oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            bis = new ByteArrayInputStream(bos.toByteArray());
            ois = new ObjectInputStream(bis);
            return ois.readObject();
        } catch (Exception e) {
            return null;
        } finally {
            IOUtil.close(bos);
            IOUtil.close(oos);
            IOUtil.close(bis);
            IOUtil.close(ois);
        }
    }

    public static <T> T copy(T src) {
        try {
            return (T) JsonUtil.convertFromJson(JsonUtil.convertToJson(src), src.getClass());
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean copy(Object target, Object src) {
        try {
            JsonUtil.convertFromJson(JsonUtil.convertToJson(src), target);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean equals(Object one, Object another) {
        if (one == another) {
            return true;
        }
        if (one == null) {
            return false;
        } else {
            return one.equals(another);
        }
    }

}

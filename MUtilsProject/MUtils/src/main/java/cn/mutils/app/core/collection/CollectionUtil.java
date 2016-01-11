package cn.mutils.app.core.collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.mutils.app.core.beans.BeanField;
import cn.mutils.app.core.beans.ObjectUtil;
import cn.mutils.app.core.reflect.ReflectUtil;
import cn.mutils.app.util.AppUtil;

@SuppressWarnings({"unchecked", "unused"})
public class CollectionUtil {

    public static <T extends List<E>, E> T truncate(T list, int maxSize) {
        if (list == null) {
            return null;
        }
        if (list.size() <= maxSize) {
            return list;
        }
        return (T) list.subList(0, maxSize);
    }

    public static void clear(Object[] array) {
        if (array == null) {
            return;
        }
        for (int i = 0, size = array.length; i < size; i++) {
            array[i] = null;
        }
    }

    public static <T> ArrayList<T> asArrayList(T[] array) {
        ArrayList<T> arrayList = new ArrayList<T>();
        if (array == null) {
            return arrayList;
        }
        Collections.addAll(arrayList, array);
        return arrayList;
    }

    public static <T> T findByProperty(List<T> list, String property, Object propertyValue) {
        if (list == null) {
            return null;
        }
        if (list.size() == 0) {
            return null;
        }
        BeanField propertyField = BeanField.getField(list.get(0), property);
        if (propertyField == null) {
            return null;
        }
        for (T element : list) {
            if (ObjectUtil.equals(propertyValue, ReflectUtil.get(element, propertyField))) {
                return element;
            }
        }
        return null;
    }

    public static <T> List<T> findAllByProperty(List<T> list, String property, Object propertyValue) {
        if (list == null) {
            return null;
        }
        if (list.size() == 0) {
            return null;
        }
        BeanField propertyField = BeanField.getField(list.get(0), property);
        if (propertyField == null) {
            return null;
        }
        List<T> result = new ArrayList<T>();
        for (T element : list) {
            try {
                Object v = propertyField.get(element);
                if (AppUtil.equals(v, propertyValue)) {
                    result.add(element);
                }
            } catch (Exception e) {
                //IllegalAccessException
            }
        }
        if (result.size() != 0) {
            return result;
        }
        return null;
    }

}

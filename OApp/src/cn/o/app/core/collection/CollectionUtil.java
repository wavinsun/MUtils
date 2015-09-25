package cn.o.app.core.collection;

import java.util.ArrayList;
import java.util.List;

import cn.o.app.AppUtil;
import cn.o.app.core.beans.BeanField;
import cn.o.app.core.reflect.ReflectUtil;

public class CollectionUtil {

	public static <T> ArrayList<T> asArrayList(T[] array) {
		ArrayList<T> arrayList = new ArrayList<T>();
		if (array == null) {
			return arrayList;
		}
		for (int i = 0, size = array.length; i < size; i++) {
			arrayList.add(array[i]);
		}
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
			if (AppUtil.equals(propertyValue, ReflectUtil.get(element, propertyField))) {
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

			}
		}
		if (result.size() != 0) {
			return result;
		}
		return null;
	}

}

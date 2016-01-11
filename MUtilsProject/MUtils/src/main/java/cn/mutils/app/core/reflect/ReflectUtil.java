package cn.mutils.app.core.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import cn.mutils.app.core.beans.BeanField;

/**
 * Reflection utility of framework
 */
@SuppressWarnings({"rawtypes", "unchecked", "unused"})
public class ReflectUtil {

    /**
     * Get TypeVariable generic type information
     */
    public static GenericInfo getGenericInfo(String typeVariableName, Class<?> targetClass,
                                             ParameterizedType parameterizedType) {
        GenericInfo gType = new GenericInfo();
        int index = -1;// Type parameter index of target class
        TypeVariable<?>[] typeVariables = targetClass.getTypeParameters();
        for (int i = 0, size = typeVariables.length; i < size; i++) {
            if (typeVariableName.equals(typeVariables[i].getName())) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            Type[] args = parameterizedType.getActualTypeArguments();
            if (index < args.length) {
                Type t = parameterizedType.getActualTypeArguments()[index];
                if (t instanceof Class) {
                    gType.rawType = (Class<?>) t;
                } else if (t instanceof ParameterizedType) {
                    gType.rawType = (Class<?>) ((ParameterizedType) t).getRawType();
                    gType.parameterizedType = t;
                }
            }
        }
        return gType;
    }

    public static <T> Constructor<T> getConstructor(Class<T> targetClass, Class<?>... parameterTypes) {
        try {
            return targetClass.getConstructor(parameterTypes);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> T newInstance(Constructor<T> constructor, Object... args) {
        try {
            return constructor.newInstance(args);
        } catch (Exception e) {
            return null;
        }
    }

    public static Object get(Object target, BeanField field) {
        try {
            return field.get(target);
        } catch (Exception e) {
            return null;
        }
    }

    public static void set(Object target, BeanField field, Object value) {
        try {
            field.set(target, value);
        } catch (Exception e) {
            // IllegalAccessException
        }
    }

    public static Object get(Object target, Field field) {
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            return field.get(target);
        } catch (Exception e) {
            return null;
        }
    }

    public static void set(Object target, Field field, Object value) {
        try {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            field.set(target, value);
        } catch (Exception e) {
            // IllegalAccessException
        }
    }

    public static Object invoke(Object target, Method method, Object... args) {
        try {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }
            return method.invoke(target, args);
        } catch (Exception e) {
            return null;
        }
    }

    public static Class<?> getParameterizedRawType(Class<?> targetClass, Type targetGenericType, int ArgumentIndex) {
        if (targetGenericType == null) {
            targetGenericType = targetClass.getGenericSuperclass();
        } else {
            if (!(targetGenericType instanceof ParameterizedType)) {
                targetGenericType = targetClass.getGenericSuperclass();
            }
        }
        if (targetGenericType instanceof ParameterizedType) {
            Type t = ((ParameterizedType) targetGenericType).getActualTypeArguments()[ArgumentIndex];
            if (t instanceof Class) {
                return (Class<?>) t;
            }
            if (t instanceof ParameterizedType) {
                return (Class<?>) ((ParameterizedType) t).getRawType();
            }
        }
        return String.class;
    }

    public static Type getParameterizedGenericType(Class<?> targetClass, Type targetGenericType, int ArgumentIndex) {
        if (targetGenericType == null) {
            targetGenericType = targetClass.getGenericSuperclass();
        }
        if (targetGenericType instanceof ParameterizedType) {
            return ((ParameterizedType) targetGenericType).getActualTypeArguments()[ArgumentIndex];
        }
        return String.class;
    }

    public static Class<?> getParameterizedRawType(Class<?> targetClass, int ArgumentIndex) {
        return getParameterizedRawType(targetClass, null, ArgumentIndex);
    }

    public static Type getParameterizedGenericType(Class<?> targetClass, int ArgumentIndex) {
        return getParameterizedGenericType(targetClass, null, ArgumentIndex);
    }

    public static Class<?> getCollectionElementRawType(Class<?> collectionClass, Type genericType) {
        return getParameterizedRawType(collectionClass, genericType, 0);
    }

    public static Type getCollectionElementGenericType(Class<?> collectionClass, Type genericType) {
        return getParameterizedGenericType(collectionClass, genericType, 0);
    }

    public static Class<?> getMapKeyRawType(Class<?> mapClass, Type genericType) {
        if (genericType == null) {
            genericType = mapClass.getGenericSuperclass();
        } else {
            if (!(genericType instanceof ParameterizedType)) {
                genericType = mapClass.getGenericSuperclass();
            }
        }
        if (genericType instanceof ParameterizedType) {
            Type t = ((ParameterizedType) genericType).getActualTypeArguments()[0];
            if (t instanceof Class) {
                return (Class<?>) t;
            }
        }
        return Object.class;
    }

    public static Class<?> getMapValueRawType(Class<?> mapClass, Type genericType) {
        return getParameterizedRawType(mapClass, genericType, 1);
    }

    public static Type getMapValueGenericType(Class<?> mapClass, Type genericType) {
        return getParameterizedGenericType(mapClass, genericType, 1);
    }

    public static Object valueOfEnum(Class<?> enumType, String name) {
        try {
            return Enum.valueOf((Class<Enum>) enumType, name);
        } catch (Exception e) {
            return null;
        }
    }
}

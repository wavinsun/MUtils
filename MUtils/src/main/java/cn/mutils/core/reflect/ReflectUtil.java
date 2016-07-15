package cn.mutils.core.reflect;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import cn.mutils.core.beans.BeanField;

/**
 * Reflection utility of framework
 */
@SuppressWarnings({"rawtypes", "unchecked", "unused", "UnusedAssignment", "ConstantConditions", "SimplifiableIfStatement"})
public class ReflectUtil {

    /**
     * Whether it is assignable from one type to another type
     */
    public static boolean isAssignable(Type one, Type another) {
        if (one == another) {
            return true;
        }
        if (one == null || another == null) {
            return false;
        }
        if (one.equals(another)) {
            return true;
        }
        if ((one instanceof ParameterizedType) && (another instanceof ParameterizedType)) {
            ParameterizedType oneParamType = (ParameterizedType) one;
            ParameterizedType anotherParamType = (ParameterizedType) another;
            if (!oneParamType.getRawType().equals(anotherParamType.getRawType())) {
                return false;
            }
            Type oneOwner = oneParamType.getOwnerType();
            Type anotherOwner = anotherParamType.getOwnerType();
            if (oneOwner != null) {
                if (!oneOwner.equals(anotherOwner)) {
                    return false;
                }
            } else {
                if (anotherOwner != null) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public static Class<?> getClass(Type type) {
        if (type.getClass() == Class.class) {
            return (Class<?>) type;
        }
        if (type instanceof ParameterizedType) {
            return ReflectUtil.getClass(((ParameterizedType) type).getRawType());
        }
        return Object.class;
    }

    public static Type getInheritGenericType(Class<?> clazz, TypeVariable<?> typeVariable) {
        Type type = null;
        GenericDeclaration genericDeclaration = typeVariable.getGenericDeclaration();
        do {
            type = clazz.getGenericSuperclass();
            if (type == null) {
                return null;
            }
            if (type instanceof ParameterizedType) {
                ParameterizedType paramType = (ParameterizedType) type;
                if (paramType.getRawType() == genericDeclaration) {
                    TypeVariable<?>[] typeVariables = genericDeclaration.getTypeParameters();
                    Type[] arguments = paramType.getActualTypeArguments();
                    String typeVariableName = typeVariable.getName();
                    for (int i = 0, size = typeVariables.length; i < size; i++) {
                        // Fix bug on android VM
                        // It is true while typeVariable==typeVariables[i] on JVM
                        // But It is false on android VM
                        if (typeVariableName.equals(typeVariables[i].getName())) {
                            return arguments[i];
                        }
                    }
                    return null;
                }
            }
            clazz = ReflectUtil.getClass(type);
        } while (type != null);
        return null;
    }

    public static boolean isGenericParamType(Type type) {
        if (type == null) {
            return false;
        }
        if (type instanceof ParameterizedType) {
            return true;
        }
        if (type instanceof Class) {
            return ReflectUtil.isGenericParamType(((Class<?>) type).getGenericSuperclass());
        }
        return false;
    }

    public static Type getGenericParamType(Type type) {
        if (type instanceof ParameterizedType) {
            return type;
        }
        if (type instanceof Class) {
            return ReflectUtil.getGenericParamType(((Class<?>) type).getGenericSuperclass());
        }
        return type;
    }

    /**
     * Get TypeVariable generic type information
     */
    public static GenericInfo getGenericInfo(String typeVariableName, Class<?> clazz, ParameterizedType paramType) {
        GenericInfo gType = new GenericInfo();
        int index = -1;// Type parameter index of target class
        TypeVariable<?>[] typeVariables = clazz.getTypeParameters();
        for (int i = 0, size = typeVariables.length; i < size; i++) {
            if (typeVariableName.equals(typeVariables[i].getName())) {
                index = i;
                break;
            }
        }
        if (index != -1) {
            Type[] args = paramType.getActualTypeArguments();
            if (index < args.length) {
                Type t = paramType.getActualTypeArguments()[index];
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

    public static <T> Constructor<T> getConstructor(Class<T> targetClass, Class<?>... paramTypes) {
        try {
            return targetClass.getConstructor(paramTypes);
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

    public static Class<?> getParamRawType(Class<?> clazz, Type genericType, int ArgumentIndex) {
        if (genericType == null) {
            genericType = clazz.getGenericSuperclass();
        }
        if (!(genericType instanceof ParameterizedType)) {
            genericType = getGenericParamType(genericType);
        }
        if (genericType instanceof ParameterizedType) {
            Type t = ((ParameterizedType) genericType).getActualTypeArguments()[ArgumentIndex];
            if (t instanceof Class) {
                return (Class<?>) t;
            }
            if (t instanceof ParameterizedType) {
                return (Class<?>) ((ParameterizedType) t).getRawType();
            }
        }
        return Object.class;
    }

    public static Type getParamGenericType(Class<?> clazz, Type genericType, int ArgumentIndex) {
        if (genericType == null) {
            genericType = clazz.getGenericSuperclass();
        }
        if (!(genericType instanceof ParameterizedType)) {
            genericType = getGenericParamType(genericType);
        }
        if (genericType instanceof ParameterizedType) {
            return ((ParameterizedType) genericType).getActualTypeArguments()[ArgumentIndex];
        }
        return Object.class;
    }

    public static Class<?> getParamRawType(Class<?> clazz, int ArgumentIndex) {
        return getParamRawType(clazz, null, ArgumentIndex);
    }

    public static Type getParamGenericType(Class<?> clazz, int ArgumentIndex) {
        return getParamGenericType(clazz, null, ArgumentIndex);
    }

    public static Class<?> getCollectionElementRawType(Class<?> clazz, Type genericType) {
        return getParamRawType(clazz, genericType, 0);
    }

    public static Type getCollectionElementGenericType(Class<?> clazz, Type genericType) {
        return getParamGenericType(clazz, genericType, 0);
    }

    public static Class<?> getMapKeyRawType(Class<?> clazz, Type genericType) {
        return getParamRawType(clazz, genericType, 0);
    }

    public static Class<?> getMapValueRawType(Class<?> clazz, Type genericType) {
        return getParamRawType(clazz, genericType, 1);
    }

    public static Type getMapValueGenericType(Class<?> clazz, Type genericType) {
        return getParamGenericType(clazz, genericType, 1);
    }

    public static Object valueOfEnum(Class<?> clazz, String name) {
        try {
            return Enum.valueOf((Class<Enum>) clazz, name);
        } catch (Exception e) {
            return null;
        }
    }
}

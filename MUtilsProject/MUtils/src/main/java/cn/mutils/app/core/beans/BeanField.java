package cn.mutils.app.core.beans;

import java.lang.annotation.Annotation;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.mutils.app.core.annotation.Ignore;
import cn.mutils.app.core.annotation.Name;
import cn.mutils.app.core.reflect.ArgumentsType;
import cn.mutils.app.core.reflect.GenericInfo;
import cn.mutils.app.core.reflect.ReflectUtil;

/**
 * Bean field of framework reflection
 */
@SuppressWarnings({"unused", "UnusedAssignment", "UnnecessaryLocalVariable"})
public class BeanField {

    protected static Map<Class<?>, BeanField[]> sFieldsCache = new ConcurrentHashMap<Class<?>, BeanField[]>();

    protected Field mField;
    protected Method mGetMethod;
    protected Method mSetMethod;
    protected String mName;
    protected Class<?> mOwnerType; // Class who is field owner
    protected Class<?> mRawType; // Class of field
    protected Type mGenericType; // Generic type of field

    protected WeakReference<Type> mGenericTypeSourceRef; // Cache for source generic type
    protected WeakReference<Class<?>> mRawTypeResultRef; // Cache for result raw type
    protected WeakReference<Type> mGenericTypeResultRef; // Cache for result generic type

    public BeanField(Class<?> ownerType, String name, Field field) {
        mField = field;
        init(ownerType, name);
    }

    public BeanField(Class<?> ownerType, String name, Method getMethod, Method setMethod) {
        mGetMethod = getMethod;
        mSetMethod = setMethod;
        init(ownerType, name);
    }

    protected void init(Class<?> ownerType, String name) {
        mOwnerType = ownerType;
        mName = name;

        // Generate raw type
        Class<?> rawType = null;
        if (mField != null) {
            rawType = mField.getType();
            mGenericType = mField.getGenericType();
        } else {
            rawType = mGetMethod.getReturnType();
            mGenericType = mGetMethod.getGenericReturnType();
        }

        // Fix bug for base class is ParamterizedType while subclass is a normal class
        if (rawType == Object.class && (mGenericType instanceof TypeVariable)) {
            TypeVariable<?> typeVariable = (TypeVariable<?>) mGenericType;
            Type genericFieldType = ReflectUtil.getInheritGenericType(mOwnerType, typeVariable);
            if (genericFieldType != null) {
                mRawType = ReflectUtil.getClass(genericFieldType);
                mGenericType = genericFieldType;
                return;
            }
        }

        if (mGenericType instanceof Class) {
            mRawType = (Class<?>) mGenericType;
        } else {
            if (mGenericType instanceof ParameterizedType) {
                mRawType = (Class<?>) ((ParameterizedType) mGenericType).getRawType();
            } else {
                if (mGenericType instanceof TypeVariable) {
                    Type ownerGenericType = mOwnerType.getGenericSuperclass();
                    if (ownerGenericType instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType) ownerGenericType;

                        // Map<K,V>:
                        // K is a TypeVariable,
                        // It's name is K,It's index is 0.
                        String n = ((TypeVariable<?>) mGenericType).getName();
                        GenericInfo info = ReflectUtil.getGenericInfo(n, mOwnerType.getSuperclass(), pt);
                        if (info.parameterizedType != null) {
                            mGenericType = info.parameterizedType;
                        }
                        mRawType = info.rawType;
                        info.clear();
                    }
                }
                // Error happens when mRawType is null.
            }
        }
    }

    public Class<?> getOwnerType() {
        return mOwnerType;
    }

    public Class<?> getRawType() {
        return getRawType(null);
    }

    // public class Role<ID,NAME> { public ID id; public NAME n; }
    // public class User { public Role<Integer,String> role; }
    // User u = new User();
    // RawType for "u.role.id" is null by normal java reflection
    // RawType for "u.role.id" is expected to be "java.lang.Integer"
    public Class<?> getRawType(Type genericType) {
        if (mRawType != null) {
            return mRawType;
        } else {
            // Get from cache
            if (genericType != null && mGenericTypeSourceRef != null) {
                synchronized (this) {
                    if (ReflectUtil.isAssignable(genericType, mGenericTypeSourceRef.get())) {
                        if (mRawTypeResultRef != null) {
                            Class<?> resultRef = mRawTypeResultRef.get();
                            if (resultRef != null) {
                                return resultRef;
                            }
                        }
                    }
                }
            }

            Class<?> expectedType = null;
            if (genericType != null && (genericType instanceof ParameterizedType)) {
                ParameterizedType pt = (ParameterizedType) genericType;
                if (mOwnerType.isAssignableFrom((Class<?>) pt.getRawType())) {
                    if (mGenericType instanceof TypeVariable) {
                        String n = ((TypeVariable<?>) mGenericType).getName();
                        GenericInfo info = ReflectUtil.getGenericInfo(n, mOwnerType, pt);
                        expectedType = info.rawType;
                        info.clear();
                    }
                }
            }
            expectedType = expectedType != null ? expectedType : Object.class;

            //Put to cache
            if (genericType != null) {
                synchronized (this) {
                    if (mGenericTypeSourceRef != null) {
                        if (!genericType.equals(mGenericTypeSourceRef.get())) {
                            mGenericTypeSourceRef.clear();
                            mGenericTypeSourceRef = null;
                            mGenericTypeResultRef = null;
                            mRawTypeResultRef = null;
                        }
                    } else {
                        mGenericTypeSourceRef = new WeakReference<Type>(genericType);
                    }
                    mRawTypeResultRef = new WeakReference<Class<?>>(expectedType);
                }
            }
            return expectedType;
        }
    }

    public Type getGenericType() {
        return getGenericType(null);
    }

    public Type getGenericType(Type genericType) {
        if (!(mGenericType instanceof TypeVariable)) {
            return mGenericType;
        } else {
            // Get from cache
            if (genericType != null && mGenericTypeSourceRef != null) {
                synchronized (this) {
                    if (ReflectUtil.isAssignable(genericType, mGenericTypeSourceRef.get())) {
                        if (mGenericTypeResultRef != null) {
                            Type resultRef = mGenericTypeResultRef.get();
                            if (resultRef != null) {
                                return resultRef;
                            }
                        }
                    }
                }
            }

            Type expectedType = null;
            if (genericType != null && (genericType instanceof ParameterizedType)) {
                ParameterizedType pt = (ParameterizedType) genericType;
                if (mOwnerType.isAssignableFrom((Class<?>) pt.getRawType())) {
                    if (mGenericType instanceof TypeVariable) {
                        String n = ((TypeVariable<?>) mGenericType).getName();
                        GenericInfo info = ReflectUtil.getGenericInfo(n, mOwnerType, pt);
                        expectedType = info.parameterizedType;
                        info.clear();
                    }
                }
            }
            expectedType = expectedType != null ? expectedType : mGenericType;

            //Put to cache
            if (genericType != null) {
                synchronized (this) {
                    if (mGenericTypeSourceRef != null) {
                        if (!genericType.equals(mGenericTypeSourceRef.get())) {
                            mGenericTypeSourceRef.clear();
                            mGenericTypeSourceRef = null;
                            mGenericTypeResultRef = null;
                            mRawTypeResultRef = null;
                        }
                    } else {
                        mGenericTypeSourceRef = new WeakReference<Type>(genericType);
                    }
                    mGenericTypeResultRef = new WeakReference<Type>(expectedType);
                }
            }
            return expectedType;
        }
    }

    public <T extends Annotation> T getAnnotation(Class<T> annotationType) {
        if (mField != null) {
            return mField.getAnnotation(annotationType);
        } else {
            return mGetMethod.getAnnotation(annotationType);
        }
    }

    public String getName() {
        return mName;
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

    /**
     * Get field type by given class and class type
     */
    protected static Type getFieldType(Class<?> clazz, Type type, Type fieldType) {
        if (clazz == null || type == null) {
            return fieldType;
        }
        if (fieldType instanceof GenericArrayType) {
            GenericArrayType genericArrayType = (GenericArrayType) fieldType;
            Type componentType = genericArrayType.getGenericComponentType();
            Type componentTypeX = getFieldType(clazz, type, componentType);
            if (componentType != componentTypeX) {
                Type fieldTypeX = Array.newInstance(ReflectUtil.getClass(componentTypeX), 0).getClass();
                return fieldTypeX;
            }
            return fieldType;
        }
        if (!ReflectUtil.isGenericParamType(type)) {
            return fieldType;
        }
        if (fieldType instanceof TypeVariable) {
            String typeVariableName = ((TypeVariable<?>) fieldType).getName();
            ParameterizedType paramType = (ParameterizedType) ReflectUtil.getGenericParamType(type);
            Class<?> paramClass = ReflectUtil.getClass(paramType);
            for (TypeVariable<?> t : paramClass.getTypeParameters()) {
                if (typeVariableName.equals(t.getName())) {
                    fieldType = t;
                    return fieldType;
                }
            }
        }
        if ((fieldType instanceof ParameterizedType) && (type instanceof ParameterizedType)) {
            ParameterizedType paramFieldType = (ParameterizedType) fieldType;
            ParameterizedType paramType = (ParameterizedType) type;
            boolean changed = false;
            Type[] arguments = paramFieldType.getActualTypeArguments();
            TypeVariable<?>[] typeVariables = clazz.getTypeParameters();
            for (int i = 0, size4i = arguments.length; i < size4i; i++) {
                Type argument = arguments[i];
                if (argument instanceof TypeVariable) {
                    String typeVariableName = ((TypeVariable<?>) argument).getName();
                    for (int j = 0, size4j = typeVariables.length; j < size4j; j++) {
                        if (typeVariableName.equals(typeVariables[j].getName())) {
                            arguments[i] = paramType.getActualTypeArguments()[j];
                            changed = true;
                        }
                    }
                }
            }
            if (changed) {
                fieldType = new ArgumentsType(arguments, paramType.getOwnerType(), paramType.getRawType());
                return fieldType;
            }
        }
        return fieldType;
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

    protected static BeanField[] generateFields(Class<?> cls) {
        List<BeanField> fields = new ArrayList<BeanField>();
        List<String> fieldNames = new ArrayList<String>();
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
            fields.add(new BeanField(cls, name, f));
            fieldNames.add(name);
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
                    if (!fieldNames.contains(name)) {
                        fields.add(new BeanField(cls, name, gm, sm));
                        fieldNames.add(name);
                    }
                    getList.remove(gm);
                    break;
                }
            }
        }
        setList.clear();
        getList.clear();
        fieldNames.clear();
        BeanField[] fieldsArray = new BeanField[fields.size()];
        fields.toArray(fieldsArray);
        fields.clear();
        return fieldsArray;
    }

    public static BeanField[] getFields(Class<?> cls) {
        BeanField[] fields = sFieldsCache.get(cls);
        if (fields == null) {
            fields = generateFields(cls);
            sFieldsCache.put(cls, fields);
        }
        return fields;
    }

    public static BeanField getField(Class<?> cls, String name) {
        for (BeanField f : getFields(cls)) {
            String n = f.getName();
            if (n.equals(name)) {
                return f;
            }
        }
        return null;
    }

    public static BeanField getField(Object obj, String name) {
        if (obj == null) {
            return null;
        }
        return getField(obj.getClass(), name);
    }
}

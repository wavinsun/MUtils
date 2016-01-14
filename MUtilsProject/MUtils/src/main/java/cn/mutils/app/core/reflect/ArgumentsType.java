package cn.mutils.app.core.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

import cn.mutils.app.core.IClearable;

/**
 * Simple implementation of {@link ParameterizedType}
 */
@SuppressWarnings({"unused", "RedundantIfStatement"})
public class ArgumentsType implements ParameterizedType, IClearable {

    protected Type[] mArguments;

    protected Type mOwnerType;

    protected Type mRawType;

    public ArgumentsType() {

    }

    public ArgumentsType(Type[] arguments, Type ownerType, Type rawType) {
        mArguments = arguments;
        mOwnerType = ownerType;
        mRawType = rawType;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return mArguments;
    }

    public void setActualTypeArguments(Type[] arguments) {
        mArguments = arguments;
    }

    public void setActualTypeArguments(List<Type> arguments) {
        mArguments = (Type[]) arguments.toArray();
    }

    @Override
    public Type getOwnerType() {
        return mOwnerType;
    }

    public void setOwnerType(Type ownerType) {
        mOwnerType = ownerType;
    }

    @Override
    public Type getRawType() {
        return mRawType;
    }

    public void setRawType(Type rawType) {
        mRawType = rawType;
    }

    public void clear() {
        mOwnerType = null;
        mRawType = null;
        if (mArguments != null) {
            Arrays.fill(mArguments, null);
            mArguments = null;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || !(o instanceof ParameterizedType)) {
            return false;
        }
        if (mOwnerType == null || mRawType == null || mArguments == null) {
            return false;
        }
        ParameterizedType paramType = (ParameterizedType) o;
        if (!mOwnerType.equals(paramType.getOwnerType())) {
            return false;
        }
        if (!mRawType.equals(paramType.getRawType())) {
            return false;
        }
        if (!Arrays.equals(mArguments, paramType.getActualTypeArguments())) {
            return false;
        }
        return true;
    }
}

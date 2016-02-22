package cn.mutils.core.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.mutils.core.IClearable;

/**
 * Bean cache of framework
 */
public class BeanCache implements IClearable {

    protected Map<String, PropertyMD5> mCache;
    protected Object mTarget;

    public BeanCache(Object target) {
        mTarget = target;
    }

    public Object getTarget() {
        return mTarget;
    }

    public void setTarget(Object target) {
        mTarget = target;
        if (mCache != null) {
            mCache.clear();
        }
    }

    /**
     * Bring object to cache
     *
     * @return Properties changed
     */
    public List<String> fromTarget() {
        if (mTarget == null) {
            return null;
        }
        if (mCache == null) {
            mCache = new HashMap<String, PropertyMD5>();
        }
        ArrayList<String> changed = new ArrayList<String>();
        for (BeanField f : BeanField.getFields(mTarget.getClass())) {
            try {
                String name = f.getName();
                Object fValue = f.get(mTarget);
                ObjectMD5 md5 = new ObjectMD5(fValue);
                boolean isChanged = false;
                PropertyMD5 propertyMD5 = mCache.get(name);
                if (propertyMD5 == null) {
                    isChanged = true;
                    propertyMD5 = new PropertyMD5();
                    mCache.put(name, propertyMD5);
                } else {
                    if (!propertyMD5.isValid()) {
                        isChanged = true;
                    } else {
                        if (!md5.equals(propertyMD5.mMD5)) {
                            isChanged = true;
                        }
                    }
                }
                if (isChanged) {
                    propertyMD5.mMD5 = md5;
                    propertyMD5.mProperty = fValue;
                    changed.add(name);
                }
            } catch (Exception e) {
                // IllegalAccessException
            }
        }
        return changed.size() != 0 ? changed : null;
    }

    /**
     * Bring cache to target object
     *
     * @return Properties changed
     */
    public List<String> toTarget() {
        if (mTarget == null) {
            return null;
        }
        if (mCache == null) {
            mCache = new HashMap<String, PropertyMD5>();
        }
        ArrayList<String> changed = new ArrayList<String>();
        for (BeanField f : BeanField.getFields(mTarget.getClass())) {
            try {
                String name = f.getName();
                Object fValue = f.get(mTarget);
                ObjectMD5 md5 = new ObjectMD5(fValue);
                boolean isChanged = false;
                PropertyMD5 propertyMD5 = mCache.get(name);
                if (propertyMD5 == null) {
                    isChanged = true;
                } else {
                    if (!propertyMD5.isValid()) {
                        isChanged = true;
                        propertyMD5.mProperty = null;
                    } else {
                        if (!md5.equals(propertyMD5.mMD5)) {
                            isChanged = true;
                            f.set(mTarget, propertyMD5.mProperty);
                        }
                    }
                }
                if (isChanged) {
                    changed.add(name);
                }
            } catch (Exception e) {
                // IllegalAccessException
            }
        }
        return changed.size() != 0 ? changed : null;
    }

    public void clear() {
        clear(null);
    }

    public void clear(List<String> properties) {
        if (mCache == null) {
            return;
        }
        if (properties == null) {
            mCache.clear();
        } else {
            for (String s : properties) {
                mCache.remove(s);
            }
        }
    }

}

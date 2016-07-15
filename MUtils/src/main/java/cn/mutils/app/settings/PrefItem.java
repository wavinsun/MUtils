package cn.mutils.app.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import org.json.JSONObject;

import java.util.List;

import cn.mutils.app.util.AppUtil;
import cn.mutils.core.annotation.Ignore;
import cn.mutils.core.annotation.Primitive;
import cn.mutils.core.annotation.PrimitiveType;
import cn.mutils.core.beans.BeanCache;
import cn.mutils.core.beans.BeanField;
import cn.mutils.core.json.JsonUtil;

/**
 * JOSN or XML serializer for shared preferences
 */
@SuppressWarnings({"serial", "unused"})
public class PrefItem implements IPrefItem {

    /**
     * shared preferences type of MAP
     */
    public static final int TYPE_PREF_MAP = 0;
    /**
     * shared preferences type of XML
     */
    public static final int TYPE_PREF_JSON = 1;

    /**
     * shared preferences type
     */
    protected int mPrefType = TYPE_PREF_MAP;

    protected String mPrefFileName;

    /**
     * Bean cache of shared preferences
     */
    protected BeanCache mPrefCache;

    @Ignore
    public int getPrefType() {
        return mPrefType;
    }

    public void setPrefType(int prefType) {
        mPrefType = prefType;
    }

    @Ignore
    public String getPrefFileName(String prefFileName) {
        return mPrefFileName;
    }

    public void setPrefFileName(String prefFileName) {
        mPrefFileName = prefFileName;
    }

    @Override
    public synchronized boolean getFromPref(Context context) {
        if (mPrefFileName == null) {
            return false;
        }
        if (mPrefCache == null) {
            mPrefCache = new BeanCache(this);
        }
        List<String> changed = mPrefCache.toTarget();
        if (changed == null) {
            return true;
        }
        if (mPrefType == TYPE_PREF_MAP) {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            int itemCount = 0;
            SharedPreferences pref = AppUtil.getPref(context, mPrefFileName);
            for (BeanField f : BeanField.getFields(this.getClass())) {
                String name = f.getName();
                if (!changed.contains(name) || !pref.contains(name)) {
                    continue;
                }
                String str = pref.getString(name, null);
                if (itemCount != 0) {
                    sb.append(",");
                }
                sb.append("\"");
                sb.append(name);
                sb.append("\":");
                boolean isString = String.class.isAssignableFrom(f.getRawType());
                if (!isString) {
                    Primitive t = f.getAnnotation(Primitive.class);
                    if (t != null) {
                        PrimitiveType type = t.value();
                        isString = PrimitiveType.STRING == type || PrimitiveType.STRING_BOOL == type
                                || PrimitiveType.STRING_DOUBLE == type || PrimitiveType.STRING_INT == type
                                || PrimitiveType.STRING_LONG == type;
                    }
                }
                if (isString && str != null) {
                    sb.append("\"");
                }
                sb.append(str);
                if (isString && str != null) {
                    sb.append("\"");
                }
                itemCount++;
            }
            sb.append("}");
            if (itemCount == 0) {
                return true;
            }
            try {
                JsonUtil.fromString(sb.toString(), this);
                mPrefCache.fromTarget();
                return true;
            } catch (Exception e) {
                return false;
            }
        } else if (mPrefType == TYPE_PREF_JSON) {
            try {
                JsonUtil.fromString(AppUtil.getPrefString(context, mPrefFileName, AppUtil.KEY, null), this);
                mPrefCache.fromTarget();
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public synchronized boolean putToPref(Context context) {
        if (mPrefFileName == null) {
            return false;
        }
        if (mPrefCache == null) {
            mPrefCache = new BeanCache(this);
        }
        List<String> changed = mPrefCache.fromTarget();
        if (changed == null) {
            return true;
        }
        if (mPrefType == TYPE_PREF_MAP) {
            try {
                JSONObject jsonObject = (JSONObject) JsonUtil.toJson(this);
                Editor editor = AppUtil.getPref(context, mPrefFileName).edit();
                for (BeanField f : BeanField.getFields(this.getClass())) {
                    String name = f.getName();
                    if (!changed.contains(name)) {
                        continue;
                    }
                    Object fValue = f.get(this);
                    if (fValue == null) {
                        editor.putString(name, null);
                    } else {
                        editor.putString(name, jsonObject.getString(name));
                    }
                }
                editor.commit();
                return true;
            } catch (Exception e) {
                mPrefCache.clear(changed);
                return false;
            }
        } else if (mPrefType == TYPE_PREF_JSON) {
            try {
                AppUtil.setPrefString(context, mPrefFileName, AppUtil.KEY, JsonUtil.toString(this));
                return true;
            } catch (Exception e) {
                mPrefCache.clear(changed);
                return false;
            }
        }
        return false;
    }

    public void clear() {
        if (mPrefCache != null) {
            mPrefCache.clear();
        }
    }

}

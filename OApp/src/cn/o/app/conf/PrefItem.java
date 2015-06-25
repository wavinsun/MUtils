package cn.o.app.conf;

import java.util.List;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import cn.o.app.OUtil;
import cn.o.app.annotation.Ignore;
import cn.o.app.annotation.Primitive;
import cn.o.app.annotation.Primitive.PrimitiveType;
import cn.o.app.json.JsonUtil;
import cn.o.app.runtime.BeanCache;
import cn.o.app.runtime.OField;

@SuppressWarnings("serial")
public class PrefItem implements IPrefItem {

	public static final int TYPE_PREF_MAP = 0;
	public static final int TYPE_PREF_JSON = 1;

	protected int mPrefType = TYPE_PREF_MAP;

	protected String mPrefFileName;

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
			for (OField f : OField.getFields(this.getClass())) {
				String name = f.getName();
				if (!changed.contains(name)) {
					continue;
				}
				String str = OUtil.getPrefString(context, mPrefFileName, name,
						null);
				if (str == null) {
					continue;
				}
				if (itemCount != 0) {
					sb.append(",");
				}
				sb.append("\"");
				sb.append(name);
				sb.append("\":");
				boolean isString = String.class.isAssignableFrom(f.getType());
				if (!isString) {
					Primitive t = f.getAnnotation(Primitive.class);
					if (t != null) {
						isString = PrimitiveType.STRING == t.value();
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
				JsonUtil.convert(sb.toString(), this);
				mPrefCache.fromTarget();
				return true;
			} catch (Exception e) {
				return false;
			}

		} else if (mPrefType == TYPE_PREF_JSON) {
			try {
				JsonUtil.convert(OUtil.getPrefString(context, mPrefFileName,
						OUtil.KEY, null), this);
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
				JSONObject jsonObject = (JSONObject) JsonUtil
						.convertToJson(this);
				Editor editor = OUtil.getPref(context, mPrefFileName).edit();
				for (OField f : OField.getFields(this.getClass())) {
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
				OUtil.setPrefString(context, mPrefFileName, OUtil.KEY,
						JsonUtil.convert(this));
				return true;
			} catch (Exception e) {
				mPrefCache.clear(changed);
				return false;
			}
		}
		return false;
	}

	public void clearCache() {
		if (mPrefCache != null) {
			mPrefCache.clear();
		}
	}

}

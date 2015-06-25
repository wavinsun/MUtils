package cn.o.app.ui.pattern;

import android.content.Context;
import cn.o.app.OUtil;
import cn.o.app.json.JsonUtil;

@SuppressWarnings("serial")
public class PatternSettings implements IPatternSettings {

	public static final int MIN_LENGTH = 4;

	private static final String PATTERN_FILENAME = "PatternSettings";

	// 用户
	public String id = "";

	// 密码
	public String mm = "";

	// 是否启用
	public boolean qy = false;

	// 周期频率
	public int zq = 0;

	public PatternSettings() {

	}

	@Override
	public String getUserId() {
		return id;
	}

	@Override
	public void setUserId(String userId) {
		id = userId;
	}

	@Override
	public String getPassword() {
		return mm;
	}

	@Override
	public void setPassword(String password) {
		mm = password;
	}

	@Override
	public boolean isEnabled() {
		return qy;
	}

	@Override
	public void setEnabled(boolean enabled) {
		qy = enabled;
	}

	@Override
	public int getPeriod() {
		return zq;
	}

	@Override
	public void setPeriod(int period) {
		zq = period;
	}

	@Override
	public boolean load(Context context, String userId) {
		id = userId;
		mm = "";
		qy = false;
		zq = 0;
		String json = OUtil.getPrefString(context, PATTERN_FILENAME, id, "");
		if (json.isEmpty()) {
			return false;
		}
		try {
			JsonUtil.convert(json, this);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@Override
	public boolean save(Context context) {
		if (id.isEmpty()) {
			return false;
		}
		try {
			return OUtil.setPrefString(context, PATTERN_FILENAME, id,
					JsonUtil.convert(this));
		} catch (Exception e) {
			return false;
		}
	}

}

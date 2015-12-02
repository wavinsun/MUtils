package cn.mutils.app.ui.pattern;

import android.content.Context;
import cn.mutils.app.core.annotation.Ignore;
import cn.mutils.app.core.json.JsonUtil;
import cn.mutils.app.util.AppUtil;

@SuppressWarnings("serial")
public class PatternSettings implements IPatternSettings {

	public static final int MIN_LENGTH = 4;

	private static final String PATTERN_FILENAME = "PatternSettings";

	/** Identity */
	public String id = "";

	/** Password */
	public String mm = "";

	/** Enabled */
	public boolean qy = false;

	/** Period */
	public int zq = 0;

	public PatternSettings() {

	}

	@Ignore
	@Override
	public String getUserId() {
		return id;
	}

	@Override
	public void setUserId(String userId) {
		id = userId;
	}

	@Ignore
	@Override
	public String getPassword() {
		return mm;
	}

	@Override
	public void setPassword(String password) {
		mm = password;
	}

	@Ignore
	@Override
	public boolean isEnabled() {
		return qy;
	}

	@Override
	public void setEnabled(boolean enabled) {
		qy = enabled;
	}

	@Ignore
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
		String json = AppUtil.getPrefString(context, PATTERN_FILENAME, id, "");
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
			return AppUtil.setPrefString(context, PATTERN_FILENAME, id, JsonUtil.convert(this));
		} catch (Exception e) {
			return false;
		}
	}

}

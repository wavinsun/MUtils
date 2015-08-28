package cn.o.app.io;

import android.content.Intent;
import android.os.Bundle;
import cn.o.app.AppUtil;
import cn.o.app.core.io.INoProguard;
import cn.o.app.core.json.JsonUtil;

/**
 * Extra of framework.<br>
 * JSON content for {@link Intent} and {@link Bundle}.
 */
@SuppressWarnings("serial")
public class Extra implements INoProguard {

	public boolean getFrom(Intent intent) {
		try {
			JsonUtil.convert(intent.getStringExtra(AppUtil.KEY), this);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean putTo(Intent intent) {
		try {
			intent.putExtra(AppUtil.KEY, JsonUtil.convert(this));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean getFrom(Bundle bundle) {
		try {
			JsonUtil.convert(bundle.getString(AppUtil.KEY), this);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean putTo(Bundle bundle) {
		try {
			bundle.putString(AppUtil.KEY, JsonUtil.convert(this));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}

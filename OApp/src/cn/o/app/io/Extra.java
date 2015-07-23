package cn.o.app.io;

import android.content.Intent;
import android.os.Bundle;
import cn.o.app.OUtil;
import cn.o.app.json.JsonUtil;

/**
 * Extra of framework
 * 
 * JSON content
 * 
 */
@SuppressWarnings("serial")
public class Extra implements INoProguard {

	public boolean getFrom(Intent intent) {
		try {
			JsonUtil.convert(intent.getStringExtra(OUtil.KEY), this);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean putTo(Intent intent) {
		try {
			intent.putExtra(OUtil.KEY, JsonUtil.convert(this));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean getFrom(Bundle bundle) {
		try {
			JsonUtil.convert(bundle.getString(OUtil.KEY), this);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public boolean putTo(Bundle bundle) {
		try {
			bundle.putString(OUtil.KEY, JsonUtil.convert(this));
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
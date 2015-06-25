package cn.o.app.json;

import cn.o.app.io.INoProguard;

@SuppressWarnings("serial")
public class Json implements INoProguard {

	public Json fromString(String json) {
		try {
			JsonUtil.convert(json, this);
		} catch (Exception e) {

		}
		return this;
	}

	@Override
	public String toString() {
		try {
			return JsonUtil.convert(this);
		} catch (Exception e) {
			return super.toString();
		}
	}

}

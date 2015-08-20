package cn.o.app.demo.net;

import cn.o.app.core.annotation.Name;
import cn.o.app.core.annotation.net.Head;
import cn.o.app.core.io.INoProguard;

@SuppressWarnings("serial")
public class BasicRequest implements INoProguard {

	protected String mAPIKey = "69ccc6b5fcd7d3bafa24c27c95f9fab0";

	@Head
	@Name("apikey")
	public String getAPIKey() {
		return mAPIKey;
	}

	public void setAPIKey(String APIKey) {
		mAPIKey = APIKey;
	}

}

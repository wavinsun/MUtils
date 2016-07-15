package cn.mutils.app.demo.net;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

import cn.mutils.core.annotation.Name;
import cn.mutils.core.annotation.net.Head;

@SuppressWarnings("serial")
@Keep
@KeepClassMembers
public class BasicRequest {

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

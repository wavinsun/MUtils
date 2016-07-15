package cn.mutils.app.demo.net;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

@SuppressWarnings("serial")
@Keep
@KeepClassMembers
public class BasicResponse {

    public static final int CODE_OK = 0;

    protected int mErrNum;

    protected String mErrMsg;

    public int getErrNum() {
        return mErrNum;
    }

    public void setErrNum(int errNum) {
        mErrNum = errNum;
    }

    public String getErrMsg() {
        return mErrMsg;
    }

    public void setErrMsg(String errMsg) {
        mErrMsg = errMsg;
    }

}

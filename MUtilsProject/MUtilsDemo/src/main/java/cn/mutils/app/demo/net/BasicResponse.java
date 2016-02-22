package cn.mutils.app.demo.net;

import cn.mutils.core.INoProguard;

@SuppressWarnings("serial")
public class BasicResponse implements INoProguard {

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

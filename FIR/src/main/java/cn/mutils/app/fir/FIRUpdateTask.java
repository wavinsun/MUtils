package cn.mutils.app.fir;

import proguard.annotation.Keep;
import proguard.annotation.KeepClassMembers;

import cn.mutils.app.net.NetTask;
import cn.mutils.app.fir.FIRUpdateTask.FIRUpdateReq;
import cn.mutils.app.fir.FIRUpdateTask.FIRUpdateRes;
import cn.mutils.core.err.ErrorCodeException;

/**
 * Fly It Remotely version API
 * <p>
 * http://fir.im/dev/api
 */
@SuppressWarnings("serial")
@Keep
@KeepClassMembers
public class FIRUpdateTask extends NetTask<FIRUpdateReq, FIRUpdateRes> {

    @Keep
    @KeepClassMembers
    public static class FIRUpdateReq {
        public String bundle_id;
        public String api_token;
        public String type = "android";
    }

    @SuppressWarnings("unused")
    @Keep
    @KeepClassMembers
    public static class FIRUpdateRes {
        public int code = -1;
        public String message;

        public String name;
        public String version;
        public String changelog;
        public String versionShort;
        public String installUrl;
        public String install_url;
        public String update_url;
        public FirUpdateBinary binary;
    }

    public static class FirUpdateBinary {
        public long fsize;
    }

    public FIRUpdateTask() {
        super();
        setRestUrl(true);
        setUrl("http://api.fir.im/apps/latest/{bundle_id}");
    }

    @Override
    protected void errorCodeVerify(FIRUpdateRes response) throws Exception {
        super.errorCodeVerify(response);
        if (response.code == -1) {
            return;
        }
        throw new ErrorCodeException(response.code, response.message);
    }
}

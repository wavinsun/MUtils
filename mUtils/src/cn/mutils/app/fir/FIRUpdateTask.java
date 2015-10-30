package cn.mutils.app.fir;

import cn.mutils.app.core.INoProguard;
import cn.mutils.app.core.net.NetClient.ErrorCodeException;
import cn.mutils.app.fir.FIRUpdateTask.FIRUpdateReq;
import cn.mutils.app.fir.FIRUpdateTask.FIRUpdateRes;
import cn.mutils.app.net.NetTask;

/**
 * Fly It Remotely version API
 * 
 * http://fir.im/dev/api
 * 
 */
@SuppressWarnings("serial")
public class FIRUpdateTask extends NetTask<FIRUpdateReq, FIRUpdateRes> {

	public static class FIRUpdateReq implements INoProguard {
		public String bundle_id;
		public String api_token;
		public String type = "android";
	}

	public static class FIRUpdateRes implements INoProguard {
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

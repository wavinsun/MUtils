package cn.o.app.fir;

import cn.o.app.fir.FIRUpdateTask.FIRUpdateReq;
import cn.o.app.fir.FIRUpdateTask.FIRUpdateRes;
import cn.o.app.io.INoProguard;
import cn.o.app.net.NetClient.ErrorCodeException;
import cn.o.app.net.NetTask;

/**
 * Fly It Remotely version API
 * 
 * http://fir.im/dev/api
 * 
 */
@SuppressWarnings("serial")
public class FIRUpdateTask extends NetTask<FIRUpdateReq, FIRUpdateRes> {

	public static class FIRUpdateReq implements INoProguard {
		public String idOrAppid;
	}

	public static class FIRUpdateRes implements INoProguard {
		public int code = -1;
		public String message;

		public String name;
		public String version;
		public String changelog;
		public String versionShort;
		public String installUrl;
		public String update_url;
	}

	public FIRUpdateTask() {
		super();
		setRestUrl(true);
		setUrl("http://fir.im/api/v2/app/version/{idOrAppid}");
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

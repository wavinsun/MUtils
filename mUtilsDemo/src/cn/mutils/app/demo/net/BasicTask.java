package cn.mutils.app.demo.net;

import cn.mutils.app.core.net.NetClient.ErrorCodeException;
import cn.mutils.app.net.NetTask;

public class BasicTask<REQUEST, RESPONSE> extends NetTask<REQUEST, RESPONSE> {

	public BasicTask() {
		setCookieId("BAIDUID");
		setCookieWithRequest(false);
		setCookieWithResponse(true);
		mRunInBackground = false;
	}

	@Override
	protected void errorCodeVerify(RESPONSE response) throws Exception {
		if (!(response instanceof BasicResponse)) {
			return;
		}
		BasicResponse res = (BasicResponse) response;
		if (res.getErrNum() == BasicResponse.CODE_OK) {
			return;
		}
		throw new ErrorCodeException(res.getErrNum(), res.getErrMsg());
	}

}

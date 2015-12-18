package cn.mutils.app.demo.web;

import android.content.Context;
import cn.mutils.app.core.annotation.Name;
import cn.mutils.app.demo.web.BasicWebMessage.BasicWebMessageData;
import cn.mutils.app.demo.web.BasicWebMessage.BasicWebMessageResult;
import cn.mutils.app.demo.web.GetAppInfoWebDispatcher.GetAppInfoWebData;
import cn.mutils.app.demo.web.GetAppInfoWebDispatcher.GetAppInfoWebResult;
import cn.mutils.app.ui.web.WebMessageState;
import cn.mutils.app.util.AppUtil;

@SuppressWarnings("serial")
public class GetAppInfoWebDispatcher extends BasicWebMessageDispatcher<GetAppInfoWebData, GetAppInfoWebResult> {

	public static class GetAppInfoWebData extends BasicWebMessageData {

	}

	public static class GetAppInfoWebResult extends BasicWebMessageResult {

		public String name;

		@Name("package")
		public String packageName;

		public String version;

	}

	@Override
	public int messageId() {
		return 99;
	}

	@Override
	public String messageName() {
		return "getAppInfo";
	}

	@Override
	public void onMessage(BasicWebMessage<GetAppInfoWebData, GetAppInfoWebResult> message) {
		Context context = getContext();
		if (message.callbacker != null) {
			message.result = new GetAppInfoWebResult();
			message.result.state = WebMessageState.complete;
			message.result.name = AppUtil.getAppName(context);
			message.result.packageName = AppUtil.getAppPackageName(context);
			message.result.version = AppUtil.getAppVersionName(context);
			this.notifyManager(message);
		}
	}

}

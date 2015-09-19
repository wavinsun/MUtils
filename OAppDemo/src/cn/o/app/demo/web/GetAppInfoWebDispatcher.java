package cn.o.app.demo.web;

import android.content.Context;
import cn.o.app.AppUtil;
import cn.o.app.core.annotation.Name;
import cn.o.app.core.json.JsonUtil;
import cn.o.app.demo.web.GetAppInfoWebDispatcher.GetAppInfoWebMessage;
import cn.o.app.ui.web.WebMessageState;

@SuppressWarnings("serial")
public class GetAppInfoWebDispatcher extends BasicWebMessageDispatcher<GetAppInfoWebMessage> {

	public static final int WEB_MESSAGE_ID = 99;
	public static final String WEB_MESSAGE_NAME = "getAppInfo";

	public static class GetAppInfoWebMessage extends BasicWebMessage {
		public GetAppInfoWebData data;
		public GetAppInfoWebResult result;
	}

	public static class GetAppInfoWebData {

	}

	public static class GetAppInfoWebResult extends BasicWebMessageResult {

		public String name;

		@Name("package")
		public String packageName;

		public String version;

	}

	@Override
	public boolean preTranslateMessage() {
		if (id != null) {
			if (id.equals(WEB_MESSAGE_ID)) {
				return false;
			}
		}
		if (name != null) {
			if (name.equals(WEB_MESSAGE_NAME)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public GetAppInfoWebMessage translateMessage() {
		GetAppInfoWebMessage msg = new GetAppInfoWebMessage();
		msg.id = id;
		msg.name = name;
		msg.callbacker = callbacker;
		msg.data = new GetAppInfoWebData();
		try {
			JsonUtil.convertFromJson(data, msg.data);
			return msg;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void onMessage(GetAppInfoWebMessage message) {
		Context context = getContext();
		if (message.callbacker != null) {
			message.result = new GetAppInfoWebResult();
			message.result.state = WebMessageState.complete;
			message.result.name = AppUtil.getAppName(context);
			message.result.packageName = AppUtil.getAppPackage(context);
			message.result.version = AppUtil.getAppVersionName(context);
			this.notifyManager(message);
		}
	}

}

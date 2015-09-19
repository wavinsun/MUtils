package cn.o.app.demo.web;

import android.app.Activity;
import android.content.Context;
import cn.o.app.core.json.JsonUtil;
import cn.o.app.demo.web.ByeByeWebDispatcher.ByeByeWebMessage;
import cn.o.app.ui.web.WebMessageState;

@SuppressWarnings("serial")
public class ByeByeWebDispatcher extends BasicWebMessageDispatcher<ByeByeWebMessage> {

	public static final int WEB_MESSAGE_ID = 88;
	public static final String WEB_MESSAGE_NAME = "byebye";

	public static class ByeByeWebMessage extends BasicWebMessage {
		public ByeByeWebData data;
		public ByeByeWebResult result;
	}

	public static class ByeByeWebData {
		public String redirect;
	}

	public static class ByeByeWebResult extends BasicWebMessageResult {

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
	public ByeByeWebMessage translateMessage() {
		ByeByeWebMessage msg = new ByeByeWebMessage();
		msg.id = id;
		msg.name = name;
		msg.callbacker = callbacker;
		msg.data = new ByeByeWebData();
		try {
			JsonUtil.convertFromJson(data, msg.data);
			return msg;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public void onMessage(ByeByeWebMessage message) {
		Context context = getContext();
		boolean complete = context instanceof Activity;
		if (message.callbacker != null) {
			message.result = new ByeByeWebResult();
			message.result.state = complete ? WebMessageState.complete : WebMessageState.error;
			this.notifyManager(message);
		}
		if (complete) {
			((Activity) context).finish();
		}
	}

}

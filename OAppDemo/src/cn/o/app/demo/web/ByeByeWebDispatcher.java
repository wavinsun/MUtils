package cn.o.app.demo.web;

import android.app.Activity;
import android.content.Context;
import cn.o.app.demo.web.BasicWebMessage.BasicWebMessageData;
import cn.o.app.demo.web.BasicWebMessage.BasicWebMessageResult;
import cn.o.app.demo.web.ByeByeWebDispatcher.ByeByeWebData;
import cn.o.app.demo.web.ByeByeWebDispatcher.ByeByeWebResult;
import cn.o.app.ui.web.WebMessageState;

@SuppressWarnings("serial")
public class ByeByeWebDispatcher extends BasicWebMessageDispatcher<ByeByeWebData, ByeByeWebResult> {

	public static class ByeByeWebData extends BasicWebMessageData {
		public String redirect;
	}

	public static class ByeByeWebResult extends BasicWebMessageResult {

	}

	@Override
	public int messageId() {
		return 88;
	}

	@Override
	public String messageName() {
		return "byebye";
	}

	@Override
	public void onMessage(BasicWebMessage<ByeByeWebData, ByeByeWebResult> message) {
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
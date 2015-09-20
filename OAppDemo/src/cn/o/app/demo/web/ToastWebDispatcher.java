package cn.o.app.demo.web;

import android.content.Context;
import cn.o.app.demo.web.BasicWebMessage.BasicWebMessageData;
import cn.o.app.demo.web.BasicWebMessage.BasicWebMessageResult;
import cn.o.app.demo.web.ToastWebDispatcher.ToastWebMessageData;
import cn.o.app.demo.web.ToastWebDispatcher.ToastWebMessageResult;
import cn.o.app.ui.core.IToastOwner;
import cn.o.app.ui.web.WebMessageState;

@SuppressWarnings("serial")
public class ToastWebDispatcher extends BasicWebMessageDispatcher<ToastWebMessageData, ToastWebMessageResult> {

	public static class ToastWebMessageData extends BasicWebMessageData {
		public String toast;
	}

	public static class ToastWebMessageResult extends BasicWebMessageResult {

	}

	@Override
	public int messageId() {
		return 66;
	}

	@Override
	public String messageName() {
		return "toast";
	}

	@Override
	public void onMessage(BasicWebMessage<ToastWebMessageData, ToastWebMessageResult> message) {
		Context context = getContext();
		if (context instanceof IToastOwner) {
			((IToastOwner) context).toast(message.data.toast);
		}
		if (message.callbacker != null) {
			message.result = new ToastWebMessageResult();
			message.result.state = WebMessageState.complete;
			this.notifyManager(message);
		}
	}

}

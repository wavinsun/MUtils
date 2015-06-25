package cn.o.app.socket;

import cn.o.app.queue.QueueItem;

@SuppressWarnings("unchecked")
public class SocketTask<REQUEST, RESPONSE> extends
		QueueItem<ISocketTask<REQUEST, RESPONSE>> implements
		ISocketTask<REQUEST, RESPONSE> {

	public static final String URL_OKCOIN_API = "wss://real.okcoin.cn:10440/websocket/okcoinapi";

	protected String mUrl;

	protected String mChannel;

	protected REQUEST mRequest;

	protected RESPONSE mResponse;

	public SocketTask() {
		mUrl = URL_OKCOIN_API;
	}

	public void addListener(SocketTaskListener<REQUEST, RESPONSE> listener) {
		super.addListener(listener);
	}

	@Override
	public String getUrl() {
		return mUrl;
	}

	@Override
	public void setUrl(String url) {
		if (mStarted || mStoped) {
			return;
		}
		mUrl = url;
	}

	@Override
	public String getChannel() {
		return mChannel;
	}

	@Override
	public void setChannel(String channel) {
		if (mStarted || mStoped) {
			return;
		}
		mChannel = channel;
	}

	@Override
	public REQUEST getRequest() {
		return mRequest;
	}

	@Override
	public void setRequest(REQUEST request) {
		if (mStarted || mStoped) {
			return;
		}
		mRequest = request;
	}

	@Override
	public RESPONSE getResponse() {
		return mResponse;
	}

	@Override
	public void setResponse(RESPONSE response) {
		if (mStoped) {
			return;
		}
		mResponse = response;
		for (ISocketTaskListener<REQUEST, RESPONSE> listener : getListeners(ISocketTaskListener.class)) {
			listener.onMessage(this, response);
		}
	}
}

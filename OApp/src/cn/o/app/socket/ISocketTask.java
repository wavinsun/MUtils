package cn.o.app.socket;

import cn.o.app.queue.IQueueItem;

public interface ISocketTask<REQUEST, RESPONSE> extends IQueueItem<ISocketTask<REQUEST, RESPONSE>> {

	public String getUrl();

	public void setUrl(String url);

	public String getChannel();

	public void setChannel(String channel);

	public REQUEST getRequest();

	public void setRequest(REQUEST request);

	public RESPONSE getResponse();

	public void setResponse(RESPONSE response);

}

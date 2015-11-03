package cn.mutils.app.net;

import cn.mutils.app.queue.IQueueItem;

public interface INetTask<REQUEST, RESPONSE> extends
		IQueueItem<INetTask<REQUEST, RESPONSE>> {

	public String getUrl();

	public void setUrl(String url);

	public String getRequestMethod();

	public void setRequestMethod(String requestMethod);

	public REQUEST getRequest();

	public void setRequest(REQUEST request);

	public RESPONSE getResponse();

	public void setResponse(RESPONSE response);

	public long getResponseTime();

}

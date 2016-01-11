package cn.mutils.app.net;

import cn.mutils.app.queue.IQueueItem;

@SuppressWarnings("unused")
public interface INetTask<REQUEST, RESPONSE> extends
        IQueueItem<INetTask<REQUEST, RESPONSE>> {

    String getUrl();

    void setUrl(String url);

    String getRequestMethod();

    void setRequestMethod(String requestMethod);

    REQUEST getRequest();

    void setRequest(REQUEST request);

    RESPONSE getResponse();

    void setResponse(RESPONSE response);

    long getResponseTime();

}

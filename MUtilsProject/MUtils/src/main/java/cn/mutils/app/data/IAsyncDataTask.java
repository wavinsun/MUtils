package cn.mutils.app.data;

import cn.mutils.app.queue.IQueueItem;

public interface IAsyncDataTask<DATA> extends IQueueItem<IAsyncDataTask<DATA>> {

    DATA getData();

    void setData(DATA data);

}

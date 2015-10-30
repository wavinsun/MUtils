package cn.mutils.app.data;

import cn.mutils.app.queue.IQueueItem;

public interface IAsyncDataTask<DATA> extends IQueueItem<IAsyncDataTask<DATA>> {

	public DATA getData();

	public void setData(DATA data);

}

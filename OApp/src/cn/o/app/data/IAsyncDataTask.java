package cn.o.app.data;

import cn.o.app.queue.IQueueItem;

public interface IAsyncDataTask<DATA> extends IQueueItem<IAsyncDataTask<DATA>> {
	public DATA getData();

	public void setData(DATA data);
}

package cn.o.app.data;

import cn.o.app.queue.IQueueItemListener;

public interface IAsyncDataTaskListener<DATA> extends
		IQueueItemListener<IAsyncDataTask<DATA>> {

	public void onDoInBackground(IAsyncDataTask<DATA> task, DATA data);

	public void onComplete(IAsyncDataTask<DATA> task);

}

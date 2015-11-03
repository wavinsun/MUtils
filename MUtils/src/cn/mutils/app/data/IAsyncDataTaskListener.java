package cn.mutils.app.data;

import cn.mutils.app.queue.IQueueItemListener;

public interface IAsyncDataTaskListener<DATA> extends
		IQueueItemListener<IAsyncDataTask<DATA>> {

	public void onDoInBackground(IAsyncDataTask<DATA> task, DATA data);

	public void onComplete(IAsyncDataTask<DATA> task);

}

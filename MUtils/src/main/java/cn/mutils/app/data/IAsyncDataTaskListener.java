package cn.mutils.app.data;

import cn.mutils.app.queue.IQueueItemListener;

public interface IAsyncDataTaskListener<DATA> extends
        IQueueItemListener<IAsyncDataTask<DATA>> {

    void onDoInBackground(IAsyncDataTask<DATA> task, DATA data);

    void onComplete(IAsyncDataTask<DATA> task);

}

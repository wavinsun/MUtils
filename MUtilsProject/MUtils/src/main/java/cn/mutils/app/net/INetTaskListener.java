package cn.mutils.app.net;

import cn.mutils.app.queue.IQueueItemListener;

public interface INetTaskListener<REQUEST, RESPONSE> extends
		IQueueItemListener<INetTask<REQUEST, RESPONSE>> {

	public void onComplete(INetTask<REQUEST, RESPONSE> task, RESPONSE response);

}

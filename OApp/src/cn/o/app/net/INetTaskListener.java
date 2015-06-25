package cn.o.app.net;

import cn.o.app.queue.IQueueItemListener;

public interface INetTaskListener<REQUEST, RESPONSE> extends
		IQueueItemListener<INetTask<REQUEST, RESPONSE>> {

	public void onComplete(INetTask<REQUEST, RESPONSE> task, RESPONSE response);

}

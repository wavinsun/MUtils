package cn.o.app.socket;

import cn.o.app.queue.IQueueItemListener;

public interface ISocketTaskListener<REQUEST, RESPONSE> extends
		IQueueItemListener<ISocketTask<REQUEST, RESPONSE>> {

	public void onMessage(ISocketTask<REQUEST, RESPONSE> task, RESPONSE response);

}

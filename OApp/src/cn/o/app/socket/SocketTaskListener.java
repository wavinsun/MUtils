package cn.o.app.socket;

import cn.o.app.queue.QueueItemListener;

public abstract class SocketTaskListener<REQUEST, RESPONSE> extends
		QueueItemListener<ISocketTask<REQUEST, RESPONSE>> implements
		ISocketTaskListener<REQUEST, RESPONSE> {

	@Override
	public void onException(ISocketTask<REQUEST, RESPONSE> task, Exception e) {

	}

}

package cn.o.app.queue;

import cn.o.app.core.event.Listener;

public interface IQueueListener extends Listener {

	public void onRunStateChanged(IQueue queue);

}

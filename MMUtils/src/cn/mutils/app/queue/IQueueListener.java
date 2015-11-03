package cn.mutils.app.queue;

import cn.mutils.app.core.event.Listener;

public interface IQueueListener extends Listener {

	public void onRunStateChanged(IQueue queue);

}

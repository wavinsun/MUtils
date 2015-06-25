package cn.o.app.net;

import cn.o.app.queue.QueueItemListener;

public abstract class NetTaskListener<REQUEST, RESPONSE> extends
		QueueItemListener<INetTask<REQUEST, RESPONSE>> implements
		INetTaskListener<REQUEST, RESPONSE> {

}

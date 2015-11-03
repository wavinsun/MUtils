package cn.mutils.app.net;

import cn.mutils.app.queue.QueueItemListener;

public abstract class NetTaskListener<REQUEST, RESPONSE> extends
		QueueItemListener<INetTask<REQUEST, RESPONSE>> implements
		INetTaskListener<REQUEST, RESPONSE> {

}

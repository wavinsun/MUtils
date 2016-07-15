package cn.mutils.app.net;

import cn.mutils.app.queue.Queue;

public class NetQueue extends Queue implements INetQueue {

	public NetQueue() {
		super();
		this.mMaxRunningCount = 2;
	}

}

package cn.o.app.net;

import cn.o.app.queue.Queue;

public class NetQueue extends Queue implements INetQueue {

	public NetQueue() {
		super();
		this.mMaxRunningCount = 2;
	}

}

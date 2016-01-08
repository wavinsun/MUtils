package cn.mutils.app.queue;

import cn.mutils.app.core.event.IListener;

public interface IQueueListener extends IListener {

    void onRunStateChanged(IQueue queue);

}

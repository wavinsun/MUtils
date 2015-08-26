package cn.o.app.io;

import cn.o.app.context.IContextOwner;
import cn.o.app.core.runtime.ILockable;
import cn.o.app.core.task.IStopable;

public interface IBroadcast extends ILockable, IStopable, IContextOwner {

}

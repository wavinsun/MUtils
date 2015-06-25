package cn.o.app.io;

import cn.o.app.context.IContextOwner;
import cn.o.app.task.ILockable;
import cn.o.app.task.IStopable;

public interface IBroadcast extends ILockable, IStopable, IContextOwner {

}

package cn.o.app.io;

import cn.o.app.core.ILockable;
import cn.o.app.core.task.IStopable;
import cn.o.app.os.IContextOwner;

public interface IBroadcast extends ILockable, IStopable, IContextOwner {

}

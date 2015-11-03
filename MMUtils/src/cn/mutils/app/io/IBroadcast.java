package cn.mutils.app.io;

import cn.mutils.app.core.ILockable;
import cn.mutils.app.core.task.IStopable;
import cn.mutils.app.os.IContextOwner;

public interface IBroadcast extends ILockable, IStopable, IContextOwner {

}

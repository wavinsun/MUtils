package cn.mutils.app.io;

import cn.mutils.app.os.IContextOwner;
import cn.mutils.core.ILockable;
import cn.mutils.core.task.IStoppable;

public interface IBroadcast extends ILockable, IStoppable, IContextOwner {

}

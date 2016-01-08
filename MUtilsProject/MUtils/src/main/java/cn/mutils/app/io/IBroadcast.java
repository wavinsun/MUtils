package cn.mutils.app.io;

import cn.mutils.app.core.ILockable;
import cn.mutils.app.core.task.IStoppable;
import cn.mutils.app.os.IContextOwner;

public interface IBroadcast extends ILockable, IStoppable, IContextOwner {

}

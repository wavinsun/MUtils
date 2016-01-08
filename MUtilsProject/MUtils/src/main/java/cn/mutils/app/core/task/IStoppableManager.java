package cn.mutils.app.core.task;

import java.util.List;

public interface IStoppableManager {

    void bind(IStoppable stoppable);

    void stopAll();

    void stopAll(boolean includeLockable);

    List<IStoppable> getBindStoppables();

}

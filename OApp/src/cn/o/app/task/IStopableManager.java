package cn.o.app.task;

import java.util.List;

public interface IStopableManager {
	public void bind(IStopable stopable);

	public void stopAll();

	public void stopAll(boolean includeLockable);

	public List<IStopable> getBindStopables();
}

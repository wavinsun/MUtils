package cn.mutils.app.core.task;

public interface IStopable {

	public boolean isRunInBackground();

	public void setRunInBackground(boolean runInBackground);

	public boolean isStoped();

	public boolean stop();

}

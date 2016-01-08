package cn.mutils.app.core.task;

public interface IStoppable {

    boolean isRunInBackground();

    void setRunInBackground(boolean runInBackground);

    boolean isStopped();

    boolean stop();

}

package cn.o.app.core.concurrent;

import java.util.List;

public interface IWoker<JOB> {

	public void delegate(List<JOB> jobs);

	public boolean start();

	public boolean stop();

	public int getConcurrentCount();

	public double getProgress();

	public void work(JOB job);

	public void done();

}

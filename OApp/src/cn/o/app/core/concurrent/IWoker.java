package cn.o.app.core.concurrent;

import java.util.List;

public interface IWoker<JOB> {

	public void delegate(List<JOB> jobs);

	public boolean start();

	public boolean stop();

	public void work(JOB job);

	public void done();

	public int getConcurrentCount();

	public boolean prepare(List<JOB> jobs);

	public double getProgress();

	public int getId(JOB job);

}

package cn.mutils.app.core.concurrent;

import java.util.List;

/**
 * Concurrent worker of framework
 * 
 * @see Worker
 */
public interface IWoker<JOB> {

	/**
	 * Delegate jobs
	 * 
	 * @param jobs
	 */
	public void delegate(List<JOB> jobs);

	/**
	 * Start working
	 * 
	 * @return
	 */
	public boolean start();

	/**
	 * Stop working
	 * 
	 * @return
	 */
	public boolean stop();

	/**
	 * Working job<br>
	 * It is execute by concurrent threads
	 * 
	 * @param job
	 */
	public void work(JOB job);

	/**
	 * Done all jobs<br>
	 * It is called when all jobs are done
	 */
	public void done();

	/**
	 * Get count of concurrent threads
	 * 
	 * @return
	 */
	public int getConcurrentCount();

	/**
	 * Prepare jobs<br>
	 * It is called before all job to be doing
	 * 
	 * @param jobs
	 * @return
	 */
	public boolean prepare(List<JOB> jobs);

	/**
	 * Get progress
	 * 
	 * @return [0,1]
	 */
	public double getProgress();

	/**
	 * Get ID for job
	 * 
	 * @param job
	 * @return
	 */
	public int getId(JOB job);

}

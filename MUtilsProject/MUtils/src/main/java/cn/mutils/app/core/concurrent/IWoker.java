package cn.mutils.app.core.concurrent;

import java.util.List;

/**
 * Concurrent worker of framework
 *
 * @see Worker
 */
@SuppressWarnings("SpellCheckingInspection")
public interface IWoker<JOB> {

    /**
     * Delegate jobs
     */
    void delegate(List<JOB> jobs);

    /**
     * Start working
     */
    boolean start();

    /**
     * Stop working
     */
    boolean stop();

    /**
     * Working job<br>
     * It is execute by concurrent threads
     */
    void work(JOB job);

    /**
     * Done all jobs<br>
     * It is called when all jobs are done
     */
    void done();

    /**
     * Get count of concurrent threads
     */
    int getConcurrentCount();

    /**
     * Prepare jobs<br>
     * It is called before all job to be doing
     */
    boolean prepare(List<JOB> jobs);

    /**
     * Get progress
     *
     * @return [0, 1]
     */
    double getProgress();

    /**
     * Get ID for job
     */
    int getId(JOB job);

}

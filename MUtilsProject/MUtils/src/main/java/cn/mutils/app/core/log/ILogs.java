package cn.mutils.app.core.log;

/**
 * Logs of framework for top level
 */
public interface ILogs {

    /**
     * Whether logs is enabled
     */
    public boolean isEnabled();

    /**
     * Set logs enabled state
     */
    public void setEnabled(boolean enabled);

    /**
     * Print log of verbose level
     */
    public int verbose(String tag, String msg);

    /**
     * Print log of verbose level
     */
    public int verbose(String tag, String msg, Throwable tr);

    /**
     * Print log of debug level
     */
    public int debug(String tag, String msg);

    /**
     * Print log of debug level
     */
    public int debug(String tag, String msg, Throwable tr);

    /**
     * Print log of info level
     */
    public int info(String tag, String msg);

    /**
     * Print log of info level
     */
    public int info(String tag, String msg, Throwable tr);

    /**
     * Print log of warn level
     */
    public int warn(String tag, String msg);

    /**
     * Print log of warn level
     */
    public int warn(String tag, String msg, Throwable tr);

    /**
     * Print log of error level
     */
    public int error(String tag, String msg);

    /**
     * Print log of error level
     */
    public int error(String tag, String msg, Throwable tr);

    /**
     * Print log of verbose level
     */
    public int verbose(String msg);

    /**
     * Print log of verbose level
     */
    public int verbose(String msg, Throwable tr);

    /**
     * Print log of debug level
     */
    public int debug(String msg);

    /**
     * Print log of debug level
     */
    public int debug(String msg, Throwable tr);

    /**
     * Print log of info level
     */
    public int info(String msg);

    /**
     * Print log of info level
     */
    public int info(String msg, Throwable tr);

    /**
     * Print log of warn level
     */
    public int warn(String msg);

    /**
     * Print log of warn level
     */
    public int warn(String msg, Throwable tr);

    /**
     * Print log of error level
     */
    public int error(String msg);

    /**
     * Print log of error level
     */
    public int error(String msg, Throwable tr);

}

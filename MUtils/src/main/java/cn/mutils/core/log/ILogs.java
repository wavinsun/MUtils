package cn.mutils.core.log;

/**
 * Logs of framework for top level
 */
@SuppressWarnings("unused")
public interface ILogs {

    /**
     * Whether logs is enabled
     */
    boolean isEnabled();

    /**
     * Set logs enabled state
     */
    void setEnabled(boolean enabled);

    /**
     * Print log of verbose level
     */
    int verbose(String tag, String msg);

    /**
     * Print log of verbose level
     */
    int verbose(String tag, String msg, Throwable tr);

    /**
     * Print log of debug level
     */
    int debug(String tag, String msg);

    /**
     * Print log of debug level
     */
    int debug(String tag, String msg, Throwable tr);

    /**
     * Print log of info level
     */
    int info(String tag, String msg);

    /**
     * Print log of info level
     */
    int info(String tag, String msg, Throwable tr);

    /**
     * Print log of warn level
     */
    int warn(String tag, String msg);

    /**
     * Print log of warn level
     */
    int warn(String tag, String msg, Throwable tr);

    /**
     * Print log of error level
     */
    int error(String tag, String msg);

    /**
     * Print log of error level
     */
    int error(String tag, String msg, Throwable tr);

    /**
     * Print log of verbose level
     */
    int verbose(String msg);

    /**
     * Print log of verbose level
     */
    int verbose(String msg, Throwable tr);

    /**
     * Print log of debug level
     */
    int debug(String msg);

    /**
     * Print log of debug level
     */
    int debug(String msg, Throwable tr);

    /**
     * Print log of info level
     */
    int info(String msg);

    /**
     * Print log of info level
     */
    int info(String msg, Throwable tr);

    /**
     * Print log of warn level
     */
    int warn(String msg);

    /**
     * Print log of warn level
     */
    int warn(String msg, Throwable tr);

    /**
     * Print log of error level
     */
    int error(String msg);

    /**
     * Print log of error level
     */
    int error(String msg, Throwable tr);

}

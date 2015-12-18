package cn.mutils.app.core.log;

/**
 * Logs of framework for top level
 */
public interface ILogs {

	/**
	 * Whether logs is enabled
	 * 
	 * @return
	 */
	public boolean isEnabled();

	/**
	 * Set logs enabled state
	 * 
	 * @param enabled
	 */
	public void setEnabled(boolean enabled);

	/**
	 * Print log of verbose level
	 * 
	 * @param tag
	 * @param msg
	 * @return
	 */
	public int verbose(String tag, String msg);

	/**
	 * Print log of verbose level
	 * 
	 * @param tag
	 * @param msg
	 * @param tr
	 * @return
	 */
	public int verbose(String tag, String msg, Throwable tr);

	/**
	 * Print log of debug level
	 * 
	 * @param tag
	 * @param msg
	 * @return
	 */
	public int debug(String tag, String msg);

	/**
	 * Print log of debug level
	 * 
	 * @param tag
	 * @param msg
	 * @param tr
	 * @return
	 */
	public int debug(String tag, String msg, Throwable tr);

	/**
	 * Print log of info level
	 * 
	 * @param tag
	 * @param msg
	 * @return
	 */
	public int info(String tag, String msg);

	/**
	 * Print log of info level
	 * 
	 * @param tag
	 * @param msg
	 * @param tr
	 * @return
	 */
	public int info(String tag, String msg, Throwable tr);

	/**
	 * Print log of warn level
	 * 
	 * @param tag
	 * @param msg
	 * @return
	 */
	public int warn(String tag, String msg);

	/**
	 * Print log of warn level
	 * 
	 * @param tag
	 * @param msg
	 * @param tr
	 * @return
	 */
	public int warn(String tag, String msg, Throwable tr);

	/**
	 * Print log of error level
	 * 
	 * @param tag
	 * @param msg
	 * @return
	 */
	public int error(String tag, String msg);

	/**
	 * Print log of error level
	 * 
	 * @param tag
	 * @param msg
	 * @param tr
	 * @return
	 */
	public int error(String tag, String msg, Throwable tr);

	/**
	 * Print log of verbose level
	 * 
	 * @param msg
	 * @return
	 */
	public int verbose(String msg);

	/**
	 * Print log of verbose level
	 * 
	 * @param msg
	 * @param tr
	 * @return
	 */
	public int verbose(String msg, Throwable tr);

	/**
	 * Print log of debug level
	 * 
	 * @param msg
	 * @return
	 */
	public int debug(String msg);

	/**
	 * Print log of debug level
	 * 
	 * @param msg
	 * @param tr
	 * @return
	 */
	public int debug(String msg, Throwable tr);

	/**
	 * Print log of info level
	 * 
	 * @param msg
	 * @return
	 */
	public int info(String msg);

	/**
	 * Print log of info level
	 * 
	 * @param msg
	 * @param tr
	 * @return
	 */
	public int info(String msg, Throwable tr);

	/**
	 * Print log of warn level
	 * 
	 * @param msg
	 * @return
	 */
	public int warn(String msg);

	/**
	 * Print log of warn level
	 * 
	 * @param msg
	 * @param tr
	 * @return
	 */
	public int warn(String msg, Throwable tr);

	/**
	 * Print log of error level
	 * 
	 * @param msg
	 * @return
	 */
	public int error(String msg);

	/**
	 * Print log of error level
	 * 
	 * @param msg
	 * @param tr
	 * @return
	 */
	public int error(String msg, Throwable tr);

}

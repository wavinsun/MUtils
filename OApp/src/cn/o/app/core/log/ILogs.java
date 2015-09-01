package cn.o.app.core.log;

public interface ILogs {

	public boolean isEnabled();

	public void setEnabled(boolean enabled);

	public int verbose(String tag, String msg);

	public int verbose(String tag, String msg, Throwable tr);

	public int debug(String tag, String msg);

	public int debug(String tag, String msg, Throwable tr);

	public int info(String tag, String msg);

	public int info(String tag, String msg, Throwable tr);

	public int warn(String tag, String msg);

	public int warn(String tag, String msg, Throwable tr);

	public int error(String tag, String msg);

	public int error(String tag, String msg, Throwable tr);

	public int verbose(String msg);

	public int verbose(String msg, Throwable tr);

	public int debug(String msg);

	public int debug(String msg, Throwable tr);

	public int info(String msg);

	public int info(String msg, Throwable tr);

	public int warn(String msg);

	public int warn(String msg, Throwable tr);

	public int error(String msg);

	public int error(String msg, Throwable tr);

}

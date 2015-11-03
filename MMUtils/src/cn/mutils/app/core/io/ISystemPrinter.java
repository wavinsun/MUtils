package cn.mutils.app.core.io;

/**
 * System printer<br>
 * To replace System.out and System.err
 */
public interface ISystemPrinter {

	public void sysout(String str);

	public void syserr(String str);

}

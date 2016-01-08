package cn.mutils.app.core.io;

/**
 * System printer<br>
 * To replace System.out and System.err
 */
public interface ISystemPrinter {

    void systemOut(String str);

    void systemErr(String str);

}

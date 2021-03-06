package cn.mutils.core.runtime;

/**
 * Stack trace utility of framework.<br>
 * Using Throwable.getStackTrace() to replace
 * Thread.currentThread().getStackTrace() because of the following bug<br>
 * <p>
 * Android environment stack trace:<br>
 * dalvik.system.VMStack.getThreadStackTrace<br>
 * java.lang.Thread.getStackTrace<br>
 * cn.mutils.app.runtime.StackTraceUtil.getCallerElement<br>
 * ... ....<br>
 * <p>
 * <p>
 * Java standard environment stack trace:<br>
 * java.lang.Thread.getStackTrace<br>
 * cn.mutils.app.runtime.StackTraceUtil.getCallerElement<br>
 * ... ...
 */
@SuppressWarnings("unused")
public class StackTraceUtil {

    /**
     * Get stack trace element for the current method
     */
    public static StackTraceElement getCurrentElement() {
        StackTraceElement[] elements = new Throwable().getStackTrace();
        return 1 < elements.length ? elements[1] : elements[elements.length - 1];
    }

    /**
     * Get stack trace element for the method who call you
     */
    public static StackTraceElement getCallerElement() {
        StackTraceElement[] elements = new Throwable().getStackTrace();
        return 2 < elements.length ? elements[2] : elements[elements.length - 1];
    }

}

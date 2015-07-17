package cn.o.app.runtime;

/**
 * Stack trace utility of framework
 * 
 * Using Throwable.getStackTrace() to replace
 * Thread.currentThread().getStackTrace() because of the following bug
 * 
 * Android environment stack trace:
 * 
 * dalvik.system.VMStack.getThreadStackTrace
 * 
 * java.lang.Thread.getStackTrace
 * 
 * cn.o.app.runtime.StackTraceUtil.getCallerElement
 * 
 * ... ....
 * 
 * 
 * Java standard environment stack trace:
 * 
 * java.lang.Thread.getStackTrace
 * 
 * cn.o.app.runtime.StackTraceUtil.getCallerElement
 * 
 * ... ...
 * 
 */
public class StackTraceUtil {

	/**
	 * Get stack trace element for the current method
	 * 
	 * @return
	 */
	public static StackTraceElement getCurrentElement() {
		StackTraceElement[] elements = new Throwable().getStackTrace();
		return 1 < elements.length ? elements[1] : elements[elements.length - 1];
	}

	/**
	 * Get stack trace element for the method who call you
	 * 
	 * @return
	 */
	public static StackTraceElement getCallerElement() {
		StackTraceElement[] elements = new Throwable().getStackTrace();
		return 2 < elements.length ? elements[2] : elements[elements.length - 1];
	}

}

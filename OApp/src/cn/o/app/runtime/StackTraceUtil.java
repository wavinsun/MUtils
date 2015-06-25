package cn.o.app.runtime;

public class StackTraceUtil {

	public static StackTraceElement getCurrentElement() {
		return Thread.currentThread().getStackTrace()[3];
	}

	public static StackTraceElement getCallerElement() {
		return Thread.currentThread().getStackTrace()[4];
	}

}

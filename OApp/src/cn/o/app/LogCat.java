package cn.o.app;

import android.util.Log;
import cn.o.app.runtime.StackTraceUtil;
import cn.o.app.text.StringUtil;

/**
 * Log cat of framework
 */
public class LogCat {

	public static int v(String tag, String msg) {
		if (!BuildConfig.DEBUG) {
			return 0;
		}
		tag = tag != null ? tag : "";
		msg = msg != null ? msg : "";
		return Log.e(tag, msg);
	}

	public static int v(String tag, String msg, Throwable tr) {
		if (!BuildConfig.DEBUG) {
			return 0;
		}
		tag = tag != null ? tag : "";
		msg = msg != null ? msg : "";
		return Log.v(tag, msg, tr);
	}

	public static int d(String tag, String msg) {
		if (!BuildConfig.DEBUG) {
			return 0;
		}
		tag = tag != null ? tag : "";
		msg = msg != null ? msg : "";
		return Log.d(tag, msg);
	}

	public static int d(String tag, String msg, Throwable tr) {
		if (!BuildConfig.DEBUG) {
			return 0;
		}
		tag = tag != null ? tag : "";
		msg = msg != null ? msg : "";
		return Log.d(tag, msg, tr);
	}

	public static int i(String tag, String msg) {
		if (!BuildConfig.DEBUG) {
			return 0;
		}
		tag = tag != null ? tag : "";
		msg = msg != null ? msg : "";
		return Log.i(tag, msg);
	}

	public static int i(String tag, String msg, Throwable tr) {
		if (!BuildConfig.DEBUG) {
			return 0;
		}
		tag = tag != null ? tag : "";
		msg = msg != null ? msg : "";
		return Log.i(tag, msg, tr);
	}

	public static int w(String tag, String msg) {
		if (!BuildConfig.DEBUG) {
			return 0;
		}
		tag = tag != null ? tag : "";
		msg = msg != null ? msg : "";
		return Log.w(tag, msg);
	}

	public static int w(String tag, String msg, Throwable tr) {
		if (!BuildConfig.DEBUG) {
			return 0;
		}
		tag = tag != null ? tag : "";
		msg = msg != null ? msg : "";
		return Log.w(tag, msg, tr);
	}

	public static int e(String tag, String msg) {
		if (!BuildConfig.DEBUG) {
			return 0;
		}
		tag = tag != null ? tag : "";
		msg = msg != null ? msg : "";
		return Log.e(tag, msg);
	}

	public static int e(String tag, String msg, Throwable tr) {
		if (!BuildConfig.DEBUG) {
			return 0;
		}
		tag = tag != null ? tag : "";
		msg = msg != null ? msg : "";
		return Log.e(tag, msg, tr);
	}

	public static int wtf(String tag, String msg) {
		if (!BuildConfig.DEBUG) {
			return 0;
		}
		tag = tag != null ? tag : "";
		msg = msg != null ? msg : "";
		return Log.wtf(tag, msg);
	}

	public static int wtf(String tag, String msg, Throwable tr) {
		if (!BuildConfig.DEBUG) {
			return 0;
		}
		tag = tag != null ? tag : "";
		msg = msg != null ? msg : "";
		return Log.wtf(tag, msg, tr);
	}

	public static int v(String msg) {
		if (!BuildConfig.DEBUG) {
			return 0;
		}
		msg = msg != null ? msg : "";
		return Log.v(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg);
	}

	public static int v(String msg, Throwable tr) {
		if (!BuildConfig.DEBUG) {
			return 0;
		}
		msg = msg != null ? msg : "";
		return Log.v(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg, tr);
	}

	public static int d(String msg) {
		if (!BuildConfig.DEBUG) {
			return 0;
		}
		msg = msg != null ? msg : "";
		return Log.d(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg);
	}

	public static int d(String msg, Throwable tr) {
		if (!BuildConfig.DEBUG) {
			return 0;
		}
		msg = msg != null ? msg : "";
		return Log.d(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg, tr);
	}

	public static int i(String msg) {
		if (!BuildConfig.DEBUG) {
			return 0;
		}
		msg = msg != null ? msg : "";
		return Log.i(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg);
	}

	public static int i(String msg, Throwable tr) {
		if (!BuildConfig.DEBUG) {
			return 0;
		}
		msg = msg != null ? msg : "";
		return Log.i(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg, tr);
	}

	public static int w(String msg) {
		if (!BuildConfig.DEBUG) {
			return 0;
		}
		msg = msg != null ? msg : "";
		return Log.w(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg);
	}

	public static int w(String msg, Throwable tr) {
		if (!BuildConfig.DEBUG) {
			return 0;
		}
		msg = msg != null ? msg : "";
		return Log.w(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg, tr);
	}

	public static int e(String msg) {
		if (!BuildConfig.DEBUG) {
			return 0;
		}
		msg = msg != null ? msg : "";
		return Log.e(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg);
	}

	public static int e(String msg, Throwable tr) {
		if (!BuildConfig.DEBUG) {
			return 0;
		}
		msg = msg != null ? msg : "";
		return Log.e(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg, tr);
	}

	public static int wtf(String msg) {
		if (!BuildConfig.DEBUG) {
			return 0;
		}
		msg = msg != null ? msg : "";
		return Log.wtf(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg);
	}

	public static int wtf(String msg, Throwable tr) {
		if (!BuildConfig.DEBUG) {
			return 0;
		}
		msg = msg != null ? msg : "";
		return Log.wtf(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg, tr);
	}

}

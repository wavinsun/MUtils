package cn.o.app.core.log;

import cn.o.app.AppLogs;
import cn.o.app.BuildConfig;
import cn.o.app.core.runtime.AppRuntime;
import cn.o.app.core.runtime.StackTraceUtil;
import cn.o.app.core.text.StringUtil;

public class Logs implements ILogs {

	protected static ILogs sLogs = null;

	protected static ILogs getLogs() {
		if (sLogs == null) {
			AppRuntime rt = AppRuntime.detect();
			if (rt == AppRuntime.ANDROID) {
				sLogs = new AppLogs();
			} else {
				sLogs = new Logs();
			}
		}
		return sLogs;
	}

	public static void setLogs(ILogs logs) {
		sLogs = logs;
	}

	public static int v(String tag, String msg) {
		return getLogs().verbose(tag, msg);
	}

	public static int v(String tag, String msg, Throwable tr) {
		return getLogs().verbose(tag, msg, tr);
	}

	public static int d(String tag, String msg) {
		return getLogs().debug(tag, msg);
	}

	public static int d(String tag, String msg, Throwable tr) {
		return getLogs().debug(tag, msg, tr);
	}

	public static int i(String tag, String msg) {
		return getLogs().info(tag, msg);
	}

	public static int i(String tag, String msg, Throwable tr) {
		return getLogs().info(tag, msg, tr);
	}

	public static int w(String tag, String msg) {
		return getLogs().warn(tag, msg);
	}

	public static int w(String tag, String msg, Throwable tr) {
		return getLogs().warn(tag, msg, tr);
	}

	public static int e(String tag, String msg) {
		return getLogs().error(tag, msg);
	}

	public static int e(String tag, String msg, Throwable tr) {
		return getLogs().error(tag, msg, tr);
	}

	public static int v(String msg) {
		ILogs logs = getLogs();
		if (!logs.isEnabled()) {
			return 0;
		}
		return logs.verbose(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg);
	}

	public static int v(String msg, Throwable tr) {
		ILogs logs = getLogs();
		if (!logs.isEnabled()) {
			return 0;
		}
		return logs.verbose(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg, tr);
	}

	public static int d(String msg) {
		ILogs logs = getLogs();
		if (!logs.isEnabled()) {
			return 0;
		}
		return logs.debug(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg);
	}

	public static int d(String msg, Throwable tr) {
		ILogs logs = getLogs();
		if (!logs.isEnabled()) {
			return 0;
		}
		return logs.debug(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg, tr);
	}

	public static int i(String msg) {
		ILogs logs = getLogs();
		if (!logs.isEnabled()) {
			return 0;
		}
		return logs.info(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg);
	}

	public static int i(String msg, Throwable tr) {
		ILogs logs = getLogs();
		if (!logs.isEnabled()) {
			return 0;
		}
		return logs.info(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg, tr);
	}

	public static int w(String msg) {
		ILogs logs = getLogs();
		if (!logs.isEnabled()) {
			return 0;
		}
		return logs.warn(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg);
	}

	public static int w(String msg, Throwable tr) {
		ILogs logs = getLogs();
		if (!logs.isEnabled()) {
			return 0;
		}
		return logs.warn(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg, tr);
	}

	public static int e(String msg) {
		ILogs logs = getLogs();
		if (!logs.isEnabled()) {
			return 0;
		}
		return logs.error(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg);
	}

	public static int e(String msg, Throwable tr) {
		ILogs logs = getLogs();
		if (!logs.isEnabled()) {
			return 0;
		}
		return logs.error(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg, tr);
	}

	protected boolean mEnabled = BuildConfig.DEBUG;

	@Override
	public int verbose(String tag, String msg) {
		if (!mEnabled) {
			return 0;
		}
		String log = generate(tag, msg, null);
		System.out.println(log);
		return log.length();
	}

	@Override
	public int verbose(String tag, String msg, Throwable tr) {
		if (!mEnabled) {
			return 0;
		}
		String log = generate(tag, msg, tr);
		System.out.println(log);
		return log.length();
	}

	@Override
	public int debug(String tag, String msg) {
		if (!mEnabled) {
			return 0;
		}
		String log = generate(tag, msg, null);
		System.out.println(log);
		return log.length();
	}

	@Override
	public int debug(String tag, String msg, Throwable tr) {
		if (!mEnabled) {
			return 0;
		}
		String log = generate(tag, msg, tr);
		System.out.println(log);
		return log.length();
	}

	@Override
	public int info(String tag, String msg) {
		if (!mEnabled) {
			return 0;
		}
		String log = generate(tag, msg, null);
		System.out.println(log);
		return log.length();
	}

	@Override
	public int info(String tag, String msg, Throwable tr) {
		if (!mEnabled) {
			return 0;
		}
		String log = generate(tag, msg, tr);
		System.out.println(log);
		return log.length();
	}

	@Override
	public int warn(String tag, String msg) {
		if (!mEnabled) {
			return 0;
		}
		String log = generate(tag, msg, null);
		System.out.println(log);
		return log.length();
	}

	@Override
	public int warn(String tag, String msg, Throwable tr) {
		if (!mEnabled) {
			return 0;
		}
		String log = generate(tag, msg, tr);
		System.out.println(log);
		return log.length();
	}

	@Override
	public int error(String tag, String msg) {
		if (!mEnabled) {
			return 0;
		}
		String log = generate(tag, msg, null);
		System.err.println(log);
		return log.length();
	}

	@Override
	public int error(String tag, String msg, Throwable tr) {
		if (!mEnabled) {
			return 0;
		}
		String log = generate(tag, msg, tr);
		System.out.println(log);
		return log.length();
	}

	@Override
	public int verbose(String msg) {
		if (!mEnabled) {
			return 0;
		}
		String log = generate(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg, null);
		System.out.println(log);
		return log.length();
	}

	@Override
	public int verbose(String msg, Throwable tr) {
		if (!mEnabled) {
			return 0;
		}
		String log = generate(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg, tr);
		System.out.println(log);
		return log.length();
	}

	@Override
	public int debug(String msg) {
		if (!mEnabled) {
			return 0;
		}
		String log = generate(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg, null);
		System.out.println(log);
		return log.length();
	}

	@Override
	public int debug(String msg, Throwable tr) {
		if (!mEnabled) {
			return 0;
		}
		String log = generate(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg, tr);
		System.out.println(log);
		return log.length();
	}

	@Override
	public int info(String msg) {
		if (!mEnabled) {
			return 0;
		}
		String log = generate(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg, null);
		System.out.println(log);
		return log.length();
	}

	@Override
	public int info(String msg, Throwable tr) {
		if (!mEnabled) {
			return 0;
		}
		String log = generate(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg, tr);
		System.out.println(log);
		return log.length();
	}

	@Override
	public int warn(String msg) {
		if (!mEnabled) {
			return 0;
		}
		String log = generate(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg, null);
		System.out.println(log);
		return log.length();
	}

	@Override
	public int warn(String msg, Throwable tr) {
		if (!mEnabled) {
			return 0;
		}
		String log = generate(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg, tr);
		System.out.println(log);
		return log.length();
	}

	@Override
	public int error(String msg) {
		if (!mEnabled) {
			return 0;
		}
		String log = generate(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg, null);
		System.err.println(log);
		return log.length();
	}

	@Override
	public int error(String msg, Throwable tr) {
		if (!mEnabled) {
			return 0;
		}
		String log = generate(StringUtil.getTag(StackTraceUtil.getCallerElement()), msg, tr);
		System.err.println(log);
		return log.length();
	}

	protected String generate(String tag, String msg, Throwable tr) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		sb.append(tag);
		sb.append("] ");
		sb.append(msg);
		if (tr != null) {
			sb.append("\n");
			sb.append(StringUtil.printStackTrace(tr));
		}
		return sb.toString();
	}

	@Override
	public boolean isEnabled() {
		return mEnabled;
	}

	@Override
	public void setEnabled(boolean enabled) {
		mEnabled = enabled;
	}

}

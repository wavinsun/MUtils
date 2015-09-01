package cn.o.app.core.runtime;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public enum AppRuntime {

	/** Unknown */
	UNKNOWN,

	/** Java */
	JAVA,

	/** Java AWT */
	JAVA_AWT,

	/** Java web */
	JAVA_WEB,

	/** Android */
	ANDROID;

	protected static final String TAG_JAVA_AWT = "java.awt.";
	protected static final String TAG_ANDROID = "android.os.";

	public static AppRuntime detect() {
		return detect(null);
	}

	public static AppRuntime detect(Map<AppRuntime, List<String>> tagMap) {
		Thread[] mainThreads = ThreadUtil.getGroup(ThreadUtil.GROUP_MAIN);
		if (mainThreads.length == 0) {
			return UNKNOWN;
		}
		for (Thread t : mainThreads) {
			StackTraceElement[] stackTrace = t.getStackTrace();
			for (int i = stackTrace.length - 1; i >= 0; i--) {
				StackTraceElement ste = stackTrace[i];
				String className = ste.getClassName();
				if (className == null) {
					continue;
				}
				if (className.startsWith(TAG_ANDROID)) {
					return ANDROID;
				}
				if (className.startsWith(TAG_JAVA_AWT)) {
					return JAVA_AWT;
				}
				if (tagMap != null) {
					AppRuntime runtime = detect(className, tagMap);
					if (runtime != null) {
						return runtime;
					}
				}
			}
		}
		return JAVA;
	}

	protected static AppRuntime detect(String stackTraceElementClass, Map<AppRuntime, List<String>> tagMap) {
		if (tagMap == null) {
			return null;
		}
		for (Entry<AppRuntime, List<String>> entry : tagMap.entrySet()) {
			List<String> tags = entry.getValue();
			if (tags == null) {
				continue;
			}
			for (String tag : tags) {
				if (stackTraceElementClass.startsWith(tag)) {
					return entry.getKey();
				}
			}
		}
		return null;
	}

}

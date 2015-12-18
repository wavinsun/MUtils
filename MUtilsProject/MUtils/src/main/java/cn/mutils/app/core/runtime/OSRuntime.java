package cn.mutils.app.core.runtime;

import android.annotation.SuppressLint;

/**
 * OS runtime
 */
@SuppressLint("DefaultLocale")
public enum OSRuntime {

	UNKNOWN,

	WINDOWS,

	LINUX,

	MAC;

	protected static OSRuntime sOSRuntime = UNKNOWN;

	public static OSRuntime getOSRuntime() {
		if (sOSRuntime == UNKNOWN) {
			String os = System.getProperty("os.name").toLowerCase();
			if (os.contains("windows")) {
				sOSRuntime = WINDOWS;
			} else if (os.contains("linux")) {
				sOSRuntime = LINUX;
			} else if (os.contains("mac")) {
				sOSRuntime = MAC;
			}
		}
		return sOSRuntime;
	}

}

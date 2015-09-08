package cn.o.app.core.io;

import java.io.File;

import cn.o.app.core.runtime.OSRuntime;

/**
 * File utility
 */
public class FileUtil {

	public static boolean setHidden(File file, boolean isHidden) {
		if (file.isHidden() == isHidden) {
			return true;
		}
		if (OSRuntime.WINDOWS == OSRuntime.getOSRuntime()) {
			try {
				ProcessBuilder pb = new ProcessBuilder("attrib", isHidden ? "+h" : "-h", file.getCanonicalPath());
				Process p = pb.start();
				return p.waitFor() == 0;
			} catch (Exception e) {
				return false;
			}
		} else {
			return false;
		}
	}

}

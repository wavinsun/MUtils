package cn.o.app.desktop;

import java.awt.Desktop;
import java.io.File;

public class DesktopUtil {

	public static boolean open(File file) {
		try {
			Desktop.getDesktop().open(file);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}

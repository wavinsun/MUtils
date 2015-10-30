package cn.mutils.app.ext;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.io.File;
import java.util.List;

import javax.swing.UIManager;

import com.sun.jna.platform.FileUtils;

import cn.mutils.app.core.graphics.Bounds;

/**
 * Desktop utility for Windows platform or Others.
 */
public class DesktopUtil {

	public static final String USER_DIR = "user.dir";

	public static final String LOOK_AND_FEEL_WINDOWS = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";

	/**
	 * Open file use system call
	 * 
	 * @param file
	 * @return
	 */
	public static boolean open(File file) {
		try {
			Desktop.getDesktop().open(file);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Move to trash
	 * 
	 * @param file
	 * @return
	 */
	public static boolean moveToTrash(File file) {
		File[] files = new File[1];
		files[0] = file;
		try {
			FileUtils.getInstance().moveToTrash(files);
			return true;
		} catch (Exception e) {
			return false;
		} finally {
			files[0] = null;
		}
	}

	/**
	 * Move to trash
	 * 
	 * @param files
	 * @return
	 */
	public static boolean moveToTrash(List<File> files) {
		try {
			FileUtils.getInstance().moveToTrash((File[]) files.toArray());
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Set look and feel of windows
	 * 
	 * @return
	 */
	public static boolean setWindowsLookAndFeel() {
		try {
			UIManager.setLookAndFeel(LOOK_AND_FEEL_WINDOWS);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Get root directory for current work directory
	 * 
	 * @return
	 */
	public static String getRootDir() {
		String userDir = System.getProperty(USER_DIR);
		String rootDir = userDir.substring(0, userDir.indexOf(File.separator) + 1);
		if (!rootDir.endsWith(File.separator)) {
			rootDir += File.separator;
		}
		return rootDir;
	}

	/**
	 * Get max window bounds
	 * 
	 * @return
	 */
	public static Bounds getMaxWindowBounds() {
		Rectangle rect = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
		return new Bounds(rect.x, rect.y, rect.width, rect.height);
	}

	/**
	 * Get screen bounds
	 * 
	 * @return
	 */
	public static Bounds getScreenBounds() {
		Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
		return new Bounds(0, 0, size.width, size.height);
	}

}

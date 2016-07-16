package cn.mutils.app.ext;

import java.awt.EventQueue;
import java.awt.Window;
import java.io.File;

import cn.mutils.core.io.FileUtil;
import cn.mutils.core.io.ISystemPrinter;
import cn.mutils.core.io.SystemStream;
import cn.mutils.core.log.Logs;
import cn.mutils.core.runtime.OSRuntime;
import cn.mutils.core.text.StringUtil;

/**
 * Desktop application of framework
 */
public class DesktopApp {

	public static final String APP_DIR = ".app";

	protected static DesktopApp sApp = null;

	protected ISystemPrinter mSystemPrinter = null;

	protected String mAppId;

	protected String mAppDir;

	protected Window mMainWindow;

	public DesktopApp() {
		sApp = this;
		System.setOut(new SystemOutStream());
		System.setErr(new SystemErrStream());
		if (OSRuntime.getOSRuntime() == OSRuntime.WINDOWS) {
			DesktopUtil.setWindowsLookAndFeel();
		}
		// Make directory for all application
		File appDir = new File(DesktopUtil.getRootDir() + APP_DIR);
		if (!appDir.exists()) {
			if (appDir.mkdirs()) {
				FileUtil.setHidden(appDir, true);
			}
		}
	}

	public String getAppDir() {
		if (mAppDir == null) {
			String id = mAppId;
			if (id == null) {
				id = StringUtil.toLowerCaseId(this.getClass());
			}
			mAppDir = DesktopUtil.getRootDir() + APP_DIR + File.separator + id + File.separator;
			File appDirFile = new File(mAppDir);
			if (!appDirFile.exists()) {
				appDirFile.mkdirs();
			}
		}
		return mAppDir;
	}

	public void setAppDir(String appDir) {
		File appDirFile = new File(appDir);
		if (!appDirFile.exists()) {
			appDirFile.mkdirs();
		}
		try {
			mAppDir = appDirFile.getCanonicalPath() + File.separator;
		} catch (Exception e) {

		}
	}

	public String getAppId() {
		return mAppId;
	}

	public void setAppId(String appId) {
		mAppId = appId;
	}

	public synchronized ISystemPrinter getSystemPrinter() {
		if (mSystemPrinter == null) {
			mSystemPrinter = new DesktopPrinter(mMainWindow);
		}
		return mSystemPrinter;
	}

	public synchronized void setSystemPrinter(ISystemPrinter printer) {
		mSystemPrinter = printer;
	}

	public Window getMainWindow() {
		return mMainWindow;
	}

	public void setMainFrame(Window mainWindow) {
		mMainWindow = mainWindow;
	}

	public void start(Class<? extends Window> mainWindowClass) {
		start(mainWindowClass, true, true);
	}

	public void start(Class<? extends Window> mainWindowClass, boolean locationCenter) {
		start(mainWindowClass, locationCenter, true);
	}

	public void start(Class<? extends Window> mainWindowClass, boolean locationCenter, boolean showOnStart) {
		if (mAppId == null) {
			mAppId = StringUtil.toLowerCaseId(mainWindowClass);
		}
		EventQueue.invokeLater(new StartRunnable(mainWindowClass, locationCenter, showOnStart));
	}

	public static DesktopApp getApp() {
		return sApp;
	}

	class StartRunnable implements Runnable {

		protected Class<? extends Window> mMainWindowClass;

		protected boolean mLocationCenter = true;

		protected boolean mShowOnStart = true;

		public StartRunnable(Class<? extends Window> mainWindowClass, boolean locationCenter, boolean showOnStart) {
			mMainWindowClass = mainWindowClass;
			mLocationCenter = locationCenter;
			mShowOnStart = showOnStart;
		}

		@Override
		public void run() {
			try {
				mMainWindow = mMainWindowClass.newInstance();
				if (mLocationCenter) {
					mMainWindow.setLocationRelativeTo(null);
				}
				mMainWindow.setVisible(true);
				if (!mShowOnStart) {
					mMainWindow.setVisible(false);
				}
			} catch (Exception e) {
				Logs.e("", e);
			}
		}
	}

	class SystemOutStream extends SystemStream {

		public SystemOutStream() {
			super(System.out, STREAM_TYPE.OUT);
		}

		@Override
		public void systemOut(String str) {
			getSystemPrinter().systemOut(str);
		}

	}

	class SystemErrStream extends SystemStream {

		public SystemErrStream() {
			super(System.err, STREAM_TYPE.ERR);
		}

		@Override
		public void systemErr(String str) {
			getSystemPrinter().systemErr(str);
		}
	}

}

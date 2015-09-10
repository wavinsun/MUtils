package cn.o.app.ext;

import java.awt.EventQueue;
import java.awt.Window;

import cn.o.app.core.io.ISystemPrinter;
import cn.o.app.core.io.SystemStream;
import cn.o.app.core.runtime.OSRuntime;

/**
 * Desktop application of framework
 */
public class DesktopApp {

	protected static DesktopApp sApp = null;

	protected ISystemPrinter mSystemPrinter = null;

	protected Window mMainWindow;

	public DesktopApp() {
		sApp = this;
		System.setOut(new SystemOutStream());
		System.setErr(new SystemErrStream());
		if (OSRuntime.getOSRuntime() == OSRuntime.WINDOWS) {
			DesktopUtil.setWindowsLookAndFeel();
		}
	}

	public synchronized ISystemPrinter getSystemPrinter() {
		if (mSystemPrinter == null) {
			mSystemPrinter = new DesktopPrinter();
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

			}
		}
	}

	class SystemOutStream extends SystemStream {

		public SystemOutStream() {
			super(System.out, STREAM_TYPE.OUT);
		}

		@Override
		public void sysout(String str) {
			getSystemPrinter().sysout(str);
		}

	}

	class SystemErrStream extends SystemStream {

		public SystemErrStream() {
			super(System.err, STREAM_TYPE.ERR);
		}

		@Override
		public void syserr(String str) {
			getSystemPrinter().syserr(str);
		}
	}

}

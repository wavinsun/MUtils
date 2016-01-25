package cn.mutils.app.ext;

import java.awt.Color;
import java.awt.Dialog;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.Document;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;

import cn.mutils.app.core.graphics.Bounds;
import cn.mutils.app.core.io.ISystemPrinter;

/**
 * Desktop pinter to print system stream to desktop UI.
 */
@SuppressWarnings("serial")
public class DesktopPrinter extends JDialog implements ISystemPrinter {

	/** Console buffer size of default like eclipse */
	public static final int BUFFER_SIZE = 80000;

	protected int mBufferSize = BUFFER_SIZE;

	protected LogText mTextPane;

	protected JScrollPane mScrollPane;

	protected Style mStyleSysout;

	protected Style mStyleSyserr;

	/**
	 * Create the dialog.
	 */
	public DesktopPrinter(Window owner) {
		super(owner);
		setBounds(100, 100, 550, 200);

		mScrollPane = new JScrollPane();
		mScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		mScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		getContentPane().add(mScrollPane);

		mTextPane = new LogText();
		mTextPane.setFont(new Font("微软雅黑", Font.PLAIN, 14));
		mScrollPane.setViewportView(mTextPane);

		String appTitle = null;
		DesktopApp app = DesktopApp.getApp();
		if (app != null) {
			Window w = app.getMainWindow();
			if (w instanceof Frame) {
				appTitle = ((Frame) w).getTitle();
			} else if (w instanceof Dialog) {
				appTitle = ((Dialog) w).getTitle();
			}
		}
		if (appTitle != null && !appTitle.isEmpty()) {
			this.setTitle("日志 - " + appTitle);
		} else {
			this.setTitle("日志");
		}

		mStyleSysout = mTextPane.getStyledDocument().addStyle(null, null);
		mStyleSyserr = mTextPane.addStyle("syserr", mStyleSysout);
		StyleConstants.setForeground(mStyleSyserr, Color.RED);

		this.setLocationRelativeTo(null);
	}

	@Override
	protected void processWindowEvent(WindowEvent e) {
		if (e.getID() == WindowEvent.WINDOW_CLOSING) {
			DesktopApp app = DesktopApp.getApp();
			if (app != null) {
				if (app.getMainWindow() == null) {
					// Main window startup happens exception
					System.exit(0);
				}
			}
		}
		super.processWindowEvent(e);
	}

	public void log(String text) {
		this.log(text, false);
	}

	public void log(String text, boolean err) {
		synchronized (this) {
			Document doc = mTextPane.getDocument();
			try {
				int docLength = doc.getLength();
				if (docLength >= mBufferSize) {
					if (text != null && text.equals("\r\n")) {
						if (docLength >= mBufferSize * 1.5) {
							doc.remove(0, docLength);
							docLength = 0;
						}
					} else {
						doc.remove(0, docLength);
						docLength = 0;
					}
				}
				doc.insertString(docLength, text, err ? mStyleSyserr : mStyleSysout);
			} catch (Exception e) {

			}
		}
		EventQueue.invokeLater(new UpdateRunnable());
	}

	@Override
	public void systemOut(String str) {
		log(str, false);
	}

	@Override
	public void systemErr(String str) {
		log(str, true);
	}

	public static class LogText extends JTextPane {

		@Override
		public boolean getScrollableTracksViewportWidth() {
			return getUI().getPreferredSize(this).width <= getParent().getSize().width;
		}

	}

	class UpdateRunnable implements Runnable {

		@Override
		public void run() {
			mTextPane.scrollRectToVisible(new Rectangle(mScrollPane.getHorizontalScrollBar().getValue(),
					mTextPane.getPreferredSize().height, 0, 0));
			if (!isVisible()) {
				Bounds max = DesktopUtil.getMaxWindowBounds();
				int x = max.intX() + max.intWidth() - getWidth();
				int y = max.intY() + max.intHeight() - getHeight();
				if (x != getX() || y != getY()) {
					setLocation(x, y);
				}
				setVisible(true);
			}
		}
	}

}

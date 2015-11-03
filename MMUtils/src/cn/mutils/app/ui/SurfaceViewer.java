package cn.mutils.app.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * SurfaceViewer of framework<br>
 * High performance requirements for UI.
 */
@SuppressLint("WrongCall")
public class SurfaceViewer extends SurfaceView implements SurfaceHolder.Callback, Runnable {

	protected SurfaceHolder mSurfaceHolder;
	protected Thread mDrawThread;

	public SurfaceViewer(Context context) {
		super(context);
		init(context, null);
	}

	public SurfaceViewer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public SurfaceViewer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	protected void init(Context context, AttributeSet attrs) {
		this.setZOrderOnTop(true);
		mSurfaceHolder = getHolder();
		mSurfaceHolder.setFormat(PixelFormat.TRANSLUCENT);
		mSurfaceHolder.addCallback(this);
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		synchronized (this) {
			if (mDrawThread != null) {
				mDrawThread.interrupt();
			}
			mDrawThread = new Thread(this);
			mDrawThread.start();
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		synchronized (this) {
			if (mDrawThread != null) {
				mDrawThread.interrupt();
				mDrawThread = null;
			}
		}
	}

	public long getDrawThreadId() {
		synchronized (this) {
			return mDrawThread != null ? mDrawThread.getId() : 0;
		}
	}

	@Override
	public void run() {
		while (Thread.currentThread().getId() == getDrawThreadId()) {
			Canvas c = null;
			int saveCount = 0;
			try {
				c = mSurfaceHolder.lockCanvas();
				if (c != null) {
					saveCount = c.save();
					DrawFilter filter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
					c.setDrawFilter(filter);
					c.drawColor(Color.TRANSPARENT, Mode.CLEAR);
					onDraw(c, c.getWidth(), c.getHeight());
				}
				Thread.sleep(16);// 60fps
			} catch (Exception e) {
				if (e != null && (e instanceof InterruptedException)) {
					return;
				}
			} finally {
				if (c != null) {
					try {
						if (saveCount != 0) {
							c.restoreToCount(saveCount);
						}
						mSurfaceHolder.unlockCanvasAndPost(c);
					} catch (Exception e) {
						// java.lang.IllegalStateException
					}
				}
			}
		}
	}

	/**
	 * High performance draw logic of viewer<br>
	 * This is called by owned draw thread.
	 * 
	 * @param canvas
	 * @param width
	 * @param height
	 */
	protected void onDraw(Canvas canvas, int width, int height) {

	}

}

package cn.o.app.ui.pattern;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import cn.o.app.OUtil;

@SuppressLint("ClickableViewAccessibility")
public class PatternCanvas extends View {

	public interface OnPasswordDrawnListener {

		public void onPasswordDrawn(PatternCanvas canvas, String password);

	}

	private Paint mCircleStonePaint;
	private Paint mCircleEarthPaint;
	private Paint mCircleFiredPaint;
	private Paint mCircleGlowPaint;
	private Paint mDotFiredPaint;
	private Paint mDotGlowPaint;
	private Paint mLinePaint;

	private static class Circle {
		public boolean mUsed;
		public Point mCenter;
		public Rect mOuterBound;
	}

	private int mCircleInnerRadius;
	private int mCircleOuterRadius;
	private int mDotInnerRadius;
	private int mDotOuterRadius;
	private Circle[] mCircles = new Circle[0];

	private boolean mDrawingStarted = false;
	private int mX, mY;

	private final int mFireColor = 0xffff5f;
	private final int mStoneColor = 0xc0c0c0;
	private final int mEarthColor = 0xffffff;

	private ArrayList<Integer> mPassword = new ArrayList<Integer>();

	private OnPasswordDrawnListener mOnPasswordDrawnListener;

	public void setOnPasswordDrawnListener(OnPasswordDrawnListener listener) {
		mOnPasswordDrawnListener = listener;
	}

	public PatternCanvas(Context context) {
		super(context);
		init();
	}

	public PatternCanvas(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	private void init() {
		Paint paint = new Paint();
		paint.setAntiAlias(true);

		mCircleStonePaint = new Paint(paint);
		mCircleStonePaint.setColor(0xff000000 | mStoneColor);
		mCircleStonePaint.setStyle(Paint.Style.STROKE);
		mCircleStonePaint.setStrokeWidth(OUtil.getRawSize(getContext(), TypedValue.COMPLEX_UNIT_DIP, 1));

		mCircleEarthPaint = new Paint(paint);
		mCircleEarthPaint.setColor(0x10000000 | mEarthColor);
		mCircleEarthPaint.setStyle(Paint.Style.FILL);

		mCircleFiredPaint = new Paint(mCircleStonePaint);
		mCircleFiredPaint.setColor(0xff000000 | mFireColor);

		mCircleGlowPaint = new Paint(mCircleStonePaint);
		mCircleGlowPaint.setColor(0x40000000 | mFireColor);
		mCircleGlowPaint.setStrokeWidth(OUtil.getRawSize(getContext(), TypedValue.COMPLEX_UNIT_DIP, 6));

		mDotFiredPaint = new Paint(paint);
		mDotFiredPaint.setColor(0xff000000 | mFireColor);
		mDotFiredPaint.setStyle(Paint.Style.FILL);

		mDotGlowPaint = new Paint(mDotFiredPaint);
		mDotGlowPaint.setColor(0x40000000 | mFireColor);

		mLinePaint = new Paint(paint);
		mLinePaint.setColor(0x80000000 | mFireColor);
		mLinePaint.setStyle(Paint.Style.STROKE);
		mLinePaint.setStrokeWidth(OUtil.getRawSize(getContext(), TypedValue.COMPLEX_UNIT_DIP, 8));
	}

	private void initCircles(double w, double h) {
		int cw = (int) Math.floor(Math.min(Math.floor(w / 3.0), Math.floor(h / 3.0)));

		mCircleOuterRadius = (int) Math.floor((double) cw * 0.3);
		mCircleInnerRadius = mCircleOuterRadius - (int) OUtil.getRawSize(getContext(), TypedValue.COMPLEX_UNIT_DIP, 4);

		mDotOuterRadius = (int) Math.floor((double) cw * 0.15);
		mDotInnerRadius = mDotOuterRadius - (int) OUtil.getRawSize(getContext(), TypedValue.COMPLEX_UNIT_DIP, 4);

		int x0, y0, x3, y3;
		if (h > w) {
			x0 = 0;
			y0 = (int) Math.floor((h - w) / 2.0);
			x3 = (int) Math.floor(w);
			y3 = y0 + (int) Math.floor(cw * 3.0);
		} else {
			x0 = (int) Math.floor((w - h) / 2.0);
			y0 = 0;
			x3 = x0 + (int) Math.floor(cw * 3.0);
			y3 = (int) Math.floor(h);
		}

		int x1 = x0 + cw;
		int x2 = x3 - cw;
		int y1 = y0 + cw;
		int y2 = y3 - cw;

		mCircles = new Circle[9];
		for (int i = 0; i < mCircles.length; i++)
			mCircles[i] = new Circle();

		mCircles[0].mOuterBound = new Rect(x0, y0, x1, y1);
		mCircles[1].mOuterBound = new Rect(x0, y1, x1, y2);
		mCircles[2].mOuterBound = new Rect(x0, y2, x1, y3);

		mCircles[3].mOuterBound = new Rect(x1, y0, x2, y1);
		mCircles[4].mOuterBound = new Rect(x1, y1, x2, y2);
		mCircles[5].mOuterBound = new Rect(x1, y2, x2, y3);

		mCircles[6].mOuterBound = new Rect(x2, y0, x3, y1);
		mCircles[7].mOuterBound = new Rect(x2, y1, x3, y2);
		mCircles[8].mOuterBound = new Rect(x2, y2, x3, y3);

		for (int i = 0; i < mCircles.length; i++)
			mCircles[i].mCenter = new Point(mCircles[i].mOuterBound.centerX(), mCircles[i].mOuterBound.centerY());
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);

		initCircles(right - left, bottom - top);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int i, j, m, n, x, y, x1, y1;

		canvas.drawColor(Color.TRANSPARENT);

		// Circle.
		for (i = 0; i < mCircles.length; i++) {
			x = mCircles[i].mCenter.x;
			y = mCircles[i].mCenter.y;

			canvas.drawCircle(x, y, mCircleInnerRadius, mCircleEarthPaint);
			canvas.drawCircle(x, y, mCircleOuterRadius, mCircleStonePaint);
		}

		for (i = 0, j = -1; i < mPassword.size(); i++, j++) {
			m = mPassword.get(i);
			x = mCircles[m].mCenter.x;
			y = mCircles[m].mCenter.y;

			// Circle Glow.
			canvas.drawCircle(x, y, mCircleOuterRadius, mCircleGlowPaint);

			// Circle Highlight.
			canvas.drawCircle(x, y, mCircleOuterRadius, mCircleFiredPaint);

			// Dot Glow.
			canvas.drawCircle(x, y, mDotOuterRadius, mDotGlowPaint);

			// Dot Highlight.
			canvas.drawCircle(x, y, mDotInnerRadius, mDotFiredPaint);

			if (j >= 0) {
				n = mPassword.get(j);
				x1 = mCircles[n].mCenter.x;
				y1 = mCircles[n].mCenter.y;

				canvas.drawLine(x1, y1, x, y, mLinePaint);
			}
		}
		if (mDrawingStarted && i > 0) {
			m = mPassword.get(i - 1);
			x = mCircles[m].mCenter.x;
			y = mCircles[m].mCenter.y;

			canvas.drawLine(x, y, mX, mY, mLinePaint);
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mDrawingStarted = true;
			break;
		case MotionEvent.ACTION_MOVE:
			mX = (int) event.getX();
			mY = (int) event.getY();

			int idx = inCycle(mX, mY);
			if (idx >= 0) {
				if (mPassword.size() > 0) {
					int last = mPassword.get(mPassword.size() - 1);
					int x1 = last % 3;
					int x2 = idx % 3;
					int y1 = last / 3;
					int y2 = idx / 3;

					int dx = Math.abs(x2 - x1);
					int dy = Math.abs(y2 - y1);

					int x3 = -1;
					int y3 = -1;

					if (dx == 2)
						x3 = 1;
					else if (dx == 0)
						x3 = x1;

					if (dy == 2)
						y3 = 1;
					else if (dy == 0)
						y3 = y1;

					if (x3 >= 0 && y3 >= 0) {
						int i = x3 + y3 * 3;
						if (!mPassword.contains(i))
							mPassword.add(i);
					}
				}
				mPassword.add(idx);
			}

			invalidate();
			break;
		case MotionEvent.ACTION_UP:
			mDrawingStarted = false;
			invalidate();

			if (mOnPasswordDrawnListener != null && mPassword.size() > 0) {
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < mPassword.size(); i++)
					sb.append(mPassword.get(i).toString());
				mOnPasswordDrawnListener.onPasswordDrawn(this, sb.toString());
			}
			break;
		}
		return true;
	}

	private int inCycle(int x, int y) {
		int i;
		for (i = 0; i < mCircles.length; i++) {
			if (mCircles[i].mOuterBound.contains(x, y))
				break;
		}
		if (i == mCircles.length)
			return -1;
		if (mCircles[i].mUsed)
			return -1;
		if (Math.sqrt(
				Math.pow(x - mCircles[i].mCenter.x, 2) + Math.pow(y - mCircles[i].mCenter.y, 2)) > mCircleOuterRadius)
			return -1;

		mCircles[i].mUsed = true;
		return i;
	}

	public void refresh() {
		for (int i = 0; i < mCircles.length; i++)
			mCircles[i].mUsed = false;
		mPassword.clear();
		invalidate();
	}

}

package cn.o.app.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import cn.o.app.R;

public class RoundProgressBar extends View {

	protected Paint mPaint = new Paint();
	protected int mRoundColor = Color.GRAY;
	protected int mRoundProgressColor = Color.GREEN;
	protected int mRoundWidth = 8;
	protected int mMax = 100;
	protected float mStartAngle = -90;
	protected RectF mRectF = new RectF();
	protected int mProgress = 0;
	protected float mRatio = 0;
	protected int mSolidColor = Color.WHITE;
	protected Cap mCap = Cap.ROUND;

	public RoundProgressBar(Context context) {
		super(context);
		init(context, null);
	}

	public RoundProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public RoundProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	protected void init(Context context, AttributeSet attrs) {
		if (attrs != null) {
			TypedArray typedArray = context.obtainStyledAttributes(attrs,
					R.styleable.RoundProgressBar);
			mRoundColor = typedArray.getColor(
					R.styleable.RoundProgressBar_roundColor, Color.GRAY);
			mRoundProgressColor = typedArray.getColor(
					R.styleable.RoundProgressBar_roundProgressColor,
					Color.GREEN);
			mRoundWidth = typedArray.getDimensionPixelSize(
					R.styleable.RoundProgressBar_roundWidth, 8);
			mMax = typedArray.getInt(R.styleable.RoundProgressBar_android_max,
					100);
			mProgress = typedArray.getInt(
					R.styleable.RoundProgressBar_android_progress, 0);
			mStartAngle = typedArray.getFloat(
					R.styleable.RoundProgressBar_android_angle, -90);
			mSolidColor = typedArray.getColor(
					R.styleable.RoundProgressBar_android_solidColor,
					Color.WHITE);
			try {
				int cap = typedArray.getInt(
						R.styleable.RoundProgressBar_strokeCap, 1);
				switch (cap) {
				case 0:
					mCap = Cap.BUTT;
					break;
				case 1:
					mCap = Cap.ROUND;
					break;
				case 2:
					mCap = Cap.SQUARE;
					break;
				default:
					mCap = Cap.ROUND;
					break;
				}
			} catch (Exception e) {

			}
			typedArray.recycle();
			setRatio(((float) mProgress) / mMax);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int centerX = getWidth() / 2;
		int centerY = getHeight() / 2;
		int radius = (centerX < centerY ? centerX : centerY) - mRoundWidth / 2;
		mPaint.reset();
		mPaint.setAntiAlias(true);
		mPaint.setStrokeCap(mCap);
		mPaint.setStrokeWidth(mRoundWidth);
		mPaint.setColor(mSolidColor);
		mPaint.setStyle(Paint.Style.FILL);
		canvas.drawCircle(centerX, centerY, radius, mPaint);
		mRectF.set(centerX - radius, centerY - radius, centerX + radius,
				centerY + radius);
		mPaint.setColor(mRoundColor);
		mPaint.setStyle(Paint.Style.STROKE);
		canvas.drawArc(mRectF, mStartAngle, 360 * (mRatio - 1), false, mPaint);
		mPaint.setColor(mRoundProgressColor);
		mPaint.setStyle(Paint.Style.STROKE);
		canvas.drawArc(mRectF, mStartAngle, 360 * mRatio, false, mPaint);
	}

	public int getMax() {
		return mMax;
	}

	public void setMax(int max) {
		max = max <= 0 ? 1 : max;
		mMax = max;
		setRatio(((float) mProgress) / mMax);
	}

	public int getProgress() {
		return mProgress;
	}

	public void setProgress(int progress) {
		progress = progress < 0 ? 0 : progress;
		progress = progress > mMax ? mMax : progress;
		mProgress = progress;
		setRatio(((float) mProgress) / mMax);
	}

	public void setRatio(float ratio) {
		ratio = ratio < 0 ? 0 : ratio;
		ratio = ratio > 1 ? 1 : ratio;
		mRatio = ratio;
		postInvalidate();
	}
}

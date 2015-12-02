package cn.mutils.app.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import cn.mutils.app.R;
import cn.mutils.app.util.AppUtil;

public class ProgressView extends View {

	public static final int SHAPE_RING = 0;
	public static final int SHAPE_LINE = 1;

	protected Paint mPaint = new Paint();
	protected int mProgressBgColor = Color.GRAY;
	protected int mProgressColor = Color.GREEN;
	protected int mProgressWidth = 12;
	protected int mMax = 100;
	protected float mStartAngle = -90;
	protected RectF mRectF = new RectF();
	protected int mProgress = 0;
	protected float mRatio = 0;
	protected int mSolidColor = Color.WHITE;
	protected Cap mCap = Cap.ROUND;
	protected int mShape = SHAPE_RING;
	protected boolean mTextVisible = false;
	protected int mTextSize = 12;
	protected int mTextColor = Color.WHITE;

	public ProgressView(Context context) {
		super(context);
		init(context, null);
	}

	public ProgressView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public ProgressView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	protected void init(Context context, AttributeSet attrs) {
		if (attrs != null) {
			TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressView);
			mProgressBgColor = typedArray.getColor(R.styleable.ProgressView_bgColor, Color.GRAY);
			mProgressColor = typedArray.getColor(R.styleable.ProgressView_strokeColor, Color.GREEN);
			mProgressWidth = typedArray.getDimensionPixelSize(R.styleable.ProgressView_strokeWidth, 8);
			mMax = typedArray.getInt(R.styleable.ProgressView_android_max, 100);
			mProgress = typedArray.getInt(R.styleable.ProgressView_android_progress, 0);
			mStartAngle = typedArray.getFloat(R.styleable.ProgressView_android_angle, -90);
			mSolidColor = typedArray.getColor(R.styleable.ProgressView_android_solidColor, Color.WHITE);
			try {
				int cap = typedArray.getInt(R.styleable.ProgressView_strokeCap, 1);
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
			try {
				int shape = typedArray.getInt(R.styleable.ProgressView_android_shape, 3);
				switch (shape) {
				case 0:// Rectangle
					mShape = SHAPE_LINE;
					break;
				case 1:// Oval
					mShape = SHAPE_RING;
					break;
				case 2:// Line
					mShape = SHAPE_LINE;
					break;
				case 3:// Ring
					mShape = SHAPE_RING;
					break;
				default:
					mShape = SHAPE_RING;
					break;
				}
			} catch (Exception e) {

			}
			try {
				int textVisible = typedArray.getInt(R.styleable.ProgressView_textVisible, 0);
				switch (textVisible) {
				case 0:// invisible
					mTextVisible = false;
					break;
				case 1:// visible
					mTextVisible = true;
					break;
				default:
					mTextVisible = false;
					break;
				}
			} catch (Exception e) {

			}
			mTextColor = typedArray.getColor(R.styleable.ProgressView_android_textColor, Color.BLACK);
			mTextSize = typedArray.getDimensionPixelSize(R.styleable.ProgressView_android_textSize, 12);
			typedArray.recycle();
			setRatio(((float) mProgress) / mMax);
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int w = getWidth();
		int h = getHeight();
		int centerX = w / 2;
		int centerY = h / 2;
		if (mShape == SHAPE_RING) {
			int radius = (centerX < centerY ? centerX : centerY) - mProgressWidth / 2;
			mPaint.reset();
			mPaint.setAntiAlias(true);
			mPaint.setStrokeCap(mCap);
			mPaint.setStrokeWidth(mProgressWidth);
			mPaint.setColor(mSolidColor);
			mPaint.setStyle(Paint.Style.FILL);
			canvas.drawCircle(centerX, centerY, radius, mPaint);
			mRectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
			mPaint.setColor(mProgressBgColor);
			mPaint.setStyle(Paint.Style.STROKE);
			canvas.drawArc(mRectF, mStartAngle, 360 * (mRatio - 1), false, mPaint);
			mPaint.setColor(mProgressColor);
			mPaint.setStyle(Paint.Style.STROKE);
			canvas.drawArc(mRectF, mStartAngle, 360 * mRatio, false, mPaint);
		} else {
			int stokePadding = mProgressWidth / 2;
			w = w - mProgressWidth;
			mPaint.reset();
			mPaint.setAntiAlias(true);
			mPaint.setStrokeCap(mCap);
			mPaint.setStrokeWidth(mProgressWidth);
			mPaint.setColor(mProgressBgColor);
			mPaint.setStyle(Paint.Style.STROKE);
			canvas.drawLine(stokePadding, centerY, stokePadding + w, centerY, mPaint);
			mPaint.setColor(mProgressColor);
			mPaint.setStyle(Paint.Style.STROKE);
			float progressLineWidth = w * mRatio;
			canvas.drawLine(stokePadding, centerY, stokePadding + progressLineWidth, centerY, mPaint);
			if (mTextVisible) {
				String text = Math.round(mRatio * 100) + "%";
				mPaint.reset();
				mPaint.setAntiAlias(true);
				mPaint.setColor(mTextColor);
				mPaint.setTextSize(mTextSize);
				float textWidth = mPaint.measureText(text);
				float textX = stokePadding + (textWidth >= progressLineWidth ? 0 : (progressLineWidth - textWidth));
				canvas.drawText(text, textX, AppUtil.getYOfDrawText(mPaint, centerY), mPaint);
			}
		}
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
		setRatio(((float) progress) / mMax);
	}

	public void setRatio(float ratio) {
		ratio = ratio < 0 ? 0 : ratio;
		ratio = ratio > 1 ? 1 : ratio;
		mRatio = ratio;
		mProgress = Math.round(mRatio * mMax);
		postInvalidate();
	}

	public int getShape() {
		return mShape;
	}

	public void setShape(int shape) {
		if (mShape == shape) {
			return;
		}
		mShape = shape;
		postInvalidate();
	}

}

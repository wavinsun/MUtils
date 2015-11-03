package cn.mutils.app.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import cn.mutils.app.AppUtil;

public class SwitchButton extends View {

	public static interface OnCheckedChangeListener {

		public boolean onCheckedChanging(SwitchButton v, boolean checked);

		public void onCheckedChanged(SwitchButton v, boolean checked);

	}

	protected int mChromeColor = 0xFF4DD963;

	protected int mBorderColor = 0xFFDADADA;

	protected Paint mPaint = new Paint();

	protected Path mPath = new Path();

	protected RectF mRect = new RectF();

	protected boolean mChecked;

	protected int mPadding;

	protected int mWrapContentWidth;

	protected int mWrapContentHeight;

	protected OnCheckedChangeListener mListener;

	public SwitchButton(Context context) {
		super(context);
		init(context, null);
	}

	public SwitchButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public SwitchButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	protected void init(Context context, AttributeSet attrs) {
		DisplayMetrics metrics = AppUtil.getDisplayMetrics(context);
		mWrapContentWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 46, metrics);
		mWrapContentHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 26, metrics);
		mPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, metrics);
		int minWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, metrics);
		this.setMinimumWidth(minWidth);
		int minHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, metrics);
		this.setMinimumHeight(minHeight);
		super.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mListener != null) {
					if (mListener.onCheckedChanging((SwitchButton) v, mChecked)) {
						setChecked(!mChecked);
					}
				} else {
					setChecked(!mChecked);
				}
			}
		});
	}

	@Override
	protected void onDraw(Canvas canvas) {
		int w = this.getWidth();
		int h = this.getHeight();
		int padding2 = mPadding << 1;
		int r = (int) ((w - 4 - padding2) * 0.295);
		int r2 = r << 1;
		int offsetX = mPadding;
		int offsetY = mPadding + ((h - padding2) >> 1) - r - 1;
		mPath.reset();
		mPath.moveTo(offsetX + r + 1, offsetY);
		mPath.lineTo(w - r - 1 - offsetX, offsetY);
		mRect.set(w - r2 - 2 - offsetX, offsetY, w - offsetX, offsetY + r2 + 2);
		mPath.arcTo(mRect, 270, 180);
		mPath.lineTo(offsetX + r + 1, offsetY + r2 + 2);
		mRect.set(offsetX, offsetY, offsetX + r2 + 2, offsetY + r2 + 2);
		mPath.arcTo(mRect, 90, 180);
		mPath.close();
		mPaint.reset();
		mPaint.setAntiAlias(true);
		if (mChecked) {
			mPaint.setStyle(Paint.Style.FILL);
			mPaint.setColor(mChromeColor);
			canvas.drawPath(mPath, mPaint);

			int circleX = w - r - 1 - offsetX;
			int circleY = offsetY + r + 1;
			mPaint.setStyle(Paint.Style.FILL);
			mPaint.setColor(0xFFFFFFFF);
			canvas.drawCircle(circleX, circleY, r, mPaint);
		} else {
			mPaint.setStyle(Paint.Style.FILL);
			mPaint.setColor(0xFFFFFFFF);
			canvas.drawPath(mPath, mPaint);
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setColor(mBorderColor);
			mPaint.setStrokeWidth(1);
			canvas.drawPath(mPath, mPaint);

			int circleX = offsetX + r + 1;
			int circleY = offsetY + r + 1;
			mPaint.setStyle(Paint.Style.FILL);
			mPaint.setColor(0xFFFFFFFF);
			canvas.drawCircle(circleX, circleY, r, mPaint);
			mPaint.setStyle(Paint.Style.STROKE);
			mPaint.setColor(mBorderColor);
			mPaint.setStrokeWidth(1);
			canvas.drawCircle(circleX, circleY, r, mPaint);
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
			widthMeasureSpec = MeasureSpec.makeMeasureSpec(mWrapContentWidth, MeasureSpec.EXACTLY);
		}
		if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(mWrapContentHeight, MeasureSpec.EXACTLY);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	public boolean isChecked() {
		return mChecked;
	}

	public void setChecked(boolean checked) {
		mChecked = checked;
		invalidate();
		if (mListener != null) {
			mListener.onCheckedChanged(SwitchButton.this, mChecked);
		}
	}

	public int getChromeColor() {
		return mChromeColor;
	}

	public void setChromeColor(int chromeColor) {
		mChromeColor = chromeColor;
		invalidate();
	}

	public int getBorderColor() {
		return mBorderColor;
	}

	public void setBorderColor(int borderColor) {
		mBorderColor = borderColor;
		invalidate();
	}

	@Override
	public void setOnClickListener(OnClickListener l) {
		throw new IllegalArgumentException("You can not call this method");
	}

	public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
		mListener = listener;
	}

}

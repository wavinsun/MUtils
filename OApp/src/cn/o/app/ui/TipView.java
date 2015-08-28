package cn.o.app.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import cn.o.app.AppUtil;
import cn.o.app.R;

/**
 * Round rectangle tip view width triangle
 */
@SuppressWarnings("unused")
public class TipView extends RelativeLayout {

	public static final int TRIANGLE_ALIGN_TOP_LEFT = 0;
	public static final int TRIANGLE_ALIGN_TOP_CENTER = 1;
	public static final int TRIANGLE_ALIGN_TOP_RIGHT = 2;
	public static final int TRIANGLE_ALIGN_BOTTOM_LEFT = 3;
	public static final int TRIANGLE_ALIGN_BOTTOM_CENTER = 4;
	public static final int TRIANGLE_ALIGN_BOTTOM_RIGHT = 5;

	protected int mTopLeftRadius = 8;

	protected int mTopRightRadius = 8;

	protected int mBottomLeftRadius = 8;

	protected int mBottomRightRadius = 8;

	protected int mTriangleHeight = 12;

	protected float mTriangleAngle = 77;

	protected int mTriangleAlign = TRIANGLE_ALIGN_TOP_RIGHT;

	protected int mTrianlgePadding = 8;

	protected int mSolidColor = 0xFFFFFFFF;

	protected int mStrokeColor = 0xFFD0D0D0;

	protected int mStrokeWidth = 1;

	protected int mPointToScreenX;

	protected int mPointToScreenY;

	protected Path mPath = new Path();

	protected Paint mPaint = new Paint();

	protected int[] mScreenLocation = new int[2];

	public TipView(Context context) {
		super(context);
		init(context, null);
	}

	public TipView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public TipView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	protected void init(Context context, AttributeSet attrs) {
		setWillNotDraw(false);
		if (attrs != null) {
			TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TipView);
			mSolidColor = typedArray.getColor(R.styleable.TipView_android_solidColor, mSolidColor);
			mStrokeColor = typedArray.getColor(R.styleable.TipView_strokeColor, mStrokeColor);
			mStrokeWidth = typedArray.getDimensionPixelSize(R.styleable.TipView_strokeWidth, mStrokeWidth);
			int radius = typedArray.getDimensionPixelSize(R.styleable.TipView_android_radius, -1);
			if (radius != -1) {
				mTopLeftRadius = mTopRightRadius = mBottomLeftRadius = mBottomRightRadius = radius;
			}
			mTopLeftRadius = typedArray.getDimensionPixelSize(R.styleable.TipView_android_topLeftRadius,
					mTopLeftRadius);
			mTopRightRadius = typedArray.getDimensionPixelSize(R.styleable.TipView_android_topRightRadius,
					mTopRightRadius);
			mBottomLeftRadius = typedArray.getDimensionPixelSize(R.styleable.TipView_android_bottomLeftRadius,
					mBottomLeftRadius);
			mBottomRightRadius = typedArray.getDimensionPixelSize(R.styleable.TipView_android_bottomRightRadius,
					mBottomRightRadius);
			mTrianlgePadding = typedArray.getDimensionPixelSize(R.styleable.TipView_trianglePadding, mTrianlgePadding);
			mTriangleHeight = typedArray.getDimensionPixelSize(R.styleable.TipView_triangleHeight, mTriangleHeight);
			mTriangleAngle = typedArray.getFloat(R.styleable.TipView_triangleAngle, mTriangleAngle);
			try {
				mTriangleAlign = typedArray.getInt(R.styleable.TipView_triangleAlign, mTriangleAlign);
			} catch (Exception e) {

			}
			typedArray.recycle();
		}
	}

	public int getTriangleAlign() {
		return mTriangleAlign;
	}

	public void setTriangleAlign(int triangleAlign) {
		switch (triangleAlign) {
		case TRIANGLE_ALIGN_TOP_LEFT:
			mTriangleAlign = TRIANGLE_ALIGN_TOP_LEFT;
			break;
		case TRIANGLE_ALIGN_TOP_CENTER:
			mTriangleAlign = TRIANGLE_ALIGN_TOP_CENTER;
			break;
		case TRIANGLE_ALIGN_TOP_RIGHT:
			mTriangleAlign = TRIANGLE_ALIGN_TOP_RIGHT;
			break;
		case TRIANGLE_ALIGN_BOTTOM_LEFT:
			mTriangleAlign = TRIANGLE_ALIGN_BOTTOM_LEFT;
			break;
		case TRIANGLE_ALIGN_BOTTOM_CENTER:
			mTriangleAlign = TRIANGLE_ALIGN_BOTTOM_CENTER;
			break;
		case TRIANGLE_ALIGN_BOTTOM_RIGHT:
			mTriangleAlign = TRIANGLE_ALIGN_BOTTOM_RIGHT;
			break;
		default:
			break;
		}
		postInvalidate();
	}

	public void pointTo(View v) {
		int[] vLocation = new int[2];
		v.getLocationOnScreen(vLocation);
		pointTo(vLocation[0] + v.getWidth() / 2, vLocation[1] + v.getHeight() / 2);
	}

	public void pointTo(Rect screenRect) {
		pointTo(screenRect.left + screenRect.width() / 2, screenRect.top + screenRect.height() / 2);
	}

	public void pointTo(int screenX, int screenY) {
		mPointToScreenX = screenX;
		mPointToScreenY = screenY;
		postInvalidate();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		float l = mStrokeWidth * 0.5F;// left
		float t = l;// top
		float r = getWidth() - l;// right
		float b = getHeight() - t;// bottom
		float triangleBottomRadius = (float) Math.tan(AppUtil.TO_RADIAN * mTriangleAngle / 2) * mTriangleHeight;
		float triangleTopX = 0, triangleTopY = 0;
		boolean canDrawTriangle = false;
		boolean pointValid = mPointToScreenX != 0 || mPointToScreenY != 0;
		int pointX = 0, pointY = 0;
		if (pointValid) {
			this.getLocationOnScreen(mScreenLocation);
		}
		if (mTriangleAlign == TRIANGLE_ALIGN_TOP_LEFT) {
			canDrawTriangle = mTopLeftRadius + mTopRightRadius + mTrianlgePadding * 2 + triangleBottomRadius * 2 < r
					- l;
			canDrawTriangle = canDrawTriangle && mTriangleHeight < b - t;
			if (canDrawTriangle) {
				if (pointValid) {
					pointX = mPointToScreenX - mScreenLocation[0];
					pointY = mPointToScreenY - mScreenLocation[1];
				}
				pointValid = pointValid
						? (pointX > (l + mTopLeftRadius + mTrianlgePadding + triangleBottomRadius)
								&& pointX < (r - mTopRightRadius - mTrianlgePadding - triangleBottomRadius))
						: pointValid;
				if (pointValid) {
					triangleTopX = pointX;
					triangleTopY = t;
				} else {
					triangleTopX = l + mTopLeftRadius + mTrianlgePadding + triangleBottomRadius;
					triangleTopY = t;
				}
			}
		} else if (mTriangleAlign == TRIANGLE_ALIGN_TOP_CENTER) {
			canDrawTriangle = mTopLeftRadius + mTopRightRadius + mTrianlgePadding * 2 + triangleBottomRadius * 2 < r
					- l;
			canDrawTriangle = canDrawTriangle && mTriangleHeight < b - t;
			if (canDrawTriangle) {
				if (pointValid) {
					pointX = mPointToScreenX - mScreenLocation[0];
					pointY = mPointToScreenY - mScreenLocation[1];
				}
				pointValid = pointValid
						? (pointX > (l + mTopLeftRadius + mTrianlgePadding + triangleBottomRadius)
								&& pointX < (r - mTopRightRadius - mTrianlgePadding - triangleBottomRadius))
						: pointValid;
				if (pointValid) {
					triangleTopX = pointX;
					triangleTopY = t;
				} else {
					triangleTopX = l + (r - l) / 2;
					triangleTopY = t;
				}
			}
		} else if (mTriangleAlign == TRIANGLE_ALIGN_TOP_RIGHT) {
			canDrawTriangle = mTopLeftRadius + mTopRightRadius + mTrianlgePadding * 2 + triangleBottomRadius * 2 < r
					- l;
			canDrawTriangle = canDrawTriangle && mTriangleHeight < b - t;
			if (canDrawTriangle) {
				if (pointValid) {
					pointX = mPointToScreenX - mScreenLocation[0];
					pointY = mPointToScreenY - mScreenLocation[1];
				}
				pointValid = pointValid
						? (pointX > (l + mTopLeftRadius + mTrianlgePadding + triangleBottomRadius)
								&& pointX < (r - mTopRightRadius - mTrianlgePadding - triangleBottomRadius))
						: pointValid;
				if (pointValid) {
					triangleTopX = pointX;
					triangleTopY = t;
				} else {
					triangleTopX = r - mTopRightRadius - mTrianlgePadding - triangleBottomRadius;
					triangleTopY = t;
				}
			}
		} else if (mTriangleAlign == TRIANGLE_ALIGN_BOTTOM_LEFT) {
			canDrawTriangle = mBottomLeftRadius + mBottomRightRadius + mTrianlgePadding * 2
					+ triangleBottomRadius * 2 < r - l;
			canDrawTriangle = canDrawTriangle && mTriangleHeight < b - t;
			if (canDrawTriangle) {
				if (pointValid) {
					pointX = mPointToScreenX - mScreenLocation[0];
					pointY = mPointToScreenY - mScreenLocation[1];
				}
				pointValid = pointValid
						? (pointX > (l + mTopLeftRadius + mTrianlgePadding + triangleBottomRadius)
								&& pointX < (r - mTopRightRadius - mTrianlgePadding - triangleBottomRadius))
						: pointValid;
				if (pointValid) {
					triangleTopX = pointX;
					triangleTopY = b;
				} else {
					triangleTopX = l + mTopLeftRadius + mTrianlgePadding + triangleBottomRadius;
					triangleTopY = b;
				}
			}
		} else if (mTriangleAlign == TRIANGLE_ALIGN_BOTTOM_CENTER) {
			canDrawTriangle = mBottomLeftRadius + mBottomRightRadius + mTrianlgePadding * 2
					+ triangleBottomRadius * 2 < r - l;
			canDrawTriangle = canDrawTriangle && mTriangleHeight < b - t;
			if (canDrawTriangle) {
				if (pointValid) {
					pointX = mPointToScreenX - mScreenLocation[0];
					pointY = mPointToScreenY - mScreenLocation[1];
				}
				pointValid = pointValid
						? (pointX > (l + mTopLeftRadius + mTrianlgePadding + triangleBottomRadius)
								&& pointX < (r - mTopRightRadius - mTrianlgePadding - triangleBottomRadius))
						: pointValid;
				if (pointValid) {
					triangleTopX = pointX;
					triangleTopY = b;
				} else {
					triangleTopX = l + (r - l) / 2;
					triangleTopY = b;
				}
			}
		} else if (mTriangleAlign == TRIANGLE_ALIGN_BOTTOM_RIGHT) {
			canDrawTriangle = mBottomLeftRadius + mBottomRightRadius + mTrianlgePadding * 2
					+ triangleBottomRadius * 2 < r - l;
			canDrawTriangle = canDrawTriangle && mTriangleHeight < b - t;
			if (canDrawTriangle) {
				if (pointValid) {
					pointX = mPointToScreenX - mScreenLocation[0];
					pointY = mPointToScreenY - mScreenLocation[1];
				}
				pointValid = pointValid
						? (pointX > (l + mTopLeftRadius + mTrianlgePadding + triangleBottomRadius)
								&& pointX < (r - mTopRightRadius - mTrianlgePadding - triangleBottomRadius))
						: pointValid;
				if (pointValid) {
					triangleTopX = pointX;
					triangleTopY = b;
				} else {
					triangleTopX = r - mTopRightRadius - mTrianlgePadding - triangleBottomRadius;
					triangleTopY = b;
				}
			}
		} else {
			return;
		}
		float x = l, y = t;
		mPaint.reset();
		mPaint.setAntiAlias(true);
		mPaint.setStyle(Style.FILL);
		mPaint.setColor(mSolidColor);
		mPath.reset();
		if (canDrawTriangle && triangleTopY == t) {
			y += mTriangleHeight;
		}
		x += mTopLeftRadius;
		mPath.moveTo(x, y);
		if (canDrawTriangle && triangleTopY == t) {
			x = triangleTopX - triangleBottomRadius;
			mPath.lineTo(x, y);
			mPath.lineTo(triangleTopX, triangleTopY);
			x = triangleTopX + triangleBottomRadius;
			mPath.lineTo(x, y);
		}
		x = r - mTopRightRadius;
		mPath.lineTo(x, y);
		mPath.quadTo(r, y, r, y + mTopRightRadius);
		x = r;
		y = b;
		if (canDrawTriangle && triangleTopY == b) {
			y -= mTriangleHeight;
		}
		y -= mBottomRightRadius;
		mPath.lineTo(x, y);
		y += mBottomRightRadius;
		mPath.quadTo(r, y, r - mBottomRightRadius, y);
		if (canDrawTriangle && triangleTopY == b) {
			x = triangleTopX + triangleBottomRadius;
			mPath.lineTo(x, y);
			mPath.lineTo(triangleTopX, triangleTopY);
			x = triangleTopX - triangleBottomRadius;
			mPath.lineTo(x, y);
		}
		x = l + mBottomLeftRadius;
		mPath.lineTo(x, y);
		mPath.quadTo(l, y, l, y - mBottomLeftRadius);
		x = l;
		y = t;
		if (canDrawTriangle && triangleTopY == t) {
			y += mTriangleHeight;
		}
		y += mTopLeftRadius;
		mPath.lineTo(x, y);
		y -= mTopLeftRadius;
		mPath.quadTo(l, y, l + mTopLeftRadius, y);
		mPath.close();
		canvas.drawPath(mPath, mPaint);
		if (mStrokeWidth != 0) {
			mPaint.setStyle(Style.STROKE);
			mPaint.setStrokeWidth(mStrokeWidth);
			mPaint.setColor(mStrokeColor);
			canvas.drawPath(mPath, mPaint);
		}
	}
}

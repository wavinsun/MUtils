package cn.o.app.ui;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;
import cn.o.app.R;
import cn.o.app.ui.core.IDefaultDrawableView;

@SuppressWarnings("deprecation")
public class OImageView extends ImageView implements IDefaultDrawableView {
	protected static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;
	protected static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
	protected static final int COLOR_DRAWABLE_DIMENSION = 1;
	protected static final int DEFAULT_BORDER_WIDTH = 0;
	protected static final int DEFAULT_BORDER_COLOR = Color.WHITE;
	protected static final int DEFAULT_RADIUS = 8;

	protected WeakReference<Bitmap> mBitmapRef = new WeakReference<Bitmap>(null);
	protected BitmapShader mBitmapShader;
	protected Matrix mBitmapShaderMatrix = new Matrix();
	protected int mBorderColor = DEFAULT_BORDER_COLOR;
	protected int mBorderWidth = DEFAULT_BORDER_WIDTH;
	protected Paint mPaint = new Paint();

	protected RectF mRectF = new RectF();

	protected boolean mReady;

	protected Drawable mDefaultDrawable;

	protected boolean mIsCircle;
	protected boolean mIsRoundRect;
	protected int mRoundRectRadius = DEFAULT_RADIUS;

	public OImageView(Context context) {
		super(context);
		init(context, null);
	}

	public OImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public OImageView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	protected void init(Context context, AttributeSet attrs) {
		if (attrs != null) {
			TypedArray typedArray = context.obtainStyledAttributes(attrs,
					R.styleable.OImageView);
			mBorderWidth = typedArray.getDimensionPixelSize(
					R.styleable.OImageView_borderWidth, DEFAULT_BORDER_WIDTH);
			mBorderColor = typedArray.getColor(
					R.styleable.OImageView_borderColor, DEFAULT_BORDER_COLOR);
			mDefaultDrawable = typedArray
					.getDrawable(R.styleable.OImageView_drawableDefault);
			try {
				int shape = typedArray.getInt(R.styleable.OImageView_shape, 0);
				switch (shape) {
				case 0:// 矩形
					mIsCircle = false;
					mIsRoundRect = false;
					break;
				case 1:// 圆矩形
					mIsCircle = false;
					mIsRoundRect = true;
					break;
				case 2:// 圆形
					mIsCircle = true;
					mIsRoundRect = false;
					break;
				default:
					mIsCircle = false;
					mIsRoundRect = false;
					break;
				}
			} catch (Exception e) {

			}
			if (mIsRoundRect) {
				if (mIsCircle) {
					mIsRoundRect = false;
				}
			}
			typedArray.recycle();
		}
		if (mIsCircle) {
			setScaleType(SCALE_TYPE);
		}
		mReady = true;
		setup();
	}

	@Override
	public void setScaleType(ScaleType scaleType) {
		if (mIsCircle) {
			if (scaleType != SCALE_TYPE) {
				throw new UnsupportedOperationException();
			}
		}
		super.setScaleType(scaleType);
	}

	public boolean isCircle() {
		return mIsCircle;
	}

	public void setIsCircle(boolean isCircle) {
		if (isCircle) {
			if (mIsRoundRect) {
				mIsRoundRect = false;
			}
		}
		mIsCircle = isCircle;
		setup();
	}

	public boolean isRoundRect() {
		return mIsRoundRect;
	}

	public void setIsRoundRect(boolean isRoundRect) {
		if (isRoundRect) {
			if (mIsCircle) {
				mIsCircle = false;
			}
		}
		mIsRoundRect = isRoundRect;
		setup();
	}

	public int getBorderColor() {
		return mBorderColor;
	}

	public void setBorderColor(int borderColor) {
		if (mBorderColor == borderColor) {
			return;
		}
		mBorderColor = borderColor;
		invalidate();
	}

	public int getBorderWidth() {
		return mBorderWidth;
	}

	public void setBorderWidth(int borderWidth) {
		if (mBorderWidth == borderWidth) {
			return;
		}
		mBorderWidth = borderWidth;
		setup();
	}

	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(bm);
		if (mIsCircle || mIsRoundRect) {
			mBitmapRef = new WeakReference<Bitmap>(getBitmapFromDrawable());
		}
		setup();
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		if (mIsCircle || mIsRoundRect) {
			mBitmapRef = new WeakReference<Bitmap>(getBitmapFromDrawable());
		}
		setup();
	}

	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		if (mIsCircle || mIsRoundRect) {
			mBitmapRef = new WeakReference<Bitmap>(getBitmapFromDrawable());
		}
		setup();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
		setup();
	}

	protected void setup() {
		if (!mReady) {
			return;
		}
		if (!mIsCircle && !mIsRoundRect) {
			invalidate();
			return;
		}
		Bitmap bitmap = mBitmapRef.get();
		if (bitmap == null) {
			bitmap = getBitmapFromDrawable();
			if (bitmap == null) {
				invalidate();
				return;
			}
			mBitmapRef = new WeakReference<Bitmap>(bitmap);
		}
		mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP,
				Shader.TileMode.CLAMP);

		int bitmapWidth = bitmap.getWidth();
		int bitmapHeight = bitmap.getHeight();

		int w = getWidth();
		int h = getHeight();
		float drawableWidth = w - mBorderWidth * 2;
		float drawableHeight = h - mBorderWidth * 2;

		// update shader matrix
		float scale = 0, dx = 0, dy = 0;
		mBitmapShaderMatrix.reset();
		if (bitmapWidth * drawableHeight > drawableWidth * bitmapHeight) {
			scale = drawableHeight / (float) bitmapHeight;
			dx = (drawableWidth - bitmapWidth * scale) * 0.5f;
		} else {
			scale = drawableWidth / (float) bitmapWidth;
			dy = (drawableHeight - bitmapHeight * scale) * 0.5f;
		}
		mBitmapShaderMatrix.setScale(scale, scale);
		mBitmapShaderMatrix.postTranslate(dx + mBorderWidth, dy + mBorderWidth);
		mBitmapShader.setLocalMatrix(mBitmapShaderMatrix);

		invalidate();
	}

	protected Bitmap getBitmapFromDrawable() {
		Drawable drawable = getDrawable();
		if (drawable == null) {
			return null;
		}
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}
		try {
			Bitmap bitmap = null;
			if (drawable instanceof ColorDrawable) {
				bitmap = Bitmap.createBitmap(COLOR_DRAWABLE_DIMENSION,
						COLOR_DRAWABLE_DIMENSION, BITMAP_CONFIG);
			} else {
				bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
						drawable.getIntrinsicHeight(), BITMAP_CONFIG);
			}
			Canvas canvas = new Canvas(bitmap);
			drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
			drawable.draw(canvas);
			return bitmap;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	protected void onDraw(Canvas canvas) {
		if (getDrawable() == null) {
			return;
		}
		if (!mIsCircle && !mIsRoundRect) {
			super.onDraw(canvas);
			return;
		}
		int w = getWidth();
		int h = getHeight();
		float halfBorderWidth = mBorderWidth * 0.5f;
		if (mIsCircle) {
			float cx = w * 0.5f;
			float cy = h * 0.5f;
			float borderRadius = cx < cy ? cx : cy;
			float drawableRadius = borderRadius - mBorderWidth;
			borderRadius = drawableRadius + halfBorderWidth;
			mPaint.reset();
			mPaint.setAntiAlias(true);
			mPaint.setFilterBitmap(true);
			mPaint.setShader(mBitmapShader);
			canvas.drawCircle(cx, cy, drawableRadius, mPaint);
			if (mBorderWidth > 0) {
				mPaint.reset();
				mPaint.setAntiAlias(true);
				mPaint.setColor(mBorderColor);
				mPaint.setStyle(Paint.Style.STROKE);
				mPaint.setStrokeWidth(mBorderWidth);
				canvas.drawCircle(cx, cy, borderRadius, mPaint);
			}
		} else if (mIsRoundRect) {
			mRectF.set(0, 0, w, h);
			mPaint.reset();
			mPaint.setAntiAlias(true);
			mPaint.setFilterBitmap(true);
			mPaint.setShader(mBitmapShader);
			canvas.drawRoundRect(mRectF, mRoundRectRadius, mRoundRectRadius,
					mPaint);
		}
	}

	@Override
	public Drawable getDefault() {
		return mDefaultDrawable;
	}

	@Override
	public void setDefault(Drawable drawable) {
		mDefaultDrawable = drawable;
	}

	@Override
	public void setDefault(int resId) {
		mDefaultDrawable = this.getContext().getResources().getDrawable(resId);
	}

	@Override
	public void showDefault() {
		Drawable showingDrawable = getDrawable();
		if (showingDrawable != null) {
			if (showingDrawable.equals(mDefaultDrawable)) {
				return;
			}
		}
		this.setImageDrawable(mDefaultDrawable);
	}

	@Override
	public void showDefault(Drawable drawable) {
		setDefault(drawable);
		showDefault();
	}

	@Override
	public void showDefault(int resId) {
		setDefault(resId);
		showDefault();
	}

}

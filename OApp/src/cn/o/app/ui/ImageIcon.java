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

/**
 * ImageView of framework.
 * 
 * Provide circle shape and round rectangle for content image.
 */
@SuppressWarnings("deprecation")
public class ImageIcon extends ImageView implements IDefaultDrawableView {

	public static final int SHAPE_CIRCLE = 0;
	public static final int SHAPE_RECT = 1;
	public static final int SHAPE_ROUND_RECT = 2;

	protected static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;
	protected static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
	protected static final int COLOR_DRAWABLE_DIMENSION = 1;
	protected static final int DEFAULT_BORDER_WIDTH = 0;
	protected static final int DEFAULT_BORDER_COLOR = Color.WHITE;

	protected WeakReference<Bitmap> mBitmapRef = new WeakReference<Bitmap>(null);
	protected BitmapShader mBitmapShader;
	protected Matrix mBitmapShaderMatrix = new Matrix();
	protected int mBorderColor = DEFAULT_BORDER_COLOR;
	protected int mBorderWidth = DEFAULT_BORDER_WIDTH;
	protected int mCornerRadius = 0;
	protected Paint mPaint = new Paint();
	protected RectF mRectF = new RectF();

	protected boolean mReady;

	protected Drawable mDefaultDrawable;
	protected int mShape = SHAPE_CIRCLE;

	public ImageIcon(Context context) {
		super(context);
		init(context, null);
	}

	public ImageIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public ImageIcon(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	protected void init(Context context, AttributeSet attrs) {
		if (attrs != null) {
			TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ImageIcon);
			mBorderWidth = typedArray.getDimensionPixelSize(R.styleable.ImageIcon_borderWidth, DEFAULT_BORDER_WIDTH);
			mBorderColor = typedArray.getColor(R.styleable.ImageIcon_borderColor, DEFAULT_BORDER_COLOR);
			mDefaultDrawable = typedArray.getDrawable(R.styleable.ImageIcon_drawableDefault);
			mCornerRadius = typedArray.getDimensionPixelSize(R.styleable.ImageIcon_android_radius, 0);
			try {
				int shape = typedArray.getInt(R.styleable.ImageIcon_android_shape, 1);
				switch (shape) {
				case 0:// Rectangle
					mShape = SHAPE_RECT;
					break;
				case 1:// Oval
					mShape = SHAPE_CIRCLE;
					break;
				case 2:// Line
					mShape = SHAPE_RECT;
					break;
				case 3:// Ring
					mShape = SHAPE_CIRCLE;
					break;
				default:
					mShape = SHAPE_CIRCLE;
					break;
				}
			} catch (Exception e) {

			}
			mCornerRadius = typedArray.getDimensionPixelSize(R.styleable.ImageIcon_android_radius, 0);
			if (mCornerRadius != 0 && mShape == SHAPE_RECT) {
				mShape = SHAPE_ROUND_RECT;
			}
			typedArray.recycle();
		}
		if (mShape == SHAPE_CIRCLE) {
			setScaleType(SCALE_TYPE);
		}
		mReady = true;
		setup();
	}

	@Override
	public void setScaleType(ScaleType scaleType) {
		if (mShape == SHAPE_CIRCLE) {
			if (scaleType != SCALE_TYPE) {
				throw new UnsupportedOperationException();
			}
		}
		super.setScaleType(scaleType);
	}

	public int getShape() {
		return mShape;
	}

	public void setShape(int shape) {
		if (mShape == shape) {
			return;
		}
		mShape = shape;
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
		if (mShape != SHAPE_RECT) {
			mBitmapRef = new WeakReference<Bitmap>(getBitmapFromDrawable());
		}
		setup();
	}

	@Override
	public void setImageDrawable(Drawable drawable) {
		super.setImageDrawable(drawable);
		if (mShape != SHAPE_RECT) {
			mBitmapRef = new WeakReference<Bitmap>(getBitmapFromDrawable());
		}
		setup();
	}

	@Override
	public void setImageResource(int resId) {
		super.setImageResource(resId);
		if (mShape != SHAPE_RECT) {
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
		if (mShape == SHAPE_RECT) {
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
		mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

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
				bitmap = Bitmap.createBitmap(COLOR_DRAWABLE_DIMENSION, COLOR_DRAWABLE_DIMENSION, BITMAP_CONFIG);
			} else {
				bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
						BITMAP_CONFIG);
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
		if (mShape == SHAPE_RECT) {
			super.onDraw(canvas);
			return;
		}
		int w = getWidth();
		int h = getHeight();
		float halfBorderWidth = mBorderWidth * 0.5f;
		if (mShape == SHAPE_CIRCLE) {
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
		} else if (mShape == SHAPE_ROUND_RECT) {
			mRectF.set(0, 0, w, h);
			mPaint.reset();
			mPaint.setAntiAlias(true);
			mPaint.setFilterBitmap(true);
			mPaint.setShader(mBitmapShader);
			canvas.drawRoundRect(mRectF, mCornerRadius, mCornerRadius, mPaint);
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

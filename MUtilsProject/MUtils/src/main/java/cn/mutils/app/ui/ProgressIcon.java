package cn.mutils.app.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import cn.mutils.app.R;
import cn.mutils.app.util.AppUtil;

/**
 * Progress icon
 */
public class ProgressIcon extends SurfaceViewer {

    protected Bitmap mBitmap;
    protected Matrix mMatrix;
    protected Paint mPaint;
    protected int mRotateDegree;

    public ProgressIcon(Context context) {
        super(context);
    }

    public ProgressIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressIcon(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init(Context context, AttributeSet attrs) {
        super.init(context, attrs);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressIcon);
            this.setDrawable(typedArray.getDrawable(R.styleable.ProgressIcon_android_src));
            typedArray.recycle();
        }
        mMatrix = new Matrix();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    public void setDrawable(Drawable drawable) {
        if (drawable != null) {
            mBitmap = AppUtil.toBitmap(drawable);
        } else {
            mBitmap = null;
        }
        this.requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int iconSize = 0;
        if (mBitmap != null) {
            int w = mBitmap.getWidth();
            int h = mBitmap.getHeight();
            iconSize = (int) Math.sqrt(w * w + h * h);
        }
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(iconSize, MeasureSpec.EXACTLY);
        }
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(iconSize, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas, int width, int height) {
        if (mBitmap != null) {
            int w = mBitmap.getWidth();
            int h = mBitmap.getHeight();
            int l = (width - w) / 2;
            int t = (height - h) / 2;
            mMatrix.reset();
            mMatrix.postRotate(mRotateDegree, w / 2, h / 2);
            mMatrix.postTranslate(l, t);
            canvas.drawBitmap(mBitmap, mMatrix, mPaint);
        }
        mRotateDegree += 12;
        mRotateDegree %= 360;
    }

}

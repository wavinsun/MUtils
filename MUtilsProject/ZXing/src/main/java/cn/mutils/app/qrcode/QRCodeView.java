package cn.mutils.app.qrcode;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;

import cn.mutils.app.zxing.R;

public class QRCodeView extends View {

    protected Bitmap mLogoBitmap;

    protected Bitmap mContentBitmap;

    protected Bitmap mContentBgBitmap;

    protected String mText;

    protected Matrix mMatrix = new Matrix();

    protected int mContentPadding = 5;

    public QRCodeView(Context context) {
        super(context);
        init(context, null);
    }

    public QRCodeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public QRCodeView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.QRCodeView);
            setText(typedArray.getString(R.styleable.QRCodeView_android_text));
            typedArray.recycle();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.mContentBitmap == null) {
            return;
        }
        int width = this.getWidth();
        int height = this.getHeight();
        double backgroundSize = width < height ? width : height;
        double backgroundX = (width - backgroundSize) * 0.5;
        double backgroundY = (height - backgroundSize) * 0.5;
        if (this.mContentBgBitmap != null) {
            double backgroundScale = backgroundSize / this.mContentBgBitmap.getWidth();
            this.mMatrix.reset();
            this.mMatrix.postScale((float) backgroundScale, (float) backgroundScale);
            this.mMatrix.postTranslate((float) backgroundX, (float) backgroundY);
            canvas.drawBitmap(this.mContentBgBitmap, this.mMatrix, null);
        }
        double codeSize = backgroundSize - 2 * this.mContentPadding;
        double codeScale = codeSize / this.mContentBitmap.getWidth();
        double codeX = (width - codeSize) * 0.5;
        double codeY = (height - codeSize) * 0.5;
        this.mMatrix.reset();
        this.mMatrix.postScale((float) codeScale, (float) codeScale);
        this.mMatrix.postTranslate((float) codeX, (float) codeY);
        canvas.drawBitmap(this.mContentBitmap, this.mMatrix, null);

        if (this.mLogoBitmap == null) {
            return;
        }
        double logoSize = codeSize * 0.19;
        double logoScale = logoSize / this.mLogoBitmap.getWidth();
        double logoX = (width - logoSize) * 0.5;
        double logoY = (height - logoSize) * 0.5;
        this.mMatrix.reset();
        this.mMatrix.postScale((float) logoScale, (float) logoScale);
        this.mMatrix.postTranslate((float) logoX, (float) logoY);
        canvas.drawBitmap(this.mLogoBitmap, this.mMatrix, null);
    }

    public String getText() {
        return this.mText;
    }

    public void setText(String text) {
        if (text == null) {
            mText = null;
            this.mContentBitmap = null;
            this.invalidate();
            return;
        }
        if (text.equals(mText)) {
            return;
        }
        mText = text;
        this.mContentBitmap = QRCodeUtil.convert(text);
        this.invalidate();
    }

    public void setLogoRes(int logoResId) {
        if (logoResId == 0) {
            this.mLogoBitmap = null;
            this.invalidate();
            return;
        }
        this.mLogoBitmap = BitmapFactory.decodeResource(this.getResources(), logoResId);
        this.invalidate();
    }

    public void setContentBgRes(int contentBgRes) {
        if (contentBgRes == 0) {
            this.mContentBgBitmap = null;
            this.invalidate();
            return;
        }
        this.mContentBgBitmap = BitmapFactory.decodeResource(this.getResources(), contentBgRes);
        this.invalidate();
    }

    public void setContentPadding(int contentPadding) {
        if (contentPadding < 0) {
            return;
        }
        this.mContentPadding = contentPadding;
        this.invalidate();
    }

}

package cn.mutils.app.ui.pattern;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import cn.mutils.app.util.AppUtil;

public class PatternIcon extends View {

    private String mPassword;
    private Point[] mPoints = new Point[9];
    private int mRadius;

    private Paint mStrokePaint;
    private Paint mFillPaint;

    public PatternIcon(Context context) {
        super(context);
        init();
    }

    public PatternIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mStrokePaint = new Paint();
        mStrokePaint.setAntiAlias(true);
        mStrokePaint.setColor(0xffc0c0c0);
        mStrokePaint.setStyle(Paint.Style.STROKE);
        mStrokePaint.setStrokeWidth(AppUtil.getRawSize(getContext(),
                TypedValue.COMPLEX_UNIT_DIP, 1));

        mFillPaint = new Paint();
        mFillPaint.setAntiAlias(true);
        mFillPaint.setColor(0xff000000 | 0xffff5f);
        mFillPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mFillPaint.setStrokeWidth(AppUtil.getRawSize(getContext(),
                TypedValue.COMPLEX_UNIT_DIP, 1));

        for (int i = 0; i < mPoints.length; i++)
            mPoints[i] = new Point();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,
                            int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        int w = right - left;
        int h = bottom - top;
        int cw = Math.min(w, h);

        int b = (int) (cw / 2.0);
        int a = (int) (b / 2.0);
        int c = a + b;

        mPoints[0].set(a, a);
        mPoints[1].set(a, b);
        mPoints[2].set(a, c);

        mPoints[3].set(b, a);
        mPoints[4].set(b, b);
        mPoints[5].set(b, c);

        mPoints[6].set(c, a);
        mPoints[7].set(c, b);
        mPoints[8].set(c, c);

        mRadius = (int) (a / 3.0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int i, n;

        for (i = 0; i < mPoints.length; i++)
            canvas.drawCircle(mPoints[i].x, mPoints[i].y, mRadius, mStrokePaint);

        if (mPassword != null) {
            for (i = 0; i < mPassword.length(); i++) {
                n = Integer.parseInt(String.valueOf(mPassword.charAt(i)));
                canvas.drawCircle(mPoints[n].x, mPoints[n].y, mRadius,
                        mFillPaint);
            }
        }
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
        invalidate();
    }

}
package cn.mutils.app.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import cn.mutils.app.R;
import cn.mutils.app.event.listener.OnSelectedChangeListener;
import cn.mutils.app.util.AppUtil;

@SuppressWarnings("unused")
public class IndexView extends View {

    protected int mSize = 0;
    protected int mSelectedIndex = -1;

    protected OnSelectedChangeListener mOnSelectedChangeListener;

    protected Paint mPaint = new Paint();
    protected int mItemBgColor = 0xFFB2B2B2;
    protected int mItemSelectedColor = 0xFF8EC43E;
    protected int mItemWidth = 8;

    public IndexView(Context context) {
        super(context);
        init(context, null);
    }

    public IndexView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public IndexView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        mPaint.setAntiAlias(true);
        mItemWidth = (int) AppUtil.dp2px(context, 8);
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.IndexView);
            mItemBgColor = typedArray.getColor(R.styleable.IndexView_bgColor, mItemBgColor);
            mItemSelectedColor = typedArray.getColor(R.styleable.IndexView_strokeColor, mItemSelectedColor);
            mItemWidth = typedArray.getDimensionPixelSize(R.styleable.IndexView_strokeWidth, mItemWidth);
            typedArray.recycle();
        }
    }

    public int getSelectedIndex() {
        return mSelectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        if (selectedIndex < 0 || selectedIndex >= mSize) {
            mSelectedIndex = -1;
            this.invalidate();
            return;
        }
        if (mSelectedIndex == selectedIndex) {
            return;
        }
        if (mOnSelectedChangeListener != null) {
            if (mOnSelectedChangeListener.onInterceptChange(this, selectedIndex)) {
                return;
            }
        }
        mSelectedIndex = selectedIndex;
        this.invalidate();
        if (mOnSelectedChangeListener != null) {
            mOnSelectedChangeListener.onChanged(this, mSelectedIndex);
        }
    }

    public int getSize() {
        return mSize;
    }

    public void setSize(int size) {
        if (mSize == size) {
            return;
        }
        mSize = size;
        setSelectedIndex(mSize > 0 ? 0 : -1);
        this.invalidate();
    }

    public void setOnSelectedChangedListener(OnSelectedChangeListener listener) {
        mOnSelectedChangeListener = listener;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (MeasureSpec.getMode(widthMeasureSpec) == MeasureSpec.AT_MOST) {
            int width = getPaddingTop() + getPaddingBottom();
            if (mSize > 0) {
                width += (mSize + mSize + 1) * mItemWidth;
            }
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        }
        if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
            int height = getPaddingTop() + getPaddingBottom() + mItemWidth;
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int w = this.getWidth();
        int h = this.getHeight();
        int itemsWidth = mSize > 0 ? ((mSize + mSize + 1) * mItemWidth) : 0;
        int l = this.getPaddingLeft();
        int r = w - this.getPaddingRight();
        int x = l + (r - l - itemsWidth) / 2;
        int y = h / 2;
        int radius = mItemWidth / 2;
        for (int i = 0; i < mSize; i++) {
            mPaint.setColor(i == mSelectedIndex ? mItemSelectedColor : mItemBgColor);
            x += mItemWidth;// Gap size
            canvas.drawCircle(x + radius, y, radius, mPaint);
            x += mItemWidth;// Item size
        }
    }

}

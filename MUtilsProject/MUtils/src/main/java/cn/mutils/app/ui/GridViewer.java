package cn.mutils.app.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

@SuppressWarnings("unused")
public class GridViewer extends GridView {

    /**
     * 设置是否高度全部显示
     */
    protected boolean mShowAll = false;

    public GridViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewer(Context context) {
        super(context);
    }

    public GridViewer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public boolean isShowAll() {
        return mShowAll;
    }

    public void setShowAll(boolean showAll) {
        if (mShowAll == showAll) {
            return;
        }
        mShowAll = showAll;
        this.requestLayout();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mShowAll) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}

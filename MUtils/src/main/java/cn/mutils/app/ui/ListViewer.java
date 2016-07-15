package cn.mutils.app.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

@SuppressWarnings("unused")
public class ListViewer extends ListView {

    /**
     * 设置是否高度全部显示
     */
    protected boolean mShowAll;

    public ListViewer(Context context) {
        super(context);
    }

    public ListViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ListViewer(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
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
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mShowAll) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}

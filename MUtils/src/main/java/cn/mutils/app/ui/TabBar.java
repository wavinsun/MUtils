package cn.mutils.app.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import cn.mutils.app.event.listener.OnSelectedChangeListener;

@SuppressWarnings({"unused", "UnusedAssignment"})
@SuppressLint("MissingSuperCall")
public class TabBar extends LinearLayout implements View.OnClickListener {

    /**
     * Handler for fluent experience
     */
    protected Handler mSelectedDispatcher;
    protected OnSelectedChangeListener mOnSelectedChangeListener;

    protected int mSelectedIndex = -1;
    protected int mSelectedIndexTryAgain = -1;

    public TabBar(Context context) {
        super(context);
    }

    public TabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        for (int i = 0, size = this.getChildCount(); i < size; i++) {
            View item = this.getChildAt(i);
            if (item instanceof TabItem) {
                TabItem tabItem = ((TabItem) item);
                if (tabItem.getIndex() == -1) {
                    tabItem.setIndex(i);
                }
            }
            item.setOnClickListener(this);
            if (i == 0) {
                item.setSelected(true);
                mSelectedIndex = 0;
            }
        }
    }

    public void setEnabled(boolean enabled, int index) {
        if (index < this.getChildCount()) {
            this.getChildAt(index).setEnabled(enabled);
        }
    }

    public void tryAgain() {
        if (mSelectedIndexTryAgain == -1) {
            return;
        }
        if (mOnSelectedChangeListener != null) {
            if (mOnSelectedChangeListener.onInterceptTryAgain(this, mSelectedIndexTryAgain)) {
                mSelectedIndexTryAgain = -1;
                return;
            }
        }
        this.setSelectedIndex(mSelectedIndexTryAgain);
        mSelectedIndexTryAgain = -1;
    }

    public int getSelectedIndex() {
        return mSelectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        if (selectedIndex < 0 || selectedIndex >= this.getChildCount()) {
            if (mSelectedIndex != -1) {
                this.getChildAt(mSelectedIndex).setSelected(false);
            }
            mSelectedIndex = -1;
            return;
        }
        if (mSelectedIndex == selectedIndex) {
            return;
        }
        if (mOnSelectedChangeListener != null) {
            if (mOnSelectedChangeListener.onInterceptChange(this, selectedIndex)) {
                mSelectedIndexTryAgain = selectedIndex;
                return;
            }
        }
        if (mSelectedIndex >= 0) {
            this.getChildAt(mSelectedIndex).setSelected(false);
        }
        mSelectedIndex = selectedIndex;
        mSelectedIndexTryAgain = -1;
        this.getChildAt(mSelectedIndex).setSelected(true);
        if (mOnSelectedChangeListener != null) {
            if (mSelectedDispatcher == null) {
                mSelectedDispatcher = new Handler(Looper.getMainLooper());
            } else {
                mSelectedDispatcher.removeCallbacksAndMessages(null);
            }
            mSelectedDispatcher.post(new SelectedRunnable());
        }
    }

    public void setOnSelectedChangeListener(OnSelectedChangeListener listener) {
        mOnSelectedChangeListener = listener;
    }

    @Override
    public void onClick(View v) {
        int index = -1;
        if (v instanceof TabItem) {
            index = ((TabItem) v).getIndex();
            if (index < 0) {
                index = this.indexOfChild(v);
            }
        } else {
            index = this.indexOfChild(v);
        }
        this.setSelectedIndex(index);
    }

    class SelectedRunnable implements Runnable {

        @Override
        public void run() {
            if (mOnSelectedChangeListener != null) {
                mOnSelectedChangeListener.onChanged(TabBar.this, mSelectedIndex);
            }
        }

    }

}

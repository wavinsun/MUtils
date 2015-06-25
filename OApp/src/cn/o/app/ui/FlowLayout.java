package cn.o.app.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import cn.o.app.R;
import cn.o.app.event.Listener;

public class FlowLayout extends ViewGroup {
	public static interface OnLinesChangeListener extends Listener {
		public void onChanged(FlowLayout v, int lines);
	}

	protected int mHorizontalGap;
	protected int mVerticalGap;

	protected int mMaxLines = -1;
	protected int mMeasuredLines;

	protected OnLinesChangeListener mOnLinesChangeListener;

	public FlowLayout(Context context) {
		super(context);
		init(context, null);
	}

	public FlowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public FlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	protected void init(Context context, AttributeSet attrs) {
		if (attrs != null) {
			TypedArray typedArray = context.obtainStyledAttributes(attrs,
					R.styleable.FlowLayout);
			mHorizontalGap = typedArray.getDimensionPixelSize(
					R.styleable.FlowLayout_android_horizontalGap, 10);
			mVerticalGap = typedArray.getDimensionPixelSize(
					R.styleable.FlowLayout_android_verticalGap, 10);
			mMaxLines = typedArray.getInt(
					R.styleable.FlowLayout_android_maxLines, -1);
			typedArray.recycle();
		}
	}

	public void setOnLinesChangeListener(OnLinesChangeListener listener) {
		mOnLinesChangeListener = listener;
	}

	public int getMaxLines() {
		return mMaxLines;
	}

	public void setMaxLines(int maxLines) {
		if (mMaxLines == maxLines) {
			return;
		}
		mMaxLines = maxLines;
		this.requestLayout();
		this.invalidate();
	}

	public int getMeasuredLines() {
		return mMeasuredLines;
	}

	public void setMeasuredLines(int measuredLines) {
		mMeasuredLines = measuredLines;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int w = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()
				- getPaddingRight();
		int h = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop()
				- getPaddingBottom();
		int childHeightMeasureSpec;
		if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
			childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(h,
					MeasureSpec.AT_MOST);
		} else {
			childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(0,
					MeasureSpec.UNSPECIFIED);
		}
		int x = getPaddingLeft();
		int y = getPaddingTop();
		int rowHeight = 0;
		int maxHeight = 0;
		int lines = 1;
		boolean linesInvalidate = false;
		for (int i = 0, size = this.getChildCount(); i < size; i++) {
			View child = getChildAt(i);
			if (child.getVisibility() == View.GONE) {
				continue;
			}
			if (linesInvalidate) {
				linesInvalidate = false;
				rowHeight = 0;
			}
			child.measure(MeasureSpec.makeMeasureSpec(w, MeasureSpec.AT_MOST),
					childHeightMeasureSpec);
			rowHeight = Math.max(rowHeight, child.getMeasuredHeight()
					+ mVerticalGap);
			if (x + child.getMeasuredWidth() > w) {
				if (x > getPaddingLeft()) {
					x = getPaddingLeft();
					y += rowHeight;
					if (lines == mMaxLines) {
						maxHeight = y;
					}
					lines++;
					linesInvalidate = true;
				}
			}
			LayoutParams lp = (LayoutParams) child.getLayoutParams();
			lp.x = x;
			lp.y = y;
			x += child.getMeasuredWidth() + mHorizontalGap;
		}
		if (maxHeight != 0) {// 固定高度
			h = maxHeight;
		} else {
			if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.UNSPECIFIED) {
				h = y + rowHeight;
			} else if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
				if (y + rowHeight < h) {
					h = y + rowHeight;
				}
			}
		}
		setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), h
				+ getPaddingBottom());
		if (mMeasuredLines != lines) {
			mMeasuredLines = lines;
			if (mOnLinesChangeListener != null) {
				mOnLinesChangeListener.onChanged(this, lines);
			}
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		for (int i = this.getChildCount() - 1; i >= 0; i--) {
			View child = this.getChildAt(i);
			if (child.getVisibility() == View.GONE) {
				continue;
			}
			LayoutParams lp = (LayoutParams) child.getLayoutParams();
			child.layout(lp.x, lp.y, lp.x + child.getMeasuredWidth(), lp.y
					+ child.getMeasuredHeight());
		}
	}

	@Override
	protected boolean checkLayoutParams(android.view.ViewGroup.LayoutParams p) {
		return p != null && (p instanceof LayoutParams);
	}

	@Override
	protected LayoutParams generateDefaultLayoutParams() {
		return new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
	}

	@Override
	public LayoutParams generateLayoutParams(AttributeSet attrs) {
		return new LayoutParams(getContext(), attrs);
	}

	@Override
	protected LayoutParams generateLayoutParams(
			android.view.ViewGroup.LayoutParams p) {
		return new LayoutParams(p.width, p.height);
	}

	public static class LayoutParams extends ViewGroup.LayoutParams {

		int x;
		int y;

		public LayoutParams(Context c, AttributeSet attrs) {
			super(c, attrs);
		}

		public LayoutParams(int width, int height) {
			super(width, height);
		}

		public LayoutParams(LayoutParams source) {
			super(source);
		}
	}

}

package cn.o.app.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import cn.o.app.ui.adapter.PopMenuAdapter;

@SuppressWarnings("deprecation")
public class PopMenu<DATA_ITEM> {

	protected View mAnchor;

	protected PopupWindow mPopupWindow;

	protected PopMenuAdapter<DATA_ITEM> mAdapter;

	protected PopMenuListener mListener;

	protected int mBackgroundRes;

	protected int mPopupX;

	protected int mPopupY;

	public PopMenu(View anchor) {
		Context context = anchor.getContext();
		if (!(context instanceof Activity)) {
			throw new IllegalArgumentException("Context is not Activity");
		}
		int[] anchorLocation = new int[2];
		mAnchor = anchor;
		mAnchor.getLocationOnScreen(anchorLocation);
		mPopupX = anchorLocation[0];
		mPopupY = anchorLocation[1];
	}

	public void setAdapter(PopMenuAdapter<DATA_ITEM> adapter) {
		if (mPopupWindow != null) {
			return;
		}
		if (adapter.getContainer() != null) {
			throw new IllegalArgumentException("I will set container by myslef");
		}
		mAdapter = adapter;
		mAdapter.setPopMenu(this);
	}

	public void setListener(PopMenuListener listener) {
		mListener = listener;
	}

	public void dismiss() {
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
		}
	}

	public void setBackgroundRes(int backgroundRes) {
		if (mPopupWindow != null) {
			return;
		}
		mBackgroundRes = backgroundRes;
	}

	public void show() {
		if (mPopupWindow != null) {
			return;
		}
		Context context = mAnchor.getContext();
		Resources resources = context.getResources();
		DisplayMetrics metrics = resources.getDisplayMetrics();
		int screenHeight = metrics.heightPixels;
		int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, metrics);
		Rect popupArea = new Rect();
		((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(popupArea);
		boolean statusBarAtTop = popupArea.top > 1;
		int popupAreaHeight = popupArea.height();
		int statusBarHeight = screenHeight - popupAreaHeight;
		int anchorHeight = mAnchor.getHeight();
		int halfPopupAreaHeight = popupAreaHeight >> 1;
		boolean underAnchor = (mPopupY - popupArea.top + (anchorHeight >> 1)) < halfPopupAreaHeight;
		mPopupY = underAnchor ? (mPopupY + anchorHeight + padding) : (mPopupY - padding);
		int contentMaxHeight = underAnchor ? (screenHeight - mPopupY - padding - (statusBarAtTop ? 0 : statusBarHeight))
				: (mPopupY - padding - (statusBarAtTop ? statusBarHeight : 0));
		FrameLayout realContentView = new FrameLayout(context);
		realContentView.setPadding(padding, 0, padding, 0);
		OPopupMenuScroller scroller = new OPopupMenuScroller(context);
		scroller.setVerticalScrollBarEnabled(false);
		if (mBackgroundRes != 0) {
			scroller.setBackgroundResource(mBackgroundRes);
		} else {
			GradientDrawable scrollerDrawable = new GradientDrawable();
			scrollerDrawable.setColor(0xFFF5F5F5);
			scrollerDrawable.setStroke(1, 0xD9D9D9D9);
			scroller.setBackgroundDrawable(scrollerDrawable);
		}
		scroller.setMaxHeight(contentMaxHeight);
		if (!underAnchor) {
			scroller.setOnMeasuredListener(new OnMeasuredListener() {

				@Override
				public void onMeasured(View v, int width, int height) {
					((OPopupMenuScroller) v).setOnMeasuredListener(null);
					mPopupY = mPopupY - height;
					mPopupWindow.update(mPopupX, mPopupY, WindowManager.LayoutParams.WRAP_CONTENT,
							WindowManager.LayoutParams.WRAP_CONTENT);
				}
			});
		}
		LinearLayout scrollContent = new LinearLayout(context);
		scrollContent.setOrientation(LinearLayout.VERTICAL);
		if (mAdapter != null) {
			mAdapter.setContainer(scrollContent);
		}
		scroller.addView(scrollContent, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT));
		realContentView.addView(scroller, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
				FrameLayout.LayoutParams.WRAP_CONTENT));

		mPopupWindow = new PopupWindow(realContentView, WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT);
		mPopupWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
		mPopupWindow.setOutsideTouchable(true);
		mPopupWindow.setFocusable(true);
		mPopupWindow.showAtLocation(mAnchor, Gravity.NO_GRAVITY, mPopupX, mPopupY);
		mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

			@Override
			public void onDismiss() {
				if (mListener != null) {
					mListener.onDismiss();
				}
				mPopupWindow = null;
			}
		});
	}

	public static interface PopMenuListener {

		public void onDismiss();

	}

	protected static interface OnMeasuredListener {

		public void onMeasured(View v, int width, int height);

	}

	protected static class OPopupMenuScroller extends ScrollView {

		protected int mMaxHeight;

		protected int mMaxWidth;

		protected OnMeasuredListener mOnMeasuredListener;

		public OPopupMenuScroller(Context context) {
			super(context);
		}

		public OPopupMenuScroller(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		public OPopupMenuScroller(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			int measuredWidth = this.getMeasuredWidth();
			int measuredHeight = this.getMeasuredHeight();
			boolean measuredChanged = false;
			if (mMaxWidth > 0 && measuredWidth > mMaxWidth) {
				measuredWidth = mMaxWidth;
				measuredChanged = true;
			}
			if (mMaxHeight > 0 && measuredHeight > mMaxHeight) {
				measuredHeight = mMaxHeight;
				measuredChanged = true;
			}
			if (measuredChanged) {
				this.setMeasuredDimension(measuredWidth, measuredHeight);
			}
			if (mOnMeasuredListener != null) {
				mOnMeasuredListener.onMeasured(this, measuredWidth, measuredHeight);
			}
		}

		public int getMaxHeight() {
			return mMaxHeight;
		}

		public void setMaxHeight(int maxHeight) {
			this.mMaxHeight = maxHeight;
		}

		public int getMaxWidth() {
			return mMaxWidth;
		}

		public void setMaxWidth(int maxWidth) {
			this.mMaxWidth = maxWidth;
		}

		public void setOnMeasuredListener(OnMeasuredListener listener) {
			mOnMeasuredListener = listener;
		}

	}

}

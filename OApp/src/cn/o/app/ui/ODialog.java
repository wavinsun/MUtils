package cn.o.app.ui;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import cn.o.app.OWrapper;
import cn.o.app.context.IContextProvider;
import cn.o.app.ui.core.IContentViewOwner;
import cn.o.app.ui.core.IViewFinder;

@SuppressLint("RtlHardcoded")
@SuppressWarnings("deprecation")
public class ODialog extends Dialog implements IViewFinder, IContentViewOwner,
		IContextProvider {

	public ODialog(Context context) {
		super(context);
		init();
	}

	public ODialog(Context context, int theme) {
		super(context, theme);
		init();
	}

	public ODialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init();
	}

	protected void init() {
		this.setCancelable(true);
		this.setCanceledOnTouchOutside(true);
		Window w = getWindow();
		w.requestFeature(Window.FEATURE_NO_TITLE);
		w.setBackgroundDrawable(new ColorDrawable(0x00000000));

		OWrapper.injectContentView(this);
	}

	// 去除黑色模糊背景
	public void clearBehind() {
		getWindow().clearFlags(
				WindowManager.LayoutParams.FLAG_BLUR_BEHIND
						| WindowManager.LayoutParams.FLAG_DIM_BEHIND);
	}

	/**
	 * fix bug of default Dialog width not full screen. please call before show
	 * and after setContentView if you want to make width MATCH_PARENT
	 */
	public void requestHFill() {
		Window w = getWindow();
		WindowManager.LayoutParams attrs = w.getAttributes();
		w.setLayout(WindowManager.LayoutParams.MATCH_PARENT, attrs.height);
	}

	public void setWindowAnimations(int resId) {
		Window w = getWindow();
		w.setWindowAnimations(resId);
	}

	public <T extends View> T findViewById(int id, Class<T> viewClass) {
		return OWrapper.findViewById(this, id, viewClass);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		OWrapper.injectResources(this);
		OWrapper.injectEvents(this);
	}

	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		OWrapper.injectResources(this);
		OWrapper.injectEvents(this);
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
		OWrapper.injectResources(this);
		OWrapper.injectEvents(this);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		OWrapper.dispatchTouchEvent(ev, this);
		return super.dispatchTouchEvent(ev);
	}

	public View getContentView() {
		return OWrapper.getContentView(this);
	}

	// 模拟PopupWindow
	public void showAsDropDown(View anchor) {
		clearBehind();
		int[] location = new int[2];
		anchor.getLocationOnScreen(location);
		Rect dialogableRect = new Rect();
		anchor.getWindowVisibleDisplayFrame(dialogableRect);
		int screenHeight = anchor.getResources().getDisplayMetrics().heightPixels;
		int maxStatusHeight = screenHeight / 10;// 假设最大状态栏高度
		boolean statusAtTop = dialogableRect.top > 1;
		int statusHeight = statusAtTop ? dialogableRect.top
				: (screenHeight - dialogableRect.bottom);
		statusHeight = statusHeight < maxStatusHeight ? statusHeight : 0;
		if (statusHeight + dialogableRect.height() != screenHeight) {// 键盘弹出
			dialogableRect.bottom = statusAtTop ? screenHeight
					: (screenHeight - statusHeight);
		}
		Window w = getWindow();
		WindowManager.LayoutParams attrs = w.getAttributes();
		attrs.y = location[1] + anchor.getHeight() - dialogableRect.top;
		attrs.gravity = Gravity.LEFT | Gravity.TOP;
		attrs.width = WindowManager.LayoutParams.MATCH_PARENT;
		int maxHeight = dialogableRect.height() - attrs.y;
		DropDownFrame frame = new DropDownFrame(getContext());
		frame.setMaxHeight(maxHeight);
		ViewGroup rootView = (ViewGroup) w.findViewById(android.R.id.content);
		if (rootView.getChildCount() != 0) {
			View v = rootView.getChildAt(0);
			rootView.removeViewAt(0);
			frame.addView(v);
		}
		frame.setLayoutParams(new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		w.setContentView(frame);
		show();
	}

	protected static class DropDownFrame extends FrameLayout {

		protected int mMaxHeight;

		public DropDownFrame(Context context) {
			super(context);
		}

		public DropDownFrame(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		public DropDownFrame(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
		}

		public void setMaxHeight(int maxHeight) {
			mMaxHeight = maxHeight;
			this.requestLayout();
		}

		@Override
		protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
			super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			int measuredHeight = this.getMeasuredHeight();
			if (mMaxHeight > 0 && mMaxHeight < measuredHeight) {
				super.onMeasure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(),
						MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(
						mMaxHeight, MeasureSpec.EXACTLY));// 通知子View
				this.setMeasuredDimension(getMeasuredWidth(), mMaxHeight);

			}
		}

	}
}

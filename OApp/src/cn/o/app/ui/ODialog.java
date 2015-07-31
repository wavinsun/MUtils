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
import android.widget.Toast;
import cn.o.app.context.IContextProvider;
import cn.o.app.ui.core.IContentViewOwner;
import cn.o.app.ui.core.IToastOwner;
import cn.o.app.ui.core.IViewFinder;
import cn.o.app.ui.core.UICore;

/**
 * Dialog of framework
 */
@SuppressLint({ "RtlHardcoded", "ShowToast" })
@SuppressWarnings("deprecation")
public class ODialog extends Dialog implements IViewFinder, IContentViewOwner, IContextProvider, IToastOwner {

	protected InfoToast mInfoToast;

	protected Toast mToast;

	public ODialog(Context context) {
		super(context);
		init();
	}

	public ODialog(Context context, int theme) {
		super(context, theme);
		init();
	}

	public ODialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
		init();
	}

	protected void init() {
		this.setCancelable(true);
		this.setCanceledOnTouchOutside(true);
		Window w = getWindow();
		w.requestFeature(Window.FEATURE_NO_TITLE);
		w.setBackgroundDrawable(new ColorDrawable(0x00000000));

		UICore.injectContentView(this);
	}

	/**
	 * clear black blur background of Dialog
	 */
	public void clearBehind() {
		getWindow()
				.clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND | WindowManager.LayoutParams.FLAG_DIM_BEHIND);
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
		return UICore.findViewById(this, id, viewClass);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		UICore.injectResources(this);
		UICore.injectEvents(this);
	}

	@Override
	public void setContentView(View view) {
		super.setContentView(view);
		UICore.injectResources(this);
		UICore.injectEvents(this);
	}

	@Override
	public void setContentView(View view, LayoutParams params) {
		super.setContentView(view, params);
		UICore.injectResources(this);
		UICore.injectEvents(this);
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		UICore.dispatchTouchEvent(ev, this);
		return super.dispatchTouchEvent(ev);
	}

	public View getContentView() {
		return UICore.getContentView(this);
	}

	@Override
	public InfoToast getInfoToast() {
		return mInfoToast;
	}

	@Override
	public Toast getToast() {
		Context context = this.getContext();
		if (context instanceof IToastOwner) {
			return ((IToastOwner) context).getToast();
		}
		if (mToast == null) {
			mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
		}
		return mToast;
	}

	@Override
	public IToastOwner getToastOwner() {
		return this;
	}

	@Override
	public void toast(CharSequence s) {
		UICore.toast(this, s);
	}

	@Override
	public void toast(int resId, Object... args) {
		UICore.toast(this, resId, args);
	}

	@Override
	public void show() {
		try {
			super.show();
		} catch (Exception e) {
			// android.view.WindowManager.BadTokenException
		}
	}

	/**
	 * Simulate PopupWindow
	 * 
	 * @param anchor
	 */
	public void showAsDropDown(View anchor) {
		clearBehind();
		int[] location = new int[2];
		anchor.getLocationOnScreen(location);
		Rect dialogableRect = new Rect();
		anchor.getWindowVisibleDisplayFrame(dialogableRect);
		int screenHeight = anchor.getResources().getDisplayMetrics().heightPixels;
		int maxStatusHeight = screenHeight / 10;// suppose max status bar height
		boolean statusAtTop = dialogableRect.top > 1;
		int statusHeight = statusAtTop ? dialogableRect.top : (screenHeight - dialogableRect.bottom);
		statusHeight = statusHeight < maxStatusHeight ? statusHeight : 0;
		// keyboard opening
		if (statusHeight + dialogableRect.height() != screenHeight) {
			dialogableRect.bottom = statusAtTop ? screenHeight : (screenHeight - statusHeight);
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
		frame.setLayoutParams(
				new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		w.setContentView(frame);
		show();
	}

	/**
	 * Drop down frame for {@link ODialog#showAsDropDown(View)} who has max
	 * height.
	 */
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
				// Notify sub views
				super.onMeasure(MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.EXACTLY),
						MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.EXACTLY));
				this.setMeasuredDimension(getMeasuredWidth(), mMaxHeight);
			}
		}

	}

}

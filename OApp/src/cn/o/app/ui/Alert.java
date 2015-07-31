package cn.o.app.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.o.app.ui.core.IViewFinder;
import cn.o.app.ui.core.UICore;

@SuppressWarnings("deprecation")
public class Alert implements IViewFinder {

	public static abstract class AlertListener {

		/**
		 * Return true to intercept
		 * 
		 * @param alert
		 * @return
		 */
		public abstract boolean onOK(Alert alert);

		/**
		 * Return true to intercept
		 * 
		 * @param alert
		 * @return
		 */
		public abstract boolean onCancel(Alert alert);

		/**
		 * Return true to intercept
		 * 
		 * @param alert
		 * @return
		 */
		public boolean onDismiss(Alert alert) {
			return false;
		}

		/**
		 * Return true to intercept
		 * 
		 * @param alert
		 * @return
		 */
		public boolean onClose(Alert alert) {
			return false;
		}
	}

	protected CharSequence mOK;

	protected CharSequence mCancel;

	protected ODialog mDialog;

	protected Context mContext;

	protected boolean mCancelable = true;

	protected CharSequence mTitle;

	protected CharSequence mMessage;

	protected AlertListener mListener;

	protected View mContentView;

	protected boolean mActionBarVisible = true;

	protected int mBackgroundColor = 0xFFEDEDED;

	protected int mLeftMargin;

	protected int mRightMargin;

	protected int mTopMargin;

	protected int mBottomMargin;

	protected LayoutInflater mLayoutInflater;

	protected View mTitleBar;

	protected int mTitleId = View.NO_ID;

	protected int mCloseId = View.NO_ID;

	protected View mActionBar;

	protected int mOKId = View.NO_ID;

	protected int mCancelId = View.NO_ID;

	protected boolean mOKEnabled = true;

	protected View mOKView;

	protected int mMessageGravity = Gravity.CENTER;

	public Alert(Context context) {
		mContext = context;
	}

	public boolean isOKEnabled() {
		return mOKEnabled;
	}

	public void setOKEnabled(boolean enabled) {
		if (mOKEnabled == enabled) {
			return;
		}
		mOKEnabled = enabled;
		if (mOKView != null) {
			mOKView.setEnabled(enabled);
		}
	}

	public LayoutInflater getLayoutInflater() {
		if (mLayoutInflater == null) {
			mLayoutInflater = LayoutInflater.from(getContext());
		}
		return mLayoutInflater;
	}

	public Resources getResources() {
		return mContext.getResources();
	}

	public void setMargin(int left, int top, int right, int bottom) {
		if (mDialog != null) {
			return;
		}
		mLeftMargin = left;
		mTopMargin = top;
		mRightMargin = right;
		mBottomMargin = bottom;
	}

	public void setBackgroundColor(int color) {
		if (mDialog != null) {
			return;
		}
		mBackgroundColor = color;
	}

	public Context getContext() {
		return mContext;
	}

	public void setTitleBar(int titleBarLayoutId) {
		if (mDialog != null) {
			return;
		}
		mTitleBar = titleBarLayoutId == 0 ? null : getLayoutInflater().inflate(titleBarLayoutId, null);
	}

	public void setTitleBar(View titleBar) {
		if (mDialog != null) {
			return;
		}
		mTitleBar = titleBar;
	}

	public void setActionBar(int actionBarLayoutId) {
		if (mDialog != null) {
			return;
		}
		mActionBar = actionBarLayoutId == 0 ? null : getLayoutInflater().inflate(actionBarLayoutId, null);
	}

	public void setActionBar(View actionBar) {
		if (mDialog != null) {
			return;
		}
		mActionBar = actionBar;
	}

	public void setTitleId(int titleId) {
		if (mDialog != null) {
			return;
		}
		mTitleId = titleId;
	}

	public void setCloseId(int closeId) {
		if (mDialog != null) {
			return;
		}
		mCloseId = closeId;
	}

	public void setOKId(int OKId) {
		if (mDialog != null) {
			return;
		}
		mOKId = OKId;
	}

	public void setCancelId(int cancelId) {
		if (mDialog != null) {
			return;
		}
		mCancelId = cancelId;
	}

	public boolean isActionBarVisible() {
		return mActionBarVisible;
	}

	public void setActionBarVisible(boolean actionBarVisible) {
		if (mDialog != null) {
			return;
		}
		mActionBarVisible = actionBarVisible;
	}

	public void setOK(CharSequence OK) {
		if (mDialog != null) {
			return;
		}
		mOK = OK;
	}

	public void setOK(int OKStringId) {
		if (mDialog != null) {
			return;
		}
		if (OKStringId == 0) {
			mOK = null;
		} else {
			mOK = mContext.getText(OKStringId);
		}
	}

	public void setCancel(CharSequence cancel) {
		if (mDialog != null) {
			return;
		}
		mCancel = cancel;
	}

	public void setCancel(int cancelStringId) {
		if (mDialog != null) {
			return;
		}
		if (cancelStringId == 0) {
			mCancel = null;
		} else {
			mCancel = mContext.getText(cancelStringId);
		}
	}

	public void setTitle(CharSequence title) {
		if (mDialog != null) {
			return;
		}
		mTitle = title;
	}

	public void setTitle(int titleStringId) {
		if (mDialog != null) {
			return;
		}
		if (titleStringId == 0) {
			mTitle = null;
		} else {
			mTitle = mContext.getText(titleStringId);
		}
	}

	public void setMessage(CharSequence message) {
		if (mDialog != null) {
			return;
		}
		mMessage = message;
	}

	public void setMessage(int messageStringId) {
		if (mDialog != null) {
			return;
		}
		if (messageStringId == 0) {
			mMessage = null;
		} else {
			mMessage = mContext.getText(messageStringId);
		}
	}

	public void setMessageGravity(int gravity) {
		if (mDialog != null) {
			return;
		}
		mMessageGravity = gravity;
	}

	public <T extends View> T findViewById(int id, Class<T> viewClass) {
		if (mContentView == null) {
			return null;
		}
		return UICore.findViewById(mContentView, id, viewClass);
	}

	public View findViewById(int id) {
		if (mContentView == null) {
			return null;
		}
		return mContentView.findViewById(id);
	}

	public View getContentView() {
		return mContentView;
	}

	public void setContentView(View contentView) {
		if (mDialog != null) {
			return;
		}
		mContentView = contentView;
	}

	public void setContentView(int contentViewLayoutId) {
		if (mDialog != null) {
			return;
		}
		if (contentViewLayoutId == 0) {
			mContentView = null;
		} else {
			mContentView = getLayoutInflater().inflate(contentViewLayoutId, null);
		}
	}

	public void setCancelable(boolean cancelable) {
		if (mDialog != null) {
			return;
		}
		mCancelable = cancelable;
	}

	public void setListener(AlertListener listener) {
		mListener = listener;
	}

	public void close() {
		if (mDialog == null) {
			return;
		}
		if (mListener != null) {
			if (mListener.onClose(this)) {
				return;
			}
		}
		dismiss();
	}

	public void ok() {
		if (mDialog == null) {
			return;
		}
		if (mListener != null) {
			if (mListener.onOK(this)) {
				return;
			}
		}
		dismiss();
	}

	public void cancel() {
		if (mDialog == null) {
			return;
		}
		if (mListener != null) {
			if (mListener.onCancel(this)) {
				return;
			}
		}
		mDialog.cancel();
		mDialog = null;
	}

	public void dismiss() {
		if (mDialog == null) {
			return;
		}
		mDialog.dismiss();
		mDialog = null;
	}

	public void hide() {
		if (mDialog == null) {
			return;
		}
		mDialog.hide();
	}

	protected void preShow() {

	}

	public void show() {
		if (mDialog != null) {
			mDialog.show();
			return;
		}
		preShow();
		DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
		int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, metrics);
		int buttonPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, metrics);
		FrameLayout realContentView = new FrameLayout(mContext);
		realContentView.setPadding(padding + mLeftMargin, padding + mTopMargin, padding + mRightMargin,
				padding + mBottomMargin);
		LinearLayout alertView = new LinearLayout(mContext);
		GradientDrawable alertViewDrawable = new GradientDrawable();
		alertViewDrawable.setColor(mBackgroundColor);
		alertViewDrawable.setCornerRadius(15);
		alertView.setBackgroundDrawable(alertViewDrawable);
		alertView.setOrientation(LinearLayout.VERTICAL);
		if (mTitleBar == null) {
			if (mTitle != null) {
				alertView.setPadding(0, padding, 0, 0);
				TextView titleTextView = new TextView(mContext);
				titleTextView.setGravity(Gravity.CENTER);
				titleTextView.setPadding(padding, padding, padding, padding);
				titleTextView.setTextColor(0xFF000000);
				titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
				titleTextView.setText(mTitle);
				alertView.addView(titleTextView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT));
			}
		} else {
			if (mTitleId != View.NO_ID && mTitle != null) {
				View titleView = mTitleBar.findViewById(mTitleId);
				if (titleView != null) {
					if (titleView instanceof TextView) {
						TextView titleTextView = (TextView) titleView;
						titleTextView.setText(mTitle);
					}
				}
			}
			if (mCloseId != View.NO_ID) {
				View closeView = mTitleBar.findViewById(mCloseId);
				if (closeView != null) {
					closeView.setOnClickListener(new CloseClickListener());
				}
			}
			if (mTitleBar.getLayoutParams() == null) {
				mTitleBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT));
			}
			alertView.addView(mTitleBar);
		}
		ScrollView scrollView = new ScrollView(mContext);
		scrollView.setVerticalScrollBarEnabled(false);
		LinearLayout scrollContent = new LinearLayout(mContext);
		scrollContent.setOrientation(LinearLayout.VERTICAL);
		if (mContentView == null) {
			if (mMessage != null) {
				scrollContent.setPadding(padding, (mTitle != null || mTitleBar != null) ? 0 : padding, padding,
						padding);
				TextView messageTextView = new TextView(mContext);
				messageTextView.setGravity(mMessageGravity);
				messageTextView.setTextColor(0xFF000000);
				messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
				messageTextView.setText(mMessage);
				scrollContent.addView(messageTextView, new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
			} else {
				scrollContent.setPadding(0, 0, 0, padding);
			}
		} else {
			if (mContentView.getLayoutParams() == null) {
				mContentView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT));
			}
			scrollContent.addView(mContentView);
		}
		scrollView.addView(scrollContent, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT));
		alertView.addView(scrollView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0, 1));
		if (mActionBarVisible) {
			if (mActionBar == null) {
				View hLine = new View(mContext);
				hLine.setBackgroundDrawable(new ColorDrawable(0xFFB3B3B3));
				alertView.addView(hLine, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 1));
				LinearLayout buttonArea = new LinearLayout(mContext);
				buttonArea.setOrientation(LinearLayout.HORIZONTAL);
				if (mCancel != null) {
					TextView cancelButton = new TextView(mContext);
					StateListDrawable cancelButtonDrawable = new StateListDrawable();
					GradientDrawable cancelButtonPressedDrawable = new GradientDrawable();
					cancelButtonPressedDrawable.setColor(0xFFD9D9D9);
					cancelButtonPressedDrawable.setCornerRadii(new float[] { 0, 0, 0, 0, 0, 0, 15, 15 });
					cancelButtonDrawable.addState(
							new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled },
							cancelButtonPressedDrawable);
					cancelButton.setBackgroundDrawable(cancelButtonDrawable);
					cancelButton.setGravity(Gravity.CENTER);
					cancelButton.setPadding(buttonPadding, buttonPadding, buttonPadding, buttonPadding);
					cancelButton.setTextColor(
							new ColorStateList(new int[][] { new int[] { android.R.attr.state_enabled }, new int[] {} },
									new int[] { 0xFF2F82C7, 0xFF999999 }));
					cancelButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
					cancelButton.setText(mCancel);
					cancelButton.setOnClickListener(new CancelClickListener());
					buttonArea.addView(cancelButton,
							new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
					View vLine = new View(mContext);
					vLine.setBackgroundDrawable(new ColorDrawable(0xFFB3B3B3));
					buttonArea.addView(vLine, new LinearLayout.LayoutParams(1, LinearLayout.LayoutParams.MATCH_PARENT));
				}
				TextView okButton = new TextView(mContext);
				StateListDrawable okButtonDrawable = new StateListDrawable();
				GradientDrawable okButtonPressedDrawable = new GradientDrawable();
				okButtonPressedDrawable.setColor(0xFFD9D9D9);
				okButtonPressedDrawable.setCornerRadii(
						new float[] { 0, 0, 0, 0, 15, 15, mCancel != null ? 0 : 15, mCancel != null ? 0 : 15 });
				okButtonDrawable.addState(new int[] { android.R.attr.state_pressed, android.R.attr.state_enabled },
						okButtonPressedDrawable);
				okButton.setBackgroundDrawable(okButtonDrawable);
				okButton.setGravity(Gravity.CENTER);
				okButton.setPadding(buttonPadding, buttonPadding, buttonPadding, buttonPadding);
				okButton.setTextColor(
						new ColorStateList(new int[][] { new int[] { android.R.attr.state_enabled }, new int[] {} },
								new int[] { 0xFF2F82C7, 0xFF999999 }));
				okButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
				if (mOK == null) {
					String country = mContext.getResources().getConfiguration().locale.getCountry();
					if (country.equals("CN")) {
						mOK = "确认";
					} else if (country.equals("TW")) {
						mOK = "確認";
					} else {
						mOK = "OK";
					}
				}
				okButton.setText(mOK);
				okButton.setOnClickListener(new OKClickListener());
				okButton.getPaint().setFakeBoldText(true);
				mOKView = okButton;
				mOKView.setEnabled(mOKEnabled);
				buttonArea.addView(okButton,
						new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1));
				alertView.addView(buttonArea, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
						LinearLayout.LayoutParams.WRAP_CONTENT));
			} else {
				if (mCancelId != View.NO_ID) {
					View cancelView = mActionBar.findViewById(mCancelId);
					if (cancelView != null) {
						if (mCancel != null) {
							if (cancelView instanceof TextView) {
								TextView cancelButton = (TextView) cancelView;
								cancelButton.setText(mCancel);
							}
							cancelView.setOnClickListener(new CancelClickListener());
						} else {
							cancelView.setVisibility(View.GONE);
						}
					}
				}
				if (mOKId != View.NO_ID) {
					View okView = mActionBar.findViewById(mOKId);
					if (okView != null) {
						if (mOK != null && (okView instanceof TextView)) {
							TextView okButton = (TextView) okView;
							okButton.setText(mOK);
						}
						okView.setOnClickListener(new OKClickListener());
					}
					mOKView = okView;
					mOKView.setEnabled(mOKEnabled);
				}
				if (mActionBar.getLayoutParams() == null) {
					mActionBar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
							LinearLayout.LayoutParams.WRAP_CONTENT));
				}
				alertView.addView(mActionBar);
			}
		} else {
			alertView.addView(new View(mContext),
					new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0));
		}
		realContentView.addView(alertView, new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT));
		mDialog = new ODialog(mContext);
		mDialog.setCancelable(mCancelable);
		mDialog.setCanceledOnTouchOutside(mCancelable);
		mDialog.setContentView(realContentView,
				new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		mDialog.setOnCancelListener(new DialogCancelListener());
		mDialog.setOnDismissListener(new DialogDismissListener());
		mDialog.show();
	}

	class OKClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			ok();
		}

	}

	class CancelClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			cancel();
		}

	}

	class CloseClickListener implements View.OnClickListener {

		@Override
		public void onClick(View v) {
			close();
		}

	}

	class DialogCancelListener implements DialogInterface.OnCancelListener {

		@Override
		public void onCancel(DialogInterface dialog) {
			cancel();
		}

	}

	class DialogDismissListener implements DialogInterface.OnDismissListener {

		@Override
		public void onDismiss(DialogInterface dialog) {
			if (mListener != null) {
				mListener.onDismiss(Alert.this);
			}
		}

	}

}

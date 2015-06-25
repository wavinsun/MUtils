package cn.o.app.ui;

import java.util.List;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.o.app.R;
import cn.o.app.adapter.IItemView;
import cn.o.app.adapter.ItemFrame;
import cn.o.app.adapter.OVLinearAdapter;
import cn.o.app.ui.ActionSheet.ActionItem;

@SuppressWarnings("deprecation")
public class ActionSheet<DATA_ITEM extends ActionItem> {

	public static interface OnActionItemClickListener<DATA_ITEM extends ActionItem> {
		public void onItemClick(ActionSheet<DATA_ITEM> sheet, View v,
				int position, DATA_ITEM dataItem);
	}

	public static class ActionItem {

		protected int mTextColor = 0xFF2F82C7;

		protected String mText = "";

		public int getTextColor() {
			return mTextColor;
		}

		public void setTextColor(int textColor) {
			mTextColor = textColor;
		}

		public String getText() {
			return mText;
		}

		public void setText(String text) {
			mText = text;
		}
	}

	protected OnActionItemClickListener<DATA_ITEM> mOnActionItemClickListener;

	protected OActionSheetAdapter<DATA_ITEM> mAdapter;

	protected ODialog mDialog;

	protected Context mContext;

	protected int mBackgroundColor = 0xFFEDEDED;

	protected int mLeftMargin;

	protected int mRightMargin;

	protected int mTopMargin;

	protected int mBottomMargin;

	protected CharSequence mCancel;

	public ActionSheet(Context context) {
		mContext = context;
		mAdapter = new OActionSheetAdapter<DATA_ITEM>();
		mAdapter.setActionSheet(this);

		String country = mContext.getResources().getConfiguration().locale
				.getCountry();
		if (country.equals("CN")) {
			mCancel = "取消";
		} else if (country.equals("TW")) {
			mCancel = "取消";
		} else {
			mCancel = "Cancel";
		}
	}

	public void setOnActionItemClickListener(
			OnActionItemClickListener<DATA_ITEM> listener) {
		mOnActionItemClickListener = listener;
	}

	public OnActionItemClickListener<DATA_ITEM> getOnActionItemClickListener() {
		return mOnActionItemClickListener;
	}

	public void setDataProvider(List<DATA_ITEM> dataProvider) {
		mAdapter.setDataProvider(dataProvider);
	}

	public void setBackgroundColor(int color) {
		if (mDialog != null) {
			return;
		}
		mBackgroundColor = color;
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

	public void setCancel(CharSequence cancel) {
		if (mDialog != null) {
			return;
		}
		mCancel = cancel;
	}

	public void cancel() {
		if (mDialog == null) {
			return;
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

	public void show() {
		if (mDialog != null) {
			mDialog.show();
			return;
		}
		DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
		int margin = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 8, metrics);
		RelativeLayout realContentView = new RelativeLayout(mContext);
		realContentView.setPadding(margin + mLeftMargin, margin + mTopMargin,
				margin + mRightMargin, margin + mBottomMargin);
		realContentView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				cancel();
			}
		});
		int cancelButtonId = 1;
		TextView cancelButton = new TextView(mContext);
		cancelButton.setId(cancelButtonId);
		int padding = (int) TypedValue.applyDimension(
				TypedValue.COMPLEX_UNIT_DIP, 10, metrics);
		cancelButton.setPadding(padding, padding, padding, padding);
		cancelButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		cancelButton.setTextColor(0xFF2F82C7);
		cancelButton.setGravity(Gravity.CENTER);
		cancelButton.setText(mCancel);
		StateListDrawable stateListDrawable = new StateListDrawable();
		GradientDrawable pressedDrawable = new GradientDrawable();
		pressedDrawable.setColor(0xFFD9D9D9);
		pressedDrawable.setCornerRadius(10);
		stateListDrawable.addState(new int[] { android.R.attr.state_pressed,
				android.R.attr.state_enabled }, pressedDrawable);
		GradientDrawable normalDrawable = new GradientDrawable();
		normalDrawable.setColor(mBackgroundColor);
		normalDrawable.setCornerRadius(10);
		stateListDrawable.addState(new int[] {}, normalDrawable);
		cancelButton.setBackgroundDrawable(stateListDrawable);
		cancelButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				cancel();
			}
		});
		RelativeLayout.LayoutParams cancelParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		cancelParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		realContentView.addView(cancelButton, cancelParams);
		ScrollView scrollView = new ScrollView(mContext);
		scrollView.setVerticalScrollBarEnabled(false);
		GradientDrawable scrollDrawable = new GradientDrawable();
		scrollDrawable.setColor(mBackgroundColor);
		scrollDrawable.setCornerRadius(10);
		scrollView.setBackgroundDrawable(scrollDrawable);
		LinearLayout scrollContent = new LinearLayout(mContext);
		scrollContent.setOrientation(LinearLayout.VERTICAL);
		mAdapter.setContainer(scrollContent);
		scrollView.addView(scrollContent, new FrameLayout.LayoutParams(
				FrameLayout.LayoutParams.MATCH_PARENT,
				FrameLayout.LayoutParams.WRAP_CONTENT));
		RelativeLayout.LayoutParams scrollParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		scrollParams.addRule(RelativeLayout.ABOVE, cancelButtonId);
		scrollParams.setMargins(0, 0, 0, margin);
		realContentView.addView(scrollView, scrollParams);
		mDialog = new ODialog(mContext);
		mDialog.setWindowAnimations(R.style.ActionSheetAnim);
		mDialog.setContentView(realContentView, new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT));
		mDialog.requestHFill();
		mDialog.show();
	}

	protected static class OActionSheetAdapter<DATA_ITEM extends ActionItem>
			extends OVLinearAdapter<DATA_ITEM> {

		protected ActionSheet<DATA_ITEM> mActionSheet;

		public ActionSheet<DATA_ITEM> getActionSheet() {
			return mActionSheet;
		}

		public void setActionSheet(ActionSheet<DATA_ITEM> actionSheet) {
			mActionSheet = actionSheet;
		}

		@Override
		public IItemView<DATA_ITEM> getItemView() {
			return new OActionItemView<DATA_ITEM>(getContext());
		}
	}

	protected static class OActionItemView<DATA_ITEM extends ActionItem>
			extends ItemFrame<DATA_ITEM> {
		protected LinearLayout mRoot;

		protected TextView mTextView;

		protected View mLine;

		public OActionItemView(Context context) {
			super(context);
		}

		@Override
		public void onCreate() {
			Context context = getContext();
			DisplayMetrics metrics = context.getResources().getDisplayMetrics();
			int padding = (int) TypedValue.applyDimension(
					TypedValue.COMPLEX_UNIT_DIP, 10, metrics);
			mRoot = new LinearLayout(context);
			mRoot.setOrientation(LinearLayout.VERTICAL);
			mTextView = new TextView(getContext());
			mTextView.setPadding(padding, padding, padding, padding);
			mTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
			mTextView.setGravity(Gravity.CENTER);
			mRoot.addView(mTextView, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT,
					LinearLayout.LayoutParams.WRAP_CONTENT));
			mLine = new View(context);
			mLine.setBackgroundDrawable(new ColorDrawable(0xFFCCCCCC));
			mRoot.addView(mLine, new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, 1));
			this.setContentView(mRoot);
			this.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					OActionSheetAdapter<DATA_ITEM> adapter = (OActionSheetAdapter<DATA_ITEM>) getAdapter();
					ActionSheet<DATA_ITEM> actionSheet = adapter
							.getActionSheet();
					OnActionItemClickListener<DATA_ITEM> listener = actionSheet
							.getOnActionItemClickListener();
					if (listener != null) {
						listener.onItemClick(actionSheet, v, getPosition(),
								getDataProvider());
					}
					actionSheet.dismiss();
				}
			});
		}

		@Override
		public void onResume() {
			OActionSheetAdapter<DATA_ITEM> adapter = (OActionSheetAdapter<DATA_ITEM>) getAdapter();
			ActionItem data = getDataProvider();
			mTextView.setText(data.getText());
			mTextView.setTextColor(data.getTextColor());
			boolean isFirst = mPosition == 0;
			boolean isLast = mPosition == adapter.getCount() - 1;
			mLine.setVisibility(isLast ? View.GONE : View.VISIBLE);
			StateListDrawable stateListDrawable = new StateListDrawable();
			GradientDrawable gradientDrawable = new GradientDrawable();
			gradientDrawable.setColor(0xFFD9D9D9);
			gradientDrawable.setCornerRadii(new float[] { isFirst ? 10 : 0,
					isFirst ? 10 : 0, isFirst ? 10 : 0, isFirst ? 10 : 0,
					isLast ? 10 : 0, isLast ? 10 : 0, isLast ? 10 : 0,
					isLast ? 10 : 0 });
			stateListDrawable.addState(
					new int[] { android.R.attr.state_pressed,
							android.R.attr.state_enabled }, gradientDrawable);
			this.setBackgroundDrawable(stateListDrawable);
		}
	}

}

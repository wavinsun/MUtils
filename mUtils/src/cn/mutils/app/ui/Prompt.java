package cn.mutils.app.ui;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import cn.mutils.app.AppUtil;

@SuppressWarnings("deprecation")
public class Prompt extends Alert {

	protected EditText mEditText;

	public Prompt(Context context) {
		super(context);
		super.setContentView(generateContentView());
	}

	public String getEditText() {
		return mEditText == null ? "" : mEditText.getText().toString();
	}

	public EditText getEditTextView() {
		return mEditText;
	}

	public void setEditTextSize(float size) {
		if (mEditText == null) {
			return;
		}
		mEditText.setTextSize(size);
	}

	@Override
	public void setContentView(int contentViewLayoutId) {

	}

	@Override
	public void setContentView(View contentView) {

	}

	protected View generateContentView() {
		int padding = (int) AppUtil.dp2px(getContext(), 8);
		GradientDrawable drawable = new GradientDrawable();
		drawable.setColor(0xFFFFFFFF);
		drawable.setCornerRadius(padding / 2);
		mEditText = new EditText(getContext());
		mEditText.setBackgroundDrawable(drawable);
		mEditText.setTextSize(16);
		mEditText.setPadding(padding, padding, padding, padding);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		params.setMargins(padding, padding, padding, padding);
		mEditText.setLayoutParams(params);
		return mEditText;
	}
}

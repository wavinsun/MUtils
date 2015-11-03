package cn.mutils.app.ui;

import java.util.regex.Pattern;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.mutils.app.R;

@SuppressWarnings("deprecation")
public class EditBox extends LinearLayout {

	protected Pattern mRegex;

	protected EditText mEdit;

	protected TextView mTip;

	protected ImageView mClear;

	protected Drawable mNormalBackgroundDrawable;

	protected Drawable mWarnBackgroundDrawable;

	protected boolean mWarning;

	protected boolean mAutoUnWarnOnChanged;

	protected boolean mFocused;

	protected boolean mAttachedToWindow;

	public EditBox(Context context) {
		super(context);
		init(context, null);
	}

	public EditBox(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	protected void init(Context context, AttributeSet attrs) {
		if (attrs != null) {
			TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.EditBox);
			String text = typedArray.getString(R.styleable.EditBox_android_text);
			if (text != null) {
				this.setText(text);
			}
			String hint = typedArray.getString(R.styleable.EditBox_android_hint);
			if (hint != null) {
				this.setHint(hint);
			}
			ColorStateList hintTextColor = typedArray.getColorStateList(R.styleable.EditBox_android_textColorHint);
			if (hintTextColor != null) {
				this.setHintTextColor(hintTextColor);
			}
			int textSize = typedArray.getDimensionPixelSize(R.styleable.EditBox_android_textSize, 0);
			if (textSize != 0) {
				this.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
			}
			ColorStateList textColor = typedArray.getColorStateList(R.styleable.EditBox_android_textColor);
			if (textColor != null) {
				this.setTextColor(textColor);
			}
			ColorStateList tipTextColor = typedArray.getColorStateList(R.styleable.EditBox_textColorTip);
			if (tipTextColor != null) {
				this.setTipColor(tipTextColor);
			}
			String textTip = typedArray.getString(R.styleable.EditBox_textTip);
			if (textTip != null) {
				this.setTipText(textTip);
			}
			Drawable iconTip = typedArray.getDrawable(R.styleable.EditBox_iconTip);
			if (iconTip != null) {
				this.setTipIcon(iconTip);
			}
			Drawable iconClear = typedArray.getDrawable(R.styleable.EditBox_iconClear);
			if (iconClear != null) {
				this.setClearIcon(iconClear);
			}
			Drawable warnBackground = typedArray.getDrawable(R.styleable.EditBox_backgroundWarn);
			if (warnBackground != null) {
				this.setWarnBackground(warnBackground);
			}
			int inputType = typedArray.getInt(R.styleable.EditBox_android_inputType, EditorInfo.TYPE_NULL);
			if (inputType != EditorInfo.TYPE_NULL) {
				this.setInputType(inputType);
			}
			int contentPadding = typedArray.getDimensionPixelSize(R.styleable.EditBox_iconPadding, 0);
			if (contentPadding != 0) {
				this.setIconPadding(contentPadding);
			}
			int tipTextWidth = typedArray.getDimensionPixelSize(R.styleable.EditBox_textWidthTip, 0);
			if (tipTextWidth != 0) {
				this.setTipTextWidth(tipTextWidth);
			}
			int maxLength = typedArray.getInt(R.styleable.EditBox_android_maxLength, 0);
			if (maxLength != 0) {
				this.setMaxLength(maxLength);
			}
			String regex = typedArray.getString(R.styleable.EditBox_regex);
			if (regex != null) {
				this.setRegex(regex);
			}
			typedArray.recycle();
		}
		this.setOrientation(LinearLayout.HORIZONTAL);
		this.setGravity(Gravity.CENTER_VERTICAL);
		mTip = new TextView(context);
		mTip.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		this.addView(mTip, 0);
		mEdit = new EditText(context);
		mEdit.setSingleLine();
		mEdit.setBackgroundDrawable(null);
		mEdit.setPadding(0, 0, 0, 0);
		mEdit.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1));
		mEdit.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (mRegex == null) {
					return;
				}
				String changedText = mEdit.getText().toString();
				String regexedText = mRegex.matcher(changedText).replaceAll("");
				if (!changedText.equals(regexedText)) {
					mEdit.setText(regexedText);
					mEdit.setSelection(regexedText.length());
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (mAutoUnWarnOnChanged) {
					unWarn();
				}
				if (!mEdit.isFocused()) {
					return;
				}
				int clearVisibility = s.toString().isEmpty() ? View.GONE : View.VISIBLE;
				if (clearVisibility != mClear.getVisibility()) {
					mClear.setVisibility(clearVisibility);
				}
			}
		});
		mEdit.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				int clearVisibility = View.GONE;
				if (hasFocus) {
					clearVisibility = mEdit.getText().toString().isEmpty() ? View.GONE : View.VISIBLE;
					setSelected(true);
				} else {
					setSelected(false);
				}
				if (clearVisibility != mClear.getVisibility()) {
					mClear.setVisibility(clearVisibility);
				}
				OnFocusChangeListener listener = getOnFocusChangeListener();
				if (listener != null) {
					listener.onFocusChange(EditBox.this, hasFocus);
				}
			}
		});
		this.addView(mEdit, 1);
		mClear = new ImageView(context);
		mClear.setVisibility(View.GONE);
		mClear.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		mClear.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				mEdit.setText("");
				mClear.setVisibility(View.GONE);
			}
		});
		this.addView(mClear, 2);
		this.mAutoUnWarnOnChanged = true;
	}

	public void setAutoUnWarnOnChanged(boolean autoUnWarnOnChanged) {
		mAutoUnWarnOnChanged = autoUnWarnOnChanged;
	}

	public boolean isAutoUnWarnOnChanged() {
		return mAutoUnWarnOnChanged;
	}

	public void setRegex(String regex) {
		mRegex = Pattern.compile(regex);
	}

	public void setMaxLength(int maxLength) {
		if (maxLength > 0) {
			mEdit.setFilters(new InputFilter[] { new InputFilter.LengthFilter(maxLength) });
		}
	}

	public void setTipTextWidth(int tipTextWidth) {
		LinearLayout.LayoutParams tipLayoutParams = (LinearLayout.LayoutParams) mTip.getLayoutParams();
		tipLayoutParams.width = tipTextWidth;
		mTip.setLayoutParams(tipLayoutParams);
	}

	@Override
	protected void onAttachedToWindow() {
		super.onAttachedToWindow();
		mAttachedToWindow = true;
		if (mFocused) {
			mEdit.requestFocus();
		} else {
			mEdit.clearFocus();
		}
	}

	@Override
	protected void onDetachedFromWindow() {
		mAttachedToWindow = false;
		mFocused = false;
		super.onDetachedFromWindow();
	}

	public void setFocused(boolean focused) {
		mFocused = focused;
		if (!mAttachedToWindow) {
			return;
		}
		if (mFocused) {
			mEdit.requestFocus();
		} else {
			mEdit.clearFocus();
		}
	}

	public boolean isFoucused() {
		return mFocused;
	}

	public void addTextChangedListener(TextWatcher watcher) {
		mEdit.addTextChangedListener(watcher);
	}

	public void removeTextChangedListener(TextWatcher watcher) {
		mEdit.removeTextChangedListener(watcher);
	}

	public void setIconPadding(int padding) {
		this.mEdit.setPadding(padding, 0, padding, 0);
	}

	public void setSingleLine() {
		this.setSingleLine(true);
	}

	public void setSingleLine(boolean singleLine) {
		mEdit.setSingleLine(singleLine);
	}

	public boolean isInputTypeOfPassword() {
		int variation = mEdit.getInputType() & (EditorInfo.TYPE_MASK_CLASS | EditorInfo.TYPE_MASK_VARIATION);
		return variation == (EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
	}

	public void setInputTypeOfPassword() {
		this.setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
	}

	public void setInputTypeOfText() {
		this.setInputType(EditorInfo.TYPE_CLASS_TEXT);
	}

	public int getInputType() {
		return mEdit.getInputType();
	}

	public void setInputType(int type) {
		int start = mEdit.getSelectionStart();
		int end = mEdit.getSelectionEnd();
		mEdit.setInputType(type);
		mEdit.setSelection(start, end);
	}

	public void setHint(int resId) {
		mEdit.setHint(resId);
	}

	public void setHint(CharSequence hint) {
		mEdit.setHint(hint);
	}

	public void setHintTextColor(int color) {
		mEdit.setHintTextColor(color);
	}

	public void setHintTextColor(ColorStateList colors) {
		mEdit.setHintTextColor(colors);
	}

	public String getText() {
		return mEdit.getText().toString();
	}

	public void setText(CharSequence text) {
		mEdit.setText(text);
		if (text != null) {
			mEdit.setSelection(text.length());
		}
	}

	public void setTextColor(int color) {
		mEdit.setTextColor(color);
	}

	public void setTextColor(ColorStateList colors) {
		mEdit.setTextColor(colors);
	}

	public void setTipColor(int color) {
		mTip.setTextColor(color);
	}

	public void setTipColor(ColorStateList colors) {
		mTip.setTextColor(colors);
	}

	public void setTextSize(float size) {
		mTip.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
		mEdit.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
	}

	public void setTextSize(int unit, float size) {
		mTip.setTextSize(unit, size);
		mEdit.setTextSize(unit, size);
	}

	public void setTipIcon(int resId) {
		Drawable icon = getContext().getResources().getDrawable(resId);
		setTipIcon(icon);
	}

	public void setTipIcon(Drawable icon) {
		mTip.setCompoundDrawablesWithIntrinsicBounds(icon, null, null, null);
		CharSequence tip = mTip.getText();
		if (tip != null) {
			if (!tip.toString().isEmpty()) {
				mTip.setText("");
			}
		}
	}

	public void setTipText(int resId) {
		String tip = getContext().getResources().getString(resId);
		setTipText(tip);
	}

	public void setTipText(CharSequence text) {
		mTip.setText(text);
		Drawable[] drawables = mTip.getCompoundDrawables();
		if (drawables[0] != null) {
			mTip.setCompoundDrawables(null, null, null, null);
		}
	}

	public void setClearIcon(int resId) {
		mClear.setImageResource(resId);
	}

	public void setClearIcon(Drawable icon) {
		mClear.setImageDrawable(icon);
	}

	public void setWarnBackground(int resId) {
		mWarnBackgroundDrawable = this.getContext().getResources().getDrawable(resId);
	}

	public void setWarnBackground(Drawable background) {
		mWarnBackgroundDrawable = background;
	}

	public void warn() {
		if (mWarning) {
			return;
		}
		mNormalBackgroundDrawable = this.getBackground();
		this.setBackgroundDrawable(mWarnBackgroundDrawable);
		mWarning = true;
	}

	public void unWarn() {
		if (!mWarning) {
			return;
		}
		this.setBackgroundDrawable(mNormalBackgroundDrawable);
		mWarning = false;
	}

	public boolean isWarn() {
		return mWarning;
	}

}

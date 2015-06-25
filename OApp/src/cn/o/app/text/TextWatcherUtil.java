package cn.o.app.text;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class TextWatcherUtil {

	public static TextWatcher setEditTextDecimals(EditText editText,
			int decimals) {
		EditTextDecimalsTextWatcher watcher = new EditTextDecimalsTextWatcher();
		watcher.setEditText(editText);
		watcher.setDecimals(decimals);
		editText.addTextChangedListener(watcher);
		return watcher;
	}

	public static class EditTextDecimalsTextWatcher implements TextWatcher {
		protected EditText mEditText;

		protected int mDecimals;

		protected boolean catchChanged;

		public void setEditText(EditText editText) {
			mEditText = editText;
		}

		public void setDecimals(int decimals) {
			mDecimals = decimals;
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}

		@Override
		public void afterTextChanged(Editable s) {
			if (catchChanged) {
				return;
			}
			catchChanged = true;
			String str = s.toString();
			int dotIndex = str.indexOf(".");
			if (dotIndex >= 0) {
				String strDecmals = str.substring(dotIndex + 1);
				if (strDecmals.length() > mDecimals) {
					str = str.substring(0, dotIndex + 1 + mDecimals);
					mEditText.setText(str);
					mEditText.setSelection(str.length());
				}
			}
			catchChanged = false;
		}
	}
}

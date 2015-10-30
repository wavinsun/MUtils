package cn.mutils.app.ui.pattern;

import android.content.Context;
import cn.mutils.app.R;
import cn.mutils.app.ui.Dialoger;

public class PatternDialog extends Dialoger {

	public PatternDialog(Context context) {
		super(context);
	}

	public PatternDialog(Context context, int theme) {
		super(context, theme);
	}

	public PatternDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	@Override
	protected void init(Context context) {
		super.init(context);
		this.setWindowAnimations(R.style.DialogerFadeAnim);
		this.clearBehind();
		this.requestFill();
		this.setCancelable(false);
		this.setCanceledOnTouchOutside(false);
	}

	public void refresh() {

	}

}

package cn.mutils.app.ui;

import android.app.Activity;
import android.content.Context;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import cn.mutils.app.AppUtil;
import cn.mutils.app.R;

@SuppressWarnings("deprecation")
public class WaitingDialog extends Dialoger {

	public WaitingDialog(Context context) {
		super(context);
	}

	@Override
	protected void init(Context context) {
		super.init(context);
		this.setWindowAnimations(R.style.DialogerNoAnim);
		this.clearBehind();
		this.requestFill();
		this.setCancelable(false);
		this.setCanceledOnTouchOutside(false);

		RelativeLayout root = new RelativeLayout(context);
		root.setLayoutParams(
				new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
		RelativeLayout.LayoutParams iconParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
		iconParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		ProgressIcon icon = new ProgressIcon(context);
		icon.setDrawable(context.getResources().getDrawable(R.drawable.ic_waiting));
		icon.setLayoutParams(iconParams);
		root.addView(icon);
		this.setContentView(root);
	}

	@Override
	public void onBackPressed() {
		Activity activity = AppUtil.toActivity(getContext());
		if (activity != null) {
			activity.onBackPressed();
		} else {
			super.onBackPressed();
		}
	}

}

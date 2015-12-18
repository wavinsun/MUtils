package cn.mutils.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;
import cn.mutils.app.core.annotation.event.OnClick;
import cn.mutils.app.core.annotation.res.SetContentView;
import cn.mutils.app.demo.R;
import cn.mutils.app.ui.AppActivity;
import cn.mutils.app.ui.StateView;

@SetContentView(R.layout.view_busy)
public class BusyDemoView extends StateView {

	public BusyDemoView(Context context) {
		super(context);
	}

	public BusyDemoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public BusyDemoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@OnClick(R.id.go)
	protected void onClickGo() {
		((AppActivity) getContext()).setBusy(true);
		getMainHandler().postDelayed(new Runnable() {

			@Override
			public void run() {
				((AppActivity) getContext()).setBusy(false);
			}
		}, 2000);
	}

}

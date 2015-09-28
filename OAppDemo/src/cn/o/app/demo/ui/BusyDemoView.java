package cn.o.app.demo.ui;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import cn.o.app.core.annotation.event.OnClick;
import cn.o.app.core.annotation.res.SetContentView;
import cn.o.app.demo.R;
import cn.o.app.ui.Activitier;
import cn.o.app.ui.StateView;

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
		((Activitier) getContext()).setBusy(true);
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				((Activitier) getContext()).setBusy(false);
			}
		}, 2000);
	}

}

package cn.o.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;
import cn.o.app.core.annotation.event.OnClick;
import cn.o.app.core.annotation.res.SetContentView;
import cn.o.app.demo.R;
import cn.o.app.ui.Alert;
import cn.o.app.ui.Alert.AlertListener;
import cn.o.app.ui.StateView;

@SetContentView(R.layout.view_alert)
public class AlertDemoView extends StateView {

	public AlertDemoView(Context context) {
		super(context);
	}

	public AlertDemoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AlertDemoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@OnClick(R.id.go)
	protected void onClickGo() {
		Alert alert = new Alert(getContext());
		alert.setTitle("标题");
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 50; i++) {
			if (i != 0) {
				sb.append("\n");
			}
			sb.append("消息");
		}
		alert.setMessage(sb);
		alert.setOK(R.string.ok);
		alert.setCancel(R.string.cancel);
		alert.setListener(new AlertListener() {

			@Override
			public boolean onOK(Alert alert) {
				toast("onOK");
				return false;
			}

			@Override
			public boolean onCancel(Alert alert) {
				toast("onCancel");
				return false;
			}
		});
		alert.show();
	}

}

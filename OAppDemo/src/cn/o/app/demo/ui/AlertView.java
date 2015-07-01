package cn.o.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;
import cn.o.app.annotation.event.OnClick;
import cn.o.app.annotation.res.SetContentView;
import cn.o.app.demo.R;
import cn.o.app.ui.Alert;
import cn.o.app.ui.Alert.AlertListener;
import cn.o.app.ui.StateView;

@SetContentView(R.layout.view_alert)
public class AlertView extends StateView {

	public AlertView(Context context) {
		super(context);
	}

	public AlertView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AlertView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@OnClick(R.id.go)
	protected void onClickGo() {
		Alert alert = new Alert(getContext());
		alert.setTitle("标题");
		alert.setMessage("消息");
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

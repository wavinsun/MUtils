package cn.o.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;
import cn.o.app.OUtil;
import cn.o.app.annotation.event.OnClick;
import cn.o.app.annotation.res.SetContentView;
import cn.o.app.demo.R;
import cn.o.app.ui.Alert;
import cn.o.app.ui.Alert.AlertListener;
import cn.o.app.ui.StateView;

@SetContentView(R.layout.view_zip)
public class ZipDemoView extends StateView {

	public ZipDemoView(Context context) {
		super(context);
	}

	public ZipDemoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ZipDemoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@OnClick(R.id.go)
	protected void onClickGo() {
		Alert alert = new Alert(getContext());
		alert.setOK(R.string.ok);
		alert.setCancel(R.string.cancel);
		alert.setTitle(OUtil.getAssetZipString(getContext(), "hello.zip", "title/title.txt"));
		alert.setMessage(OUtil.getAssetZipString(getContext(), "hello.zip", "message/message.txt"));
		alert.setListener(new AlertListener() {

			@Override
			public boolean onOK(Alert alert) {
				return false;
			}

			@Override
			public boolean onCancel(Alert alert) {
				return false;
			}

			@Override
			public boolean onDismiss(Alert alert) {
				toast(OUtil.getAssetZipString(getContext(), "hello.zip", "hello.txt"));
				return false;
			}
		});
		alert.show();
	}

}

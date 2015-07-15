package cn.o.app.demo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import cn.o.app.annotation.event.OnClick;
import cn.o.app.annotation.res.FindViewById;
import cn.o.app.annotation.res.SetContentView;
import cn.o.app.demo.CaptureActivity;
import cn.o.app.demo.CaptureActivity.CaptureResult;
import cn.o.app.demo.R;
import cn.o.app.event.listener.OnActivityResultListener;
import cn.o.app.qrcode.QRCodeView;
import cn.o.app.ui.StateView;

@SetContentView(R.layout.view_qrcode)
public class QRCodeDemoView extends StateView {

	public static final int REQUEST_CODE_CAPTURE = 1004;

	@FindViewById(R.id.code)
	protected QRCodeView mCodeView;

	public QRCodeDemoView(Context context) {
		super(context);
	}

	public QRCodeDemoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public QRCodeDemoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onCreate() {
		super.onCreate();

		this.addOnActivityResultListener(new OnActivityResultListener() {

			@Override
			public void onActivityResult(Context context, int requestCode, int resultCode, Intent data) {
				if (requestCode == REQUEST_CODE_CAPTURE && resultCode == Activity.RESULT_OK) {
					CaptureResult result = new CaptureResult();
					result.getFrom(data);
					mCodeView.setText(result.getCode());
				}
			}
		});
	}

	@OnClick(R.id.go)
	protected void onClickGo() {
		startActivityForResult(new Intent(getContext(), CaptureActivity.class), REQUEST_CODE_CAPTURE);
	}

}

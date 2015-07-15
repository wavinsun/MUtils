package cn.o.app.demo;

import android.content.Intent;
import android.os.Bundle;
import cn.o.app.annotation.res.FindViewById;
import cn.o.app.annotation.res.SetContentView;
import cn.o.app.demo.ui.BasicActivity;
import cn.o.app.io.Extra;
import cn.o.app.qrcode.CaptureView;
import cn.o.app.qrcode.CaptureView.CaptureListener;

@SuppressWarnings("serial")
@SetContentView(R.layout.activity_capture)
public class CaptureActivity extends BasicActivity {

	public static class CaptureResult extends Extra {

		protected String mCode;

		public String getCode() {
			return mCode;
		}

		public void setCode(String code) {
			mCode = code;
		}

	}

	@FindViewById(R.id.capture)
	protected CaptureView mCaptureView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mCaptureView.setListener(new CaptureListener() {

			@Override
			public void onCapture(String data) {
				Intent intent = new Intent();
				CaptureResult result = new CaptureResult();
				result.setCode(data);
				result.putTo(intent);
				setResult(RESULT_OK, intent);
				finish();
			}
		});
	}

}

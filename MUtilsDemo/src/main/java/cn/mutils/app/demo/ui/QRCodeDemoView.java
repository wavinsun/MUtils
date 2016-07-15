package cn.mutils.app.demo.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;

import cn.mutils.app.demo.CaptureActivity;
import cn.mutils.app.demo.CaptureActivity.CaptureResult;
import cn.mutils.app.demo.R;
import cn.mutils.app.event.listener.OnActivityResultListener;
import cn.mutils.app.ui.StateView;
import cn.mutils.app.zxing.QRCodeView;
import cn.mutils.core.annotation.event.Click;
import cn.mutils.core.annotation.res.FindViewById;
import cn.mutils.core.annotation.res.SetContentView;

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

    @Click(R.id.go)
    protected void onClickGo() {
        startActivityForResult(new Intent(getContext(), CaptureActivity.class), REQUEST_CODE_CAPTURE);
    }

}

package cn.mutils.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import cn.mutils.app.demo.R;
import cn.mutils.app.ui.StateView;
import cn.mutils.app.util.AppUtil;
import cn.mutils.core.annotation.res.FindViewById;
import cn.mutils.core.annotation.res.SetContentView;

@SetContentView(R.layout.view_aes)
public class AESDemoView extends StateView {

    @FindViewById(R.id.log)
    protected TextView mLog;

    public AESDemoView(Context context) {
        super(context);
    }

    public AESDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AESDemoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        String text = "MUtils";
        String password = "wavinsun";
        String encryptText = AppUtil.toAES(text, password);
        String decryptText = AppUtil.fromAES(encryptText, password);
        StringBuilder sb = new StringBuilder();
        sb.append("Text:");
        sb.append(text);
        sb.append("\nPassword:");
        sb.append(password);
        sb.append("\nEncrypt Text:");
        sb.append(encryptText);
        sb.append("\nDecrypt Text:");
        sb.append(decryptText);

        mLog.setText(sb);
    }

}

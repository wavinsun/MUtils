package cn.mutils.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import cn.mutils.app.demo.R;
import cn.mutils.app.ui.StateView;
import cn.mutils.app.util.AppUtil;
import cn.mutils.core.annotation.res.FindViewById;
import cn.mutils.core.annotation.res.SetContentView;

@SetContentView(R.layout.view_zip)
public class ZipDemoView extends StateView {

    @FindViewById(R.id.log)
    protected TextView mLog;

    public ZipDemoView(Context context) {
        super(context);
    }

    public ZipDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ZipDemoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        StringBuilder sb = new StringBuilder();
        sb.append(AppUtil.getAssetZipString(getContext(), "hello.zip", "title/title.txt"));
        sb.append("\n");
        sb.append(AppUtil.getAssetZipString(getContext(), "hello.zip", "message/message.txt"));
        sb.append("\n");
        sb.append(AppUtil.getAssetZipString(getContext(), "hello.zip", "hello.txt"));
        sb.append("\n");
        mLog.setText(sb);
    }

}

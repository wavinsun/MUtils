package cn.mutils.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import cn.mutils.app.demo.R;
import cn.mutils.app.ui.StateView;
import cn.mutils.app.util.AppUtil;
import cn.mutils.core.annotation.res.FindViewById;
import cn.mutils.core.annotation.res.SetContentView;

@SetContentView(R.layout.view_channel)
public class ChannelDemoView extends StateView {

    @FindViewById(R.id.log)
    protected TextView mLog;

    public ChannelDemoView(Context context) {
        super(context);
    }

    public ChannelDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ChannelDemoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mLog.setText(AppUtil.getAppMetaData(getContext(), "UMENG_CHANNEL"));
    }
}

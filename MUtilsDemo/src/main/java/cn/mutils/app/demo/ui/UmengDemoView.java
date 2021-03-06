package cn.mutils.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;

import cn.mutils.app.demo.R;
import cn.mutils.app.ui.StateView;
import cn.mutils.core.annotation.event.Click;
import cn.mutils.core.annotation.res.SetContentView;
import cn.mutils.core.event.listener.VersionUpdateListener;

@SetContentView(R.layout.view_umeng)
public class UmengDemoView extends StateView {

    public UmengDemoView(Context context) {
        super(context);
    }

    public UmengDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public UmengDemoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Click(R.id.umeng_update)
    protected void onClickUmengUpdate() {
        ((BasicActivity) getContext()).checkNewVersion(new VersionUpdateListener() {

            @Override
            public boolean onYes(String version) {
                return false;
            }

            @Override
            public void onUpdateCancel(String version) {

            }

            @Override
            public void onUpdate(String version) {

            }

            @Override
            public void onNo() {

            }
        });
    }

    @Click(R.id.umeng_feedback)
    protected void onClickUmengFeedback() {
        ((BasicActivity) getContext()).feedback();
    }

}

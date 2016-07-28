package cn.mutils.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import cn.mutils.app.demo.R;
import cn.mutils.app.ui.StateView;
import cn.mutils.core.annotation.event.Click;
import cn.mutils.core.annotation.res.FindViewById;
import cn.mutils.core.annotation.res.SetContentView;

@SetContentView(R.layout.view_sliding_up_panel)
public class SlidingUpPanelDemoView extends StateView {

    @FindViewById(R.id.sliding_layout)
    private SlidingUpPanelLayout mSlidingLayout;

    public SlidingUpPanelDemoView(Context context) {
        super(context);
    }

    public SlidingUpPanelDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlidingUpPanelDemoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Click(R.id.go)
    protected void onClickGoBtn(){
        toast("OK");
    }

    @Override
    public boolean onInterceptBackPressed() {
        if (mSlidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            mSlidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            return true;
        }
        return super.onInterceptBackPressed();
    }
}

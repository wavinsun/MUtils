package cn.mutils.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import cn.mutils.app.demo.R;
import cn.mutils.app.ui.StateView;
import cn.mutils.app.ui.TipView;
import cn.mutils.core.annotation.event.Click;
import cn.mutils.core.annotation.res.FindViewById;
import cn.mutils.core.annotation.res.SetContentView;

@SetContentView(R.layout.view_tip)
public class TipViewDemoView extends StateView {

    @FindViewById(R.id.tip)
    protected TipView mTip;

    public TipViewDemoView(Context context) {
        super(context);
    }

    public TipViewDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TipViewDemoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Click(R.id.top_1)
    protected void onClickTop1(View v) {
        mTip.setTriangleAlign(TipView.TRIANGLE_ALIGN_TOP_RIGHT);
        mTip.pointTo(v);
    }

    @Click(R.id.top_2)
    protected void onClickTop2(View v) {
        mTip.setTriangleAlign(TipView.TRIANGLE_ALIGN_TOP_RIGHT);
        mTip.pointTo(v);
    }

    @Click(R.id.top_3)
    protected void onClickTop3(View v) {
        mTip.setTriangleAlign(TipView.TRIANGLE_ALIGN_TOP_RIGHT);
        mTip.pointTo(v);
    }

    @Click(R.id.top_4)
    protected void onClickTop4(View v) {
        mTip.setTriangleAlign(TipView.TRIANGLE_ALIGN_TOP_RIGHT);
        mTip.pointTo(v);
    }

    @Click(R.id.down_1)
    protected void onClickBottom1(View v) {
        mTip.setTriangleAlign(TipView.TRIANGLE_ALIGN_BOTTOM_CENTER);
        mTip.pointTo(v);
    }

    @Click(R.id.down_2)
    protected void onClickBottom2(View v) {
        mTip.setTriangleAlign(TipView.TRIANGLE_ALIGN_BOTTOM_CENTER);
        mTip.pointTo(v);
    }

    @Click(R.id.down_3)
    protected void onClickBottom3(View v) {
        mTip.setTriangleAlign(TipView.TRIANGLE_ALIGN_BOTTOM_CENTER);
        mTip.pointTo(v);
    }

}

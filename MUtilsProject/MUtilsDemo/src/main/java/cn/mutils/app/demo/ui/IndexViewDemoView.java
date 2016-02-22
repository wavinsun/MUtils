package cn.mutils.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;

import cn.mutils.app.demo.R;
import cn.mutils.app.ui.IndexView;
import cn.mutils.app.ui.StateView;
import cn.mutils.core.annotation.event.Click;
import cn.mutils.core.annotation.res.FindViewById;
import cn.mutils.core.annotation.res.SetContentView;

@SetContentView(R.layout.view_index_view)
public class IndexViewDemoView extends StateView {

    @FindViewById(R.id.index_view)
    protected IndexView mIndexView;

    public IndexViewDemoView(Context context) {
        super(context);
    }

    public IndexViewDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public IndexViewDemoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mIndexView.setSize(5);
    }

    @Click(R.id.selected_index_plus)
    protected void onClickSelectedIndexPlus() {
        mIndexView.setSelectedIndex((mIndexView.getSelectedIndex() + 1) % mIndexView.getSize());
    }

}

package cn.mutils.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;

import cn.mutils.app.demo.R;
import cn.mutils.app.ui.StateView;
import cn.mutils.core.annotation.res.SetContentView;

@SetContentView(R.layout.view_progress_icon)
public class ProgressIconDemoView extends StateView {

    public ProgressIconDemoView(Context context) {
        super(context);
    }

    public ProgressIconDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressIconDemoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

}

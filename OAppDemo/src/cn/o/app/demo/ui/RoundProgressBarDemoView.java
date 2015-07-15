package cn.o.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;
import cn.o.app.annotation.res.SetContentView;
import cn.o.app.demo.R;
import cn.o.app.ui.StateView;

@SetContentView(R.layout.view_round_progress_bar)
public class RoundProgressBarDemoView extends StateView {

	public RoundProgressBarDemoView(Context context) {
		super(context);
	}

	public RoundProgressBarDemoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RoundProgressBarDemoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

}

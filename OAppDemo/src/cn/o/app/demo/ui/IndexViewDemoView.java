package cn.o.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;
import cn.o.app.core.annotation.event.OnClick;
import cn.o.app.core.annotation.res.FindViewById;
import cn.o.app.core.annotation.res.SetContentView;
import cn.o.app.demo.R;
import cn.o.app.ui.IndexView;
import cn.o.app.ui.StateView;

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

	@OnClick(R.id.selected_index_plus)
	protected void onClickSelectedIndexPlus() {
		mIndexView.setSelectedIndex((mIndexView.getSelectedIndex() + 1) % mIndexView.getSize());
	}

}

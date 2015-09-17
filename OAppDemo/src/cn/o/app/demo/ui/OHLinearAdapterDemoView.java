package cn.o.app.demo.ui;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import cn.o.app.adapter.IItemView;
import cn.o.app.adapter.ItemView;
import cn.o.app.adapter.OHLinearAdapter;
import cn.o.app.core.annotation.res.FindViewById;
import cn.o.app.core.annotation.res.SetContentView;
import cn.o.app.demo.R;
import cn.o.app.ui.StateView;

@SetContentView(R.layout.view_ohlinearadapter)
public class OHLinearAdapterDemoView extends StateView {

	@FindViewById(R.id.list)
	protected LinearLayout mList;

	protected MyAdapter mAdapter;

	public OHLinearAdapterDemoView(Context context) {
		super(context);
	}

	public OHLinearAdapterDemoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public OHLinearAdapterDemoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mAdapter = new MyAdapter();
		mAdapter.setContainer(mList);
		ArrayList<Integer> dataProvider = new ArrayList<Integer>();
		for (int i = 0; i < 5; i++) {
			dataProvider.add(
					Color.rgb((int) (Math.random() * 256), (int) (Math.random() * 256), (int) (Math.random() * 256)));
		}
		mAdapter.setDataProvider(dataProvider);
	}

	class MyAdapter extends OHLinearAdapter<Integer> {

		@Override
		public IItemView<Integer> getItemView() {
			return new ColorRectItemView(getContext());
		}

	}

	@SetContentView(R.layout.item_color_rect)
	class ColorRectItemView extends ItemView<Integer> {

		@FindViewById(R.id.color_rect)
		protected View mColorRect;

		public ColorRectItemView(Context context) {
			super(context);
		}

		@Override
		public void onResume() {
			mColorRect.setBackgroundColor(mDataProvider);
		}

	}

}

package cn.mutils.app.demo.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import cn.mutils.app.demo.R;
import cn.mutils.app.ui.StateView;
import cn.mutils.app.ui.adapter.HLinearAdapter;
import cn.mutils.app.ui.adapter.IItemView;
import cn.mutils.app.ui.adapter.ItemView;
import cn.mutils.core.annotation.res.FindViewById;
import cn.mutils.core.annotation.res.SetContentView;

@SetContentView(R.layout.view_hlinear_adapter)
public class HLinearAdapterDemoView extends StateView {

    @FindViewById(R.id.list)
    protected LinearLayout mList;

    protected MyAdapter mAdapter;

    public HLinearAdapterDemoView(Context context) {
        super(context);
    }

    public HLinearAdapterDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HLinearAdapterDemoView(Context context, AttributeSet attrs, int defStyle) {
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

    class MyAdapter extends HLinearAdapter<Integer> {

        @Override
        public IItemView<Integer> getItemView(int itemViewType) {
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

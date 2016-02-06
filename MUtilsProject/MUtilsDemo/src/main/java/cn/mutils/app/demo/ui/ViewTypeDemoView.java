package cn.mutils.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.mutils.app.core.annotation.res.FindViewById;
import cn.mutils.app.core.annotation.res.SetContentView;
import cn.mutils.app.demo.R;
import cn.mutils.app.ui.StateView;
import cn.mutils.app.ui.adapter.IItemView;
import cn.mutils.app.ui.adapter.ItemView;
import cn.mutils.app.ui.adapter.UIAdapter;

@SetContentView(R.layout.view_view_type)
public class ViewTypeDemoView extends StateView {

    @FindViewById(R.id.list)
    protected ListView mList;

    protected ViewTypeAdapter mAdapter;

    public ViewTypeDemoView(Context context) {
        super(context);
    }

    public ViewTypeDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewTypeDemoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAdapter = new ViewTypeAdapter();
        mList.setAdapter(mAdapter);
        initData();
    }

    protected void initData() {
        ArrayList<ViewTypeItem> data = new ArrayList<ViewTypeItem>();
        for (int i = 0; i < 50; i++) {
            ViewTypeItem item = new ViewTypeItem();
            item.type = Math.random() > 0.6 ? ViewTypeAdapter.TYPE_1 : ViewTypeAdapter.TYPE_2;
            item.title = "类型索引号[" + item.type + "]";
            data.add(item);
        }
        mAdapter.setDataProvider(data);
    }

    static class ViewTypeItem {
        public int type;
        public String title;
    }

    class ViewTypeAdapter extends UIAdapter<ViewTypeItem> {

        public static final int TYPE_1 = 0;
        public static final int TYPE_2 = 1;

        @Override
        public IItemView<ViewTypeItem> getItemView(int position) {
            IItemView<ViewTypeItem> itemView = null;
            int type = getItemViewType(position);
            switch (type) {
                case TYPE_1:
                    itemView = new ViewTypeView1(getContext());
                    break;
                case TYPE_2:
                    itemView = new ViewTypeView2(getContext());
                    break;
            }
            return itemView;
        }

        @Override
        public int getItemViewType(int position) {
            ViewTypeItem item = getItem(position);
            return item.type;
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

    }

    @SetContentView(R.layout.item_view_type_1)
    class ViewTypeView1 extends ItemView<ViewTypeItem> {

        @FindViewById(R.id.title)
        protected TextView mTitleText;

        public ViewTypeView1(Context context) {
            super(context);
        }

        @Override
        public void onResume() {
            mTitleText.setText((mPosition + 1) + ":" + mDataProvider.title);
        }

    }

    @SetContentView(R.layout.item_view_type_2)
    class ViewTypeView2 extends ItemView<ViewTypeItem> {

        @FindViewById(R.id.title)
        protected TextView mTitleText;

        public ViewTypeView2(Context context) {
            super(context);
        }

        @Override
        public void onResume() {
            mTitleText.setText((mPosition + 1) + ":" + mDataProvider.title);
        }

    }

}

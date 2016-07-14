package cn.mutils.app.demo.ui;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import cn.mutils.app.demo.R;
import cn.mutils.app.ui.StateView;
import cn.mutils.app.ui.adapter.IItemView;
import cn.mutils.app.ui.adapter.ItemView;
import cn.mutils.app.ui.adapter.RecyclerAdapter;
import cn.mutils.core.annotation.event.Click;
import cn.mutils.core.annotation.res.FindViewById;
import cn.mutils.core.annotation.res.SetContentView;

@SetContentView(R.layout.view_recycler_adapter)
public class RecyclerAdapterDemoView extends StateView {

    @FindViewById(R.id.recycler_view)
    protected RecyclerView mRecyclerView;


    protected MyAdapter mAdapter;

    public RecyclerAdapterDemoView(Context context) {
        super(context);
    }

    public RecyclerAdapterDemoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RecyclerAdapterDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new MyAdapter();
        ArrayList<MyItem> dataProvider = new ArrayList<MyItem>();
        for (int i = 0; i < 5; i++) {
            MyItem item = new MyItem();
            item.setTitle("No. " + (i + 1));
            item.setContent("Content for item " + (i + 1));
            dataProvider.add(item);
        }
        mAdapter.setDataProvider(dataProvider);
        mRecyclerView.setAdapter(mAdapter);
    }

    static class MyItem {

        protected String mTitle;

        protected String mContent;

        public String getTitle() {
            return mTitle;
        }

        public void setTitle(String title) {
            mTitle = title;
        }

        public String getContent() {
            return mContent;
        }

        public void setContent(String content) {
            mContent = content;
        }

    }

    class MyAdapter extends RecyclerAdapter<MyItem> {

        @Override
        public IItemView<MyItem> getItemView(int itemViewType) {
            return new MyItemView(getContext());
        }

    }

    @SetContentView(R.layout.view_my_item_view)
    class MyItemView extends ItemView<MyItem> {

        @FindViewById(R.id.title)
        protected TextView mTitleText;

        @FindViewById(R.id.content)
        protected TextView mConentText;

        @FindViewById(R.id.line)
        protected View mLineView;

        public MyItemView(Context context) {
            super(context);
        }

        @Override
        public void onResume() {
            if (mPosition == mAdapter.getCount() - 1) {
                mLineView.setVisibility(View.INVISIBLE);
            } else {
                mLineView.setVisibility(View.VISIBLE);
            }
            mTitleText.setText(mDataProvider.getTitle());
            mConentText.setText(mDataProvider.getContent());
        }

        @Click
        protected void onClick() {
            toast("You clicked item " + (mPosition + 1));
        }

    }


}

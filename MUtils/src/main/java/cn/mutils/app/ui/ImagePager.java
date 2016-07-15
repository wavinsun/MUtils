package cn.mutils.app.ui;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.lidroid.xutils.BitmapUtils;

import java.util.List;

import cn.mutils.core.IPhotoItem;

@SuppressWarnings({"UnnecessaryInterfaceModifier", "unused"})
public class ImagePager extends ViewPager {

    public static interface OnImageItemClickListener {

        void onItemClick(ImagePager pager, View v, int position, IPhotoItem item);

    }

    protected BitmapUtils mBitmapUtils;

    protected ImagePagerAdapter mAdapter;

    protected OnImageItemClickListener mOnImageItemClickListener;

    public ImagePager(Context context) {
        super(context);
        init(context, null);
    }

    public ImagePager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        mBitmapUtils = new BitmapUtils(context);
        mAdapter = new ImagePagerAdapter();
        super.setAdapter(mAdapter);
    }

    public int getCount() {
        return mAdapter.getCount();
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        throw new UnsupportedOperationException();
    }

    public void setDataProvider(List<? extends IPhotoItem> dataProvider) {
        mAdapter.setDataProvider(dataProvider);
        if (mAdapter.getCount() > 0) {
            super.setCurrentItem(0);
        }
    }

    public List<? extends IPhotoItem> getDataProvider() {
        return mAdapter.getDataProvider();
    }

    public void setOnImageItemClickListener(OnImageItemClickListener listener) {
        mOnImageItemClickListener = listener;
    }

    protected void onClickItem(View v, int position) {
        if (mOnImageItemClickListener == null) {
            return;
        }
        IPhotoItem item = mAdapter.getDataProvider().get(position);
        mOnImageItemClickListener.onItemClick(this, v, position, item);
    }

    class ImagePagerAdapter extends PagerAdapter {

        protected List<? extends IPhotoItem> mDataProvider;

        @Override
        public int getCount() {
            if (mDataProvider == null) {
                return 0;
            }
            return mDataProvider.size();
        }

        public List<? extends IPhotoItem> getDataProvider() {
            return mDataProvider;
        }

        public void setDataProvider(List<? extends IPhotoItem> dataProvider) {
            mDataProvider = dataProvider;
            notifyDataSetChanged();
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            ImageView imageView = new ImageView(getContext());
            imageView.setId(position);
            imageView.setScaleType(ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    onClickItem(v, v.getId());
                }
            });
            mBitmapUtils.display(imageView, mDataProvider.get(position).photoUrl());
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }
}

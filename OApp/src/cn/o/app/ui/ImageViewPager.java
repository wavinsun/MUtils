package cn.o.app.ui;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.lidroid.xutils.BitmapUtils;

public class ImageViewPager extends ViewPager {

	protected BitmapUtils mBitmapUtils;

	protected ImageViewPagerAdapter mAdapter;

	public ImageViewPager(Context context) {
		super(context);
		init(context, null);
	}

	public ImageViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	protected void init(Context context, AttributeSet attrs) {
		mBitmapUtils = new BitmapUtils(context);
		mAdapter = new ImageViewPagerAdapter();
		super.setAdapter(mAdapter);
	}

	public int getCount() {
		return mAdapter.getCount();
	}

	@Override
	public void setAdapter(PagerAdapter adapter) {
		throw new UnsupportedOperationException();
	}

	public void setDataProvider(List<String> dataProvider) {
		mAdapter.setDataProvider(dataProvider);
		if (mAdapter.getCount() > 0) {
			super.setCurrentItem(0);
		}
	}

	protected class ImageViewPagerAdapter extends PagerAdapter {

		protected List<String> mDataProvider;

		@Override
		public int getCount() {
			if (mDataProvider == null) {
				return 0;
			}
			return mDataProvider.size();
		}

		public void setDataProvider(List<String> dataProvider) {
			mDataProvider = dataProvider;
			notifyDataSetChanged();
		}

		@Override
		public View instantiateItem(ViewGroup container, int position) {
			ImageView imageView = new ImageView(getContext());
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageView.setLayoutParams(new ViewGroup.LayoutParams(
					ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT));
			mBitmapUtils.display(imageView, mDataProvider.get(position));
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

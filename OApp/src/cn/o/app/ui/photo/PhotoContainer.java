package cn.o.app.ui.photo;

import java.util.List;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.o.app.event.Dispatcher;
import cn.o.app.event.Listener;
import cn.o.app.event.listener.OBitmapLoadCallBack;
import cn.o.app.event.listener.OnSelectedChangeListener;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.BitmapDisplayConfig;
import com.lidroid.xutils.bitmap.callback.BitmapLoadFrom;

@SuppressWarnings("deprecation")
public class PhotoContainer extends RelativeLayout {
	protected final int BOTTOM_ID = 1;

	protected HackyViewPager mPager;

	protected View mBottom;

	protected TextView mIndicator;

	protected PhotoPagerAdapter mAdapter;

	protected BitmapUtils mBitmapUtils;

	protected Dispatcher mDispatcher;

	public PhotoContainer(Context context) {
		super(context);
		init(context, null);
	}

	public PhotoContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public PhotoContainer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	protected void init(Context context, AttributeSet attrs) {
		this.setBackgroundColor(0xFF000000);
		mDispatcher = new Dispatcher();

		mBitmapUtils = new BitmapUtils(context);
		mAdapter = new PhotoPagerAdapter();
		mPager = new HackyViewPager(context);
		mPager.setAdapter(mAdapter);
		mPager.setLayoutParams(new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT));
		mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				mIndicator.setText((position + 1) + "/" + mAdapter.getCount());
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
		this.addView(mPager);
		mBottom = new View(context);
		mBottom.setId(BOTTOM_ID);
		RelativeLayout.LayoutParams bottomParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT, 0);
		bottomParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		mBottom.setLayoutParams(bottomParams);
		this.addView(mBottom);
		GradientDrawable indicatorDrawable = new GradientDrawable();
		indicatorDrawable.setColor(0x88000000);
		indicatorDrawable.setCornerRadius(10);
		mIndicator = new TextView(context);
		mIndicator.setBackgroundDrawable(indicatorDrawable);
		mIndicator.setTextColor(0xFFFFFFFF);
		mIndicator.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
		mIndicator.setPadding(20, 8, 20, 8);
		mIndicator.setGravity(Gravity.CENTER);
		mIndicator.setVisibility(View.GONE);
		RelativeLayout.LayoutParams indicatorParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		indicatorParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
		indicatorParams.addRule(RelativeLayout.ABOVE, BOTTOM_ID);
		indicatorParams.setMargins(10, 10, 10, 10);
		mIndicator.setLayoutParams(indicatorParams);
		this.addView(mIndicator);
	}

	public void setOnSelectedChangeListener(OnSelectedChangeListener listener) {
		mDispatcher.setListener(listener);
	}

	public void setDataProvider(List<String> dataProvider) {
		setDataProvider(dataProvider, 0);
	}

	public void setDataProvider(List<String> dataProvider, int displayedIndex) {
		mAdapter.setDataProvider(dataProvider);
		int adapterCount = mAdapter.getCount();
		if (adapterCount > 0) {
			mIndicator.setVisibility(View.VISIBLE);
			if (displayedIndex >= 0 && displayedIndex < adapterCount) {
				mIndicator.setText((displayedIndex + 1) + "/" + adapterCount);
				mPager.setCurrentItem(displayedIndex);
			} else {
				mIndicator.setText("1/" + adapterCount);
				mPager.setCurrentItem(0);
			}
		} else {
			mIndicator.setVisibility(View.GONE);
		}
	}

	protected class PhotoPagerAdapter extends PagerAdapter {
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
			RelativeLayout layout = new RelativeLayout(getContext());
			final PhotoView photoView = new PhotoView(getContext());
			photoView.setTag(position);
			photoView
					.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {

						@Override
						public void onViewTap(View view, float x, float y) {
							Object o = view.getTag();
							if (!(o instanceof Integer)) {
								return;
							}
							Integer position = (Integer) o;
							Listener listener = mDispatcher.getListener();
							if (listener == null) {
								return;
							}
							((OnSelectedChangeListener) listener).onChanged(
									PhotoContainer.this, position);
						}
					});
			photoView.setLayoutParams(new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.MATCH_PARENT,
					RelativeLayout.LayoutParams.MATCH_PARENT));

			final ProgressBar progressBar = new ProgressBar(getContext(), null,
					android.R.attr.progressBarStyleLarge);
			progressBar.setVisibility(View.GONE);
			RelativeLayout.LayoutParams progressBarParams = new RelativeLayout.LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);
			progressBarParams.addRule(RelativeLayout.CENTER_IN_PARENT);
			progressBar.setLayoutParams(progressBarParams);

			mBitmapUtils.display(photoView, mDataProvider.get(position),
					new OBitmapLoadCallBack<View>() {

						@Override
						public void onLoadFailed(View container, String uri,
								Drawable drawable) {
							progressBar.setVisibility(View.GONE);
							super.onLoadFailed(container, uri, drawable);
						}

						@Override
						public void onLoadCompleted(View container, String uri,
								Bitmap bitmap, BitmapDisplayConfig config,
								BitmapLoadFrom from) {
							progressBar.setVisibility(View.GONE);
							DisplayMetrics displayMetrics = container
									.getContext().getResources()
									.getDisplayMetrics();
							if (bitmap.getWidth() < displayMetrics.widthPixels * 0.4
									&& bitmap.getHeight() < displayMetrics.widthPixels * 0.4) {
								photoView.setScaleType(ScaleType.CENTER_INSIDE);
							}
							super.onLoadCompleted(container, uri, bitmap,
									config, from);
						}

						@Override
						public void onLoading(View container, String uri,
								BitmapDisplayConfig config, long total,
								long current) {
							progressBar.setVisibility(View.VISIBLE);
							super.onLoading(container, uri, config, total,
									current);
						}

					});

			layout.addView(photoView);
			layout.addView(progressBar);
			container.addView(layout, ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.MATCH_PARENT);
			return layout;
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
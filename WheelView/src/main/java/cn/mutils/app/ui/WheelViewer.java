package cn.mutils.app.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.util.AttributeSet;
import cn.mutils.app.R;
import cn.mutils.app.util.AppUtil;
import kankan.wheel.widget.WheelView;

@SuppressWarnings("deprecation")
public class WheelViewer extends WheelView {

	public WheelViewer(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public WheelViewer(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public WheelViewer(Context context) {
		super(context);
		init(context, null);
	}

	protected void init(Context context, AttributeSet attrs) {
		Resources res = getResources();
		this.setCenterDrawable(res.getDrawable(R.drawable.wheel_val_light));
		this.setBackgroundResource(R.drawable.wheel_bg_light);

		GradientDrawable topShadow = new GradientDrawable(Orientation.TOP_BOTTOM, new int[] { 0x00000000, 0x00000000 });
		this.setTopShadowDrawable(topShadow);

		GradientDrawable bottomShadow = new GradientDrawable(Orientation.BOTTOM_TOP,
				new int[] { 0x00000000, 0x00000000 });
		this.setBottomShadowDrawable(bottomShadow);

		mTextColorValue = 0xFF000000;
		mTextColorItems = 0xFF888888;

		TEXT_SIZE = (int) AppUtil.sp2px(getContext(), 18);
		setFakeBoldText(false);
		setCyclic(true);
	}

}

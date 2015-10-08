package cn.o.app.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.util.AttributeSet;
import android.view.View;
import cn.o.app.AppUtil;
import cn.o.app.ui.core.IStatusBarOwner;

@SuppressLint("DrawAllocation")
public class StatusBox extends View {

	protected int mMeasuredHeight = -1;

	public StatusBox(Context context) {
		super(context);
	}

	public StatusBox(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public StatusBox(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		if (MeasureSpec.getMode(heightMeasureSpec) == MeasureSpec.AT_MOST) {
			if (mMeasuredHeight == -1) {
				Context context = getContext();
				if (context instanceof IStatusBarOwner) {
					if (!((IStatusBarOwner) context).isStatusBarTranslucent()) {
						mMeasuredHeight = 0;
					} else {
						mMeasuredHeight = AppUtil.getStatusBarHeight(context);
					}
				} else {
					if (VERSION.SDK_INT >= VERSION_CODES.KITKAT) {
						mMeasuredHeight = AppUtil.getStatusBarHeight(context);
					} else {
						mMeasuredHeight = 0;
					}
				}
			}
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMeasuredHeight, MeasureSpec.EXACTLY);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}

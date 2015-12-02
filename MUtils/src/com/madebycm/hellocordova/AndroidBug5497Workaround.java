package com.madebycm.hellocordova;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import cn.mutils.app.ui.core.IStatusBarOwner;
import cn.mutils.app.util.AppUtil;

@SuppressLint("NewApi")
public class AndroidBug5497Workaround {

	// For more information, see
	// https://code.google.com/p/android/issues/detail?id=5497
	// To use this class, simply invoke assistActivity() on an Activity that
	// already has its content view set.

	public static void assistActivity(Activity activity) {
		new AndroidBug5497Workaround(activity);
	}

	private View mChildOfContent;
	private int usableHeightPrevious;
	private FrameLayout.LayoutParams frameLayoutParams;
	private int mStatusBarHeightInContent;

	private AndroidBug5497Workaround(Activity activity) {
		FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
		mChildOfContent = content.getChildAt(0);
		mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
			public void onGlobalLayout() {
				possiblyResizeChildOfContent();
			}
		});
		// Fix bug for content android:fitsSystemWindows="false"
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			if (!mChildOfContent.getFitsSystemWindows()) {
				if (activity instanceof IStatusBarOwner) {
					if (((IStatusBarOwner) activity).isStatusBarTranslucent()) {
						mStatusBarHeightInContent = AppUtil.getStatusBarHeight(activity);
					}
				}
			}
		}
		frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
	}

	private void possiblyResizeChildOfContent() {
		int usableHeightNow = computeUsableHeight();
		if (usableHeightNow != usableHeightPrevious) {
			int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
			int heightDifference = usableHeightSansKeyboard - usableHeightNow;
			if (heightDifference > (usableHeightSansKeyboard / 4)) {
				// keyboard probably just became visible
				frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
			} else {
				// keyboard probably just became hidden
				frameLayoutParams.height = usableHeightSansKeyboard;
			}
			mChildOfContent.requestLayout();
			usableHeightPrevious = usableHeightNow;
		}
	}

	private int computeUsableHeight() {
		Rect r = new Rect();
		mChildOfContent.getWindowVisibleDisplayFrame(r);
		return (r.bottom - r.top) + mStatusBarHeightInContent;
	}

}
package cn.o.app.event.listener;

import android.graphics.drawable.Drawable;
import android.view.View;
import cn.o.app.event.Listener;
import cn.o.app.ui.core.IDefaultDrawableView;

import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;

public class OBitmapLoadCallBack<T extends View> extends
		DefaultBitmapLoadCallBack<T> implements Listener {

	// drawable是BitmapDisplayConfig中的loadFailedDrawable
	// 需要自定义请重写该方法
	@Override
	public void onLoadFailed(T container, String uri, Drawable drawable) {
		if (container instanceof IDefaultDrawableView) {
			IDefaultDrawableView v = (IDefaultDrawableView) container;
			if (v.getDefault() != null) {
				v.showDefault();
				return;
			}
		}
		this.setDrawable(container, drawable);
	}

}

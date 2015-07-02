package cn.o.app.event.listener;

import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;

import android.graphics.drawable.Drawable;
import android.view.View;
import cn.o.app.event.Listener;
import cn.o.app.ui.core.IDefaultDrawableView;

public class OBitmapLoadCallBack<T extends View> extends DefaultBitmapLoadCallBack<T>implements Listener {

	/**
	 * Subclass need to override this method
	 * 
	 * @param container
	 * @param uri
	 * @param drawable
	 *            loadFailedDrawable of BitmapDisplayConfig
	 */
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

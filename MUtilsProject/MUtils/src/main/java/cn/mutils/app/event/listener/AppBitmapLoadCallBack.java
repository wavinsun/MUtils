package cn.mutils.app.event.listener;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.bitmap.callback.BitmapLoadCallBack;
import com.lidroid.xutils.bitmap.callback.DefaultBitmapLoadCallBack;

import cn.mutils.app.ui.core.IDefaultDrawableView;
import cn.mutils.core.event.IListener;

/**
 * Call back for BitmapUtils
 *
 * @see BitmapUtils#display(View, String, BitmapLoadCallBack)
 */
public class AppBitmapLoadCallBack<T extends View> extends DefaultBitmapLoadCallBack<T> implements IListener {

    /**
     * Subclass need to override this method
     *
     * @param container View
     * @param uri       Uri
     * @param drawable  loadFailedDrawable of BitmapDisplayConfig
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

package cn.o.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;
import cn.o.app.annotation.res.FindViewById;
import cn.o.app.annotation.res.SetContentView;
import cn.o.app.demo.R;
import cn.o.app.ui.AMapView;
import cn.o.app.ui.StateView;

@SetContentView(R.layout.view_amap)
public class AMapDemoView extends StateView {

	@FindViewById(R.id.map)
	protected AMapView mMap;

	public AMapDemoView(Context context) {
		super(context);
	}

	public AMapDemoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public AMapDemoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mMap.lookAt(39.90846886, 116.45035744, "Beijing", "Yonganli");
	}

}

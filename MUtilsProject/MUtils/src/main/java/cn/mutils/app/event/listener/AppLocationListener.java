package cn.mutils.app.event.listener;

import cn.mutils.app.core.event.IListener;
import cn.mutils.app.lbs.AppLocation;

public interface AppLocationListener extends IListener {

	public void onLocationChanged(AppLocation location);

}

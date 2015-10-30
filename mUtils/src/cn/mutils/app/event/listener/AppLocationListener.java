package cn.mutils.app.event.listener;

import cn.mutils.app.core.event.Listener;
import cn.mutils.app.lbs.AppLocation;

public interface AppLocationListener extends Listener {

	public void onLocationChanged(AppLocation location);

}

package cn.o.app.event.listener;

import cn.o.app.core.event.Listener;
import cn.o.app.lbs.AppLocation;

public interface AppLocationListener extends Listener {

	public void onLocationChanged(AppLocation location);

}

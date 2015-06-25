package cn.o.app.event.listener;

import cn.o.app.event.Listener;
import cn.o.app.lbs.OLocation;

public interface OLocationListener extends Listener {
	public void onLocationChanged(OLocation location);
}

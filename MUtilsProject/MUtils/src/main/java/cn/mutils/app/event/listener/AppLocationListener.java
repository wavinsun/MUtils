package cn.mutils.app.event.listener;

import cn.mutils.app.core.event.IListener;
import cn.mutils.app.lbs.AppLocation;

public interface AppLocationListener extends IListener {

    void onLocationChanged(AppLocation location);

}

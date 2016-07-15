package cn.mutils.app.event.listener;

import cn.mutils.app.lbs.AppLocation;
import cn.mutils.core.event.IListener;

public interface AppLocationListener extends IListener {

    void onLocationChanged(AppLocation location);

}

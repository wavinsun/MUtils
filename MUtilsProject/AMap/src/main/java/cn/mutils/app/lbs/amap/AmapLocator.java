package cn.mutils.app.lbs.amap;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

import cn.mutils.app.core.event.Dispatcher;
import cn.mutils.app.core.task.IStoppable;
import cn.mutils.app.event.listener.AppLocationListener;
import cn.mutils.app.lbs.AppLocation;

/**
 * AMap Location
 */
@SuppressWarnings("unused")
public class AmapLocator implements IStoppable {

    protected boolean mStoped = true;

    protected Context mContext;

    protected LocationManagerProxy mProxy;

    protected Dispatcher mDispatcher = new Dispatcher();

    protected AMapLocationListener mAMapLocationListener = new AMapLocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onLocationChanged(Location location) {

        }

        @Override
        public void onLocationChanged(AMapLocation location) {
            AppLocationListener listener = (AppLocationListener) mDispatcher.getListener();
            if (listener == null) {
                return;
            }
            AppLocation loc = new AppLocation();
            loc.setAddress(location.getAddress());
            loc.setAltitude(location.getAltitude());
            loc.setCity(location.getCity());
            loc.setDistrict(location.getDistrict());
            loc.setLatitude(location.getLatitude());
            loc.setLongitude(location.getLongitude());
            loc.setProvince(location.getProvince());
            loc.setRadius(location.getAccuracy());
            loc.setStreet(location.getStreet());
            loc.setTime(location.getTime());
            listener.onLocationChanged(loc);
        }
    };

    public AmapLocator(Context context) {
        mContext = context;
        mProxy = LocationManagerProxy.getInstance(mContext);
        mProxy.setGpsEnable(true);
    }

    @Override
    public boolean isRunInBackground() {
        return true;
    }

    @Override
    public void setRunInBackground(boolean runInBackground) {

    }

    @Override
    public boolean isStopped() {
        return mStoped;
    }

    @Override
    public boolean stop() {
        if (mStoped) {
            return false;
        }
        mProxy.removeUpdates(mAMapLocationListener);
        mProxy.destroy();
        mStoped = true;
        return false;
    }

    public boolean start() {
        if (!mStoped) {
            return false;
        }
        mProxy.requestLocationData(LocationProviderProxy.AMapNetwork, 2000, 10, mAMapLocationListener);
        mStoped = false;
        return true;
    }

    public AppLocationListener getListener() {
        return mDispatcher.getListener(AppLocationListener.class);
    }

    public void setListener(AppLocationListener listener) {
        mDispatcher.setListener(listener);
    }

}

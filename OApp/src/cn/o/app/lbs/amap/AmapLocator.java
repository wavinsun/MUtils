package cn.o.app.lbs.amap;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import cn.o.app.core.event.Dispatcher;
import cn.o.app.core.task.IStopable;
import cn.o.app.event.listener.OLocationListener;
import cn.o.app.lbs.OLocation;

/**
 * AMap Location
 */
public class AmapLocator implements IStopable {

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
			OLocationListener listener = (OLocationListener) mDispatcher.getListener();
			if (listener == null) {
				return;
			}
			OLocation loc = new OLocation();
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
	public boolean isStoped() {
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

	public OLocationListener getListener() {
		return mDispatcher.getListener(OLocationListener.class);
	}

	public void setListener(OLocationListener listener) {
		mDispatcher.setListener(listener);
	}

}

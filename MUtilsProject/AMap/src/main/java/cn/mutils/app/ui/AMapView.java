package cn.mutils.app.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMapOptions;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.UiSettings;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;

import cn.mutils.app.amap.R;

@SuppressLint("InflateParams")
public class AMapView extends MapView {

    public AMapView(Context context) {
        super(context);
        init(context, null);
    }

    public AMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public AMapView(Context context, AMapOptions options) {
        super(context, options);
        init(context, null);
    }

    public AMapView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    protected void init(Context context, AttributeSet attrs) {
        this.onCreate(null);
        AMap map = this.getMap();
        UiSettings settings = map.getUiSettings();
        settings.setLogoPosition(AMapOptions.LOGO_POSITION_BOTTOM_LEFT);
        settings.setScaleControlsEnabled(true);// 比例尺
        settings.setZoomControlsEnabled(true);// 缩放
        settings.setCompassEnabled(true);// 指南针
        map.setInfoWindowAdapter(new InfoAdapter());
    }

    public void lookAt(double latitude, double longitude) {
        lookAt(latitude, longitude, null, null);
    }

    public void lookAt(double latitude, double longitude, String title) {
        lookAt(latitude, longitude, title, null);
    }

    public void lookAt(double latitude, double longitude, String title, String snippet) {
        LatLng latLng = new LatLng(latitude, longitude);
        AMap map = this.getMap();
        map.clear();
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(title).snippet(snippet)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        Marker marker = map.addMarker(markerOptions);
        if (title != null) {
            marker.showInfoWindow();
        }
        map.moveCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 15, 0, 0)));
    }

    class InfoAdapter implements InfoWindowAdapter {

        @Override
        public View getInfoWindow(Marker marker) {
            View v = LayoutInflater.from(getContext()).inflate(R.layout.amap_infowindow, null);
            TextView title = (TextView) v.findViewById(R.id.title);
            if (marker.getTitle() == null) {
                title.setText("");
            } else {
                title.setText(marker.getTitle());
            }
            TextView snippet = (TextView) v.findViewById(R.id.snippet);
            if (marker.getSnippet() == null) {
                snippet.setVisibility(View.GONE);
            } else {
                snippet.setText(marker.getSnippet());
            }
            return v;
        }

        @Override
        public View getInfoContents(Marker marker) {
            return null;
        }

    }
}

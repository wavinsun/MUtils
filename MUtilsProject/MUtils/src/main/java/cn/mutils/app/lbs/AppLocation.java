package cn.mutils.app.lbs;

import cn.mutils.app.core.INoProguard;
import cn.mutils.app.core.json.JsonUtil;

/**
 * Compatibility for AMap and Baidu Map
 */
@SuppressWarnings({"serial", "unused"})
public class AppLocation implements INoProguard {

    /**
     * 海拔
     */
    protected double mAltitude;

    /**
     * 维度
     */
    protected double mLatitude;

    /**
     * 经度
     */
    protected double mLongitude;

    /**
     * 省份
     */
    protected String mProvince;

    /**
     * 城市
     */
    protected String mCity;

    /**
     * 行政区
     */
    protected String mDistrict;

    /**
     * 街道
     */
    protected String mStreet;

    /**
     * 地址
     */
    protected String mAddress;

    /**
     * 时间
     */
    protected long mTime;

    /**
     * 精度
     */
    protected double mRadius;

    public AppLocation() {

    }

    public double getAltitude() {
        return mAltitude;
    }

    public void setAltitude(double value) {
        mAltitude = value;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double value) {
        mLatitude = value;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double value) {
        mLongitude = value;
    }

    public String getProvince() {
        return mProvince;
    }

    public void setProvince(String value) {
        mProvince = value;
    }

    public String getCity() {
        return mCity;
    }

    public void setCity(String value) {
        mCity = value;
    }

    public String getDistrict() {
        return mDistrict;
    }

    public void setDistrict(String value) {
        mDistrict = value;
    }

    public String getStreet() {
        return mStreet;
    }

    public void setStreet(String value) {
        mStreet = value;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String value) {
        mAddress = value;
    }

    public long getTime() {
        return mTime;
    }

    public void setTime(long value) {
        mTime = value;
    }

    public double getRadius() {
        return mRadius;
    }

    public void setRadius(double value) {
        mRadius = value;
    }

    @Override
    public String toString() {
        try {
            return JsonUtil.convert(this);
        } catch (Exception e) {
            return "";
        }
    }

}

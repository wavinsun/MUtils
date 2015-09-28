package cn.o.app.demo.net;

import android.content.Context;
import cn.o.app.core.INoProguard;
import cn.o.app.core.annotation.Format;
import cn.o.app.core.annotation.Name;
import cn.o.app.core.annotation.Primitive;
import cn.o.app.core.annotation.Primitive.PrimitiveType;
import cn.o.app.core.time.DateTime;
import cn.o.app.demo.net.WeatherTask.WeatherReq;
import cn.o.app.demo.net.WeatherTask.WeatherRes;

@SuppressWarnings("serial")
public class WeatherTask extends BasicTask<WeatherReq, WeatherRes> {

	public static class WeatherReq extends BasicRequest {

		protected String mCitypinyin;

		public String getCitypinyin() {
			return mCitypinyin;
		}

		public void setCitypinyin(String citypinyin) {
			mCitypinyin = citypinyin;
		}

	}

	public static class WeatherRes extends BasicResponse {

		protected WeatherRet mRetData;

		public WeatherRet getRetData() {
			return mRetData;
		}

		public void setRetData(WeatherRet retData) {
			mRetData = retData;
		}

	}

	public static class WeatherRet implements INoProguard {

		protected String mCity;

		protected String mPinyin;

		protected String mCitycode;

		protected DateTime mDate;

		protected DateTime mTime;

		protected String mPostCode;

		protected double mLongitude;

		protected double mLatitude;

		protected String mAltitude;

		protected String mWeather;

		protected String mTemp;

		protected String mL_tmp;

		protected String mH_tmp;

		protected String mWD;

		protected String mWS;

		protected String mSunrise;

		protected String mSunset;

		public String getCity() {
			return mCity;
		}

		public void setCity(String city) {
			mCity = city;
		}

		public String getPinyin() {
			return mPinyin;
		}

		public void setPinyin(String pinyin) {
			mPinyin = pinyin;
		}

		public String getCitycode() {
			return mCitycode;
		}

		public void setCitycode(String citycode) {
			mCitycode = citycode;
		}

		@Primitive(PrimitiveType.STRING)
		@Format("yy-MM-dd")
		public DateTime getDate() {
			return mDate;
		}

		public void setDate(DateTime date) {
			mDate = date;
		}

		@Primitive(PrimitiveType.STRING)
		@Format("HH:mm")
		public DateTime getTime() {
			return mTime;
		}

		public void setTime(DateTime time) {
			mTime = time;
		}

		public String getPostCode() {
			return mPostCode;
		}

		public void setPostCode(String postCode) {
			mPostCode = postCode;
		}

		public double getLongitude() {
			return mLongitude;
		}

		public void setLongitude(double longitude) {
			mLongitude = longitude;
		}

		public double getLatitude() {
			return mLatitude;
		}

		public void setLatitude(double latitude) {
			mLatitude = latitude;
		}

		public String getAltitude() {
			return mAltitude;
		}

		public void setAltitude(String altitude) {
			mAltitude = altitude;
		}

		public String getWeather() {
			return mWeather;
		}

		public void setWeather(String weather) {
			mWeather = weather;
		}

		public String getTemp() {
			return mTemp;
		}

		public void setTemp(String temp) {
			mTemp = temp;
		}

		public String getL_tmp() {
			return mL_tmp;
		}

		public void setL_tmp(String l_tmp) {
			mL_tmp = l_tmp;
		}

		public String getH_tmp() {
			return mH_tmp;
		}

		public void setH_tmp(String h_tmp) {
			mH_tmp = h_tmp;
		}

		@Name("WD")
		public String getWD() {
			return mWD;
		}

		public void setWD(String WD) {
			mWD = WD;
		}

		@Name("WD")
		public String getWS() {
			return mWS;
		}

		public void setWS(String WS) {
			mWS = WS;
		}

		public String getSunrise() {
			return mSunrise;
		}

		public void setSunrise(String sunrise) {
			mSunrise = sunrise;
		}

		public String getSunset() {
			return mSunset;
		}

		public void setSunset(String sunset) {
			mSunset = sunset;
		}

	}

	@Override
	public void setContext(Context context) {
		super.setContext(context);

		setUrl("http://apis.baidu.com/apistore/weatherservice/weather");
	}

	@Override
	protected void debugging(String event, String message) {
		super.debugging(event, message);
	}

}

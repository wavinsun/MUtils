package cn.mutils.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

import cn.mutils.app.core.annotation.res.FindViewById;
import cn.mutils.app.core.annotation.res.SetContentView;
import cn.mutils.app.demo.R;
import cn.mutils.app.demo.net.WeatherTask;
import cn.mutils.app.demo.net.WeatherTask.WeatherReq;
import cn.mutils.app.demo.net.WeatherTask.WeatherRes;
import cn.mutils.app.demo.net.WeatherTask.WeatherRet;
import cn.mutils.app.net.INetTask;
import cn.mutils.app.net.NetExceptionUtil;
import cn.mutils.app.net.NetTaskListener;
import cn.mutils.app.ui.StateView;

@SetContentView(R.layout.view_net_task)
public class NetTaskDemoView extends StateView {

    @FindViewById(R.id.log)
    protected TextView mLog;

    public NetTaskDemoView(Context context) {
        super(context);
    }

    public NetTaskDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NetTaskDemoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        getMainHandler().post(new Runnable() {

            @Override
            public void run() {
                loadData();
                getMainHandler().postDelayed(this, 2000);
            }

        });
    }

    protected void loadData() {
        WeatherReq req = new WeatherReq();
        req.setCitypinyin("beijing");
        final WeatherTask task = new WeatherTask();
        task.setRequest(req);
        task.addListener(new NetTaskListener<WeatherTask.WeatherReq, WeatherTask.WeatherRes>() {

            @Override
            public void onException(INetTask<WeatherReq, WeatherRes> task, Exception e) {
                if (NetExceptionUtil.handle(e, getToastOwner()) != null) {
                    mLog.setText("Load weather failed");
                }
            }

            @Override
            public void onComplete(INetTask<WeatherReq, WeatherRes> task, WeatherRes response) {
                StringBuilder sb = new StringBuilder();
                sb.append("Load weather success\n\n");
                WeatherRet ret = response.getRetData();
                sb.append("City: ");
                sb.append(ret.getCity());
                sb.append("\nCity pinyin: ");
                sb.append(ret.getPinyin());
                sb.append("\nCity code: ");
                sb.append(ret.getCitycode());
                sb.append("\nDate: ");
                ret.getDate().setFormat("yy-MM-dd");
                sb.append(ret.getDate().toString());
                sb.append("\nTime: ");
                ret.getTime().setFormat("HH:mm");
                sb.append(ret.getTime().toString());
                sb.append("\nPost Code: ");
                sb.append(ret.getPostCode());
                sb.append("\nLogitude: ");
                sb.append(ret.getLongitude());
                sb.append("\nLatitude: ");
                sb.append(ret.getLatitude());
                sb.append("\nAltitude: ");
                sb.append(ret.getAltitude());
                sb.append("\nWeather: ");
                sb.append(ret.getWeather());
                sb.append("\nTemperature: ");
                sb.append(ret.getTemp());
                sb.append("\nLowest Temperature: ");
                sb.append(ret.getL_tmp());
                sb.append("\nHighest Temperature: ");
                sb.append(ret.getH_tmp());
                sb.append("\nWind Direction: ");
                sb.append(ret.getWD());
                sb.append("\nWind Strength: ");
                sb.append(ret.getWS());
                sb.append("\nSunrise: ");
                sb.append(ret.getSunrise());
                sb.append("\nSunset: ");
                sb.append(ret.getSunset());
                mLog.setText(sb);
            }
        });
        add(task);
        getMainHandler().postDelayed(new Runnable() {

            @Override
            public void run() {
                task.stop();
            }

        }, 320);
        mLog.setText("Loading weather... ...");
    }


}

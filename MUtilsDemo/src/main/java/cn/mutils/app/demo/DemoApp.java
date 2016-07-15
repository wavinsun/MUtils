package cn.mutils.app.demo;

import cn.mutils.app.App;

public class DemoApp extends App {

    @Override
    public void onCreate() {
        App.setTencentAppId("1104746550");
        App.setWechatAppId("wx143c7417f8f7f690");
        super.onCreate();
    }

}

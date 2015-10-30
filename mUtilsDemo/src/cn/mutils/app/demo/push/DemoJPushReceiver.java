package cn.mutils.app.demo.push;

import cn.mutils.app.push.jpush.JPushMessageReceiver;

public class DemoJPushReceiver extends JPushMessageReceiver {

	public DemoJPushReceiver() {
		mManager.add(HelloWorldPushDispatcher.class);
	}

}

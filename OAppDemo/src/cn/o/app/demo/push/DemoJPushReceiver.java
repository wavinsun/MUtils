package cn.o.app.demo.push;

import cn.o.app.push.jpush.JPushMessageReceiver;

public class DemoJPushReceiver extends JPushMessageReceiver {

	public DemoJPushReceiver() {
		mManager.add(HelloWorldPushDispatcher.class);
	}

}

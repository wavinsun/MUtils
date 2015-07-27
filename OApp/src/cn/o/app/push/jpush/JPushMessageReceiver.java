package cn.o.app.push.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;
import cn.o.app.OUtil;
import cn.o.app.push.PushManager;

/**
 * JPush BroadcastReceiver of framework
 */
public class JPushMessageReceiver extends BroadcastReceiver {

	protected PushManager mManager = new PushManager();

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		if (bundle == null) {
			return;// Fix bug for java.lang.NullPointerException
		}
		String action = intent.getAction();
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(action)) {
			onRegistrationId(context, bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID));
		} else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(action)) {
			onMessage(context, bundle);
		} else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(action)) {
			onNotification(context, bundle);
		} else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(action)) {
			onNotificationOpened(context, bundle);
		}
	}

	protected void onRegistrationId(Context context, String regId) {
		// send the Registration Id to your server...
	}

	protected void onMessage(Context context, Bundle bundle) {
		mManager.setContext(context);
		mManager.onMessage(bundle.getString(JPushInterface.EXTRA_MESSAGE));
		mManager.setContext(null);
	}

	protected void onNotification(Context context, Bundle bundle) {

	}

	protected void onNotificationOpened(Context context, Bundle bundle) {
		OUtil.startApp(context);
	}

}

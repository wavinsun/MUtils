package cn.o.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import cn.jpush.android.api.JPushInterface;

public class JPushReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Bundle bundle = intent.getExtras();
		String action = intent.getAction();
		if (JPushInterface.ACTION_REGISTRATION_ID.equals(action)) {
			onRegistrationId(context,
					bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID));
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

	}

	protected void onNotification(Context context, Bundle bundle) {

	}

	protected void onNotificationOpened(Context context, Bundle bundle) {

	}

}

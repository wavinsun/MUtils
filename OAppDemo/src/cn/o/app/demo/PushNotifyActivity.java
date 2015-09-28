package cn.o.app.demo;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import cn.o.app.core.annotation.res.FindViewById;
import cn.o.app.core.annotation.res.SetContentView;
import cn.o.app.core.time.DateTime;
import cn.o.app.demo.push.HelloWorldPushDispatcher.HelloWorldMessage;
import cn.o.app.demo.ui.BasicActivity;
import cn.o.app.io.Extra;

@SuppressWarnings("serial")
@SetContentView(R.layout.activity_push_notify)
public class PushNotifyActivity extends BasicActivity {

	public static class DemoPushNotifyExtra extends Extra {

		protected HelloWorldMessage mMessage;

		public HelloWorldMessage getMessage() {
			return mMessage;
		}

		public void setMessage(HelloWorldMessage message) {
			mMessage = message;
		}
	}

	@FindViewById(R.id.log)
	protected TextView mLog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		showMessage(getIntent());
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		showMessage(intent);
	}

	protected void showMessage(Intent intent) {
		DemoPushNotifyExtra extra = new DemoPushNotifyExtra();
		if (extra.getFrom(this.getIntent())) {
			HelloWorldMessage msg = extra.getMessage();
			mLog.append(msg.getName());
			mLog.append("\n");
			mLog.append(msg.getMessage());
			mLog.append("\n");
			mLog.append(new DateTime().toString());
			mLog.append("\n");
		}
	}

}

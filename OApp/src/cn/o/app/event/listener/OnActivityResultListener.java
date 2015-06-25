package cn.o.app.event.listener;

import android.content.Context;
import android.content.Intent;
import cn.o.app.event.Listener;

public interface OnActivityResultListener extends Listener {
	public static final String EVENT_TYPE = "onActivityResult";

	public void onActivityResult(Context context, int requestCode,
			int resultCode, Intent data);
}

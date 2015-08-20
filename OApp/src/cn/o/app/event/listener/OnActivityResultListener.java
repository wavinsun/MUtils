package cn.o.app.event.listener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import cn.o.app.core.event.Listener;
import cn.o.app.ui.OActivity;
import cn.o.app.ui.OFragment;
import cn.o.app.ui.StateView;

/**
 * Listener for startActivityForResult
 * 
 * @see Activity#onActivityResult(int, int, Intent)
 * @see OActivity#onActivityResult(int, int, Intent)
 * @see OActivity#startActivityForResult(Intent, int)
 * @see StateView#startActivityForResult(Intent, int)
 * @see OFragment#startActivityForResult(Intent, int)
 */
public interface OnActivityResultListener extends Listener {

	/** Event type for onActivityResult */
	public static final String EVENT_TYPE = "onActivityResult";

	/**
	 * Call back
	 * 
	 * @param context
	 * @param requestCode
	 * @param resultCode
	 * @param data
	 */
	public void onActivityResult(Context context, int requestCode, int resultCode, Intent data);

}

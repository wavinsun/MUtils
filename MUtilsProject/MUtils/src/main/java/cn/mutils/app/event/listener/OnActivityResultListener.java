package cn.mutils.app.event.listener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import cn.mutils.app.core.event.IListener;
import cn.mutils.app.ui.AppActivity;
import cn.mutils.app.ui.Fragmenter;
import cn.mutils.app.ui.StateView;

/**
 * Listener for startActivityForResult
 *
 * @see Activity#onActivityResult(int, int, Intent)
 * @see AppActivity#onActivityResult(int, int, Intent)
 * @see AppActivity#startActivityForResult(Intent, int)
 * @see StateView#startActivityForResult(Intent, int)
 * @see Fragmenter#startActivityForResult(Intent, int)
 */
public interface OnActivityResultListener extends IListener {

    /**
     * Event type for onActivityResult
     */
    public static final String EVENT_TYPE = "onActivityResult";

    /**
     * Call back
     */
    public void onActivityResult(Context context, int requestCode, int resultCode, Intent data);

}

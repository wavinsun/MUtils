package cn.mutils.app.os;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import android.app.Activity;

public class AppActivityManager {

	protected static List<Activity> sActivitys;

	public static void redirectTo(Class<? extends Activity> activityCls) {
		if (sActivitys == null) {
			return;
		}
		boolean finishBehind = false;
		for (int i = 0; i < sActivitys.size(); i++) {
			Activity activity = sActivitys.get(i);
			if (finishBehind) {
				activity.finish();
				i--;
			} else {
				if (activityCls.isInstance(activity)) {
					finishBehind = true;
				}
			}
		}
	}

	public static void finishAll() {
		if (sActivitys == null) {
			return;
		}
		for (Activity activity : sActivitys) {
			activity.finish();
		}
		sActivitys.clear();
	}

	public static void attach(Activity activity) {
		if (sActivitys == null) {
			sActivitys = new CopyOnWriteArrayList<Activity>();
		} else {
			if (sActivitys.contains(activity)) {
				return;
			}
		}
		sActivitys.add(activity);
	}

	public static void detach(Activity activity) {
		if (sActivitys == null) {
			return;
		}
		sActivitys.remove(activity);
	}

}

package cn.o.app.os;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;

/**
 * Intent executor
 */
public class IntentExecutor {

	/** Application activity name */
	protected String mActivity;

	/** Application package name */
	protected String mPackageName;

	/** Application label */
	protected CharSequence mLabel;

	/** Application icon */
	protected Drawable mIcon;

	/** Intent to launch activity */
	protected Intent mIntent;

	public String getActivity() {
		return mActivity;
	}

	public void setActivity(String activity) {
		mActivity = activity;
	}

	public String getPackageName() {
		return mPackageName;
	}

	public void setPackageName(String packageName) {
		mPackageName = packageName;
	}

	public CharSequence getLabel() {
		return mLabel;
	}

	public void setLabel(CharSequence label) {
		mLabel = label;
	}

	public Drawable getIcon() {
		return mIcon;
	}

	public void setIcon(Drawable icon) {
		mIcon = icon;
	}

	public Intent getIntent() {
		return mIntent;
	}

	public void setIntent(Intent intent) {
		mIntent = intent;
	}

	public static List<IntentExecutor> queryActivities(Context context, Intent intent) {
		ArrayList<IntentExecutor> executors = new ArrayList<IntentExecutor>();
		PackageManager pm = context.getPackageManager();
		List<ResolveInfo> infos = pm.queryIntentActivities(intent, PackageManager.COMPONENT_ENABLED_STATE_DEFAULT);
		if (infos == null || infos.size() == 0) {
			return executors;
		}
		if (infos.size() > 1) {
			Collections.sort(infos, new ResolveInfo.DisplayNameComparator(pm));
		}
		for (ResolveInfo info : infos) {
			IntentExecutor executor = new IntentExecutor();
			executor.mActivity = info.activityInfo.name;
			executor.mPackageName = info.activityInfo.packageName;
			executor.mLabel = info.loadLabel(pm);
			executor.mIcon = info.loadIcon(pm);
			executor.mIntent = new Intent(intent);
			executor.mIntent.setClassName(executor.mPackageName, executor.mActivity);
			executors.add(executor);
		}
		return executors;
	}

}

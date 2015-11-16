package cn.mutils.app.open;

import java.util.ArrayList;
import java.util.List;

import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateConfig;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import android.content.Context;
import cn.mutils.app.App;
import cn.mutils.app.AppUtil;
import cn.mutils.app.core.build.Edition;
import cn.mutils.app.core.event.listener.VersionUpdateListener;
import cn.mutils.app.ui.AppActivity;
import cn.mutils.app.ui.core.IActivity;

public class UmengHelper {

	private static final int NEW_VERSION_STATE_UNKNOWN = -1;
	private static final int NEW_VERSION_STATE_NO = 0;
	private static final int NEW_VERSION_STATE_YES = 1;

	protected static Object sSync = new Object();
	protected static int sNewVersionState = NEW_VERSION_STATE_UNKNOWN;

	protected FeedbackAgent mFeedbackAgent;
	protected UmengUpdateListener mUmengUpdateListener;
	protected UmengDialogButtonListener mUmengDialogButtonListener;
	protected List<VersionUpdateListener> mVersionUpdateListeners;

	protected Context mContext;

	public UmengHelper(AppActivity context) {
		mContext = context;
	}

	public void onResume() {
		if (App.getApp() == null || !App.getApp().isUmengEnabled()) {
			return;
		}
		MobclickAgent.onResume(mContext);
	}

	public void onPause() {
		if (App.getApp() == null || !App.getApp().isUmengEnabled()) {
			return;
		}
		MobclickAgent.onPause(mContext);
	}

	public void onDestroy() {
		if (mContext instanceof IActivity) {
			if (((IActivity) mContext).isFinished()) {
				return;
			}
		}
		if (mVersionUpdateListeners != null) {
			mVersionUpdateListeners.clear();
		}
	}

	public void finish() {
		if (mVersionUpdateListeners != null) {
			mVersionUpdateListeners.clear();
		}
	}

	public boolean hasNewVersion() {
		if (sNewVersionState == NEW_VERSION_STATE_UNKNOWN) {
			checkNewVersion(null);
		}
		return sNewVersionState == NEW_VERSION_STATE_YES;
	}

	public void checkNewVersion(VersionUpdateListener listener) {
		if (App.getApp() == null || !App.getApp().isUmengEnabled()) {
			return;
		}
		if (listener != null) {
			if (mVersionUpdateListeners == null) {
				mVersionUpdateListeners = new ArrayList<VersionUpdateListener>();
			}
			mVersionUpdateListeners.add(listener);
		}
		UmengUpdateAgent.setDialogListener(null);
		if (this.mUmengUpdateListener == null) {
			this.mUmengUpdateListener = new UmengUpdateListener() {

				@Override
				public void onUpdateReturned(int statusCode, final UpdateResponse updateInfo) {
					boolean hasNewVersion = statusCode == UpdateStatus.Yes;
					synchronized (sSync) {
						if (hasNewVersion) {
							sNewVersionState = NEW_VERSION_STATE_YES;
						} else {
							sNewVersionState = NEW_VERSION_STATE_NO;
						}
					}
					if (mContext instanceof IActivity) {
						if (((IActivity) mContext).isFinished()) {
							return;
						}
					}
					if (mVersionUpdateListeners != null) {
						if (mVersionUpdateListeners.size() != 0) {
							boolean interceptDialog = false;
							for (VersionUpdateListener listener : mVersionUpdateListeners) {
								if (hasNewVersion) {
									boolean intercept = listener.onYes(updateInfo.version);
									interceptDialog = interceptDialog ? true : intercept;
								} else {
									listener.onNo();
								}
							}
							if (hasNewVersion && !interceptDialog) {
								if (mUmengDialogButtonListener == null) {
									mUmengDialogButtonListener = new UmengDialogButtonListener() {

										@Override
										public void onClick(int status) {
											if (mVersionUpdateListeners != null) {
												for (VersionUpdateListener listener : mVersionUpdateListeners) {
													switch (status) {
													case UpdateStatus.Update:
														listener.onUpdate(updateInfo.version);
														break;
													case UpdateStatus.Ignore:
													case UpdateStatus.NotNow:
														listener.onUpdateCancel(updateInfo.version);
														break;
													}
												}
												mVersionUpdateListeners.clear();
											}
										}
									};
								}
								UmengUpdateAgent.setDialogListener(mUmengDialogButtonListener);
								UmengUpdateAgent.showUpdateDialog(mContext, updateInfo);
							}
						}
					}
				}
			};
		}
		UmengUpdateAgent.setUpdateListener(this.mUmengUpdateListener);
		UmengUpdateAgent.forceUpdate(mContext);
	}

	public void feedback() {
		if (App.getApp() == null || !App.getApp().isUmengEnabled()) {
			return;
		}
		if (mFeedbackAgent == null) {
			mFeedbackAgent = new FeedbackAgent(mContext);
			mFeedbackAgent.sync();
		}
		mFeedbackAgent.startFeedbackActivity();
	}

	public static boolean isUmengEnabled(Context context) {
		return AppUtil.getAppMetaData(context, "UMENG_APPKEY") != null;
	}

	public static void initUmeng(Context context) {
		if (App.getApp() != null && App.getApp().getEdition() == Edition.DEBUG) {
			MobclickAgent.setDebugMode(true);
			MobclickAgent.setCatchUncaughtExceptions(false);
			UpdateConfig.setDebug(true);
		} else {
			UmengUpdateAgent.setUpdateCheckConfig(false);
		}
		// Fix bug for downloading always
		UmengUpdateAgent.setDeltaUpdate(false);
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.setUpdateUIStyle(UpdateStatus.STYLE_DIALOG);
		UmengUpdateAgent.setUpdateAutoPopup(false);
	}

}

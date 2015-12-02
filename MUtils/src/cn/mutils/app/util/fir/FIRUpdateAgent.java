package cn.mutils.app.util.fir;

import java.io.File;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.view.Gravity;
import cn.mutils.app.core.IClearable;
import cn.mutils.app.core.event.listener.VersionUpdateListener;
import cn.mutils.app.core.log.Logs;
import cn.mutils.app.core.text.MBFormat;
import cn.mutils.app.core.text.StringUtil;
import cn.mutils.app.net.INetTask;
import cn.mutils.app.net.NetTaskListener;
import cn.mutils.app.task.ContextOwnerTask;
import cn.mutils.app.ui.Alert;
import cn.mutils.app.ui.Alert.AlertListener;
import cn.mutils.app.util.AppUtil;
import cn.mutils.app.util.fir.FIRUpdateTask.FIRUpdateReq;
import cn.mutils.app.util.fir.FIRUpdateTask.FIRUpdateRes;

/**
 * Fly It Remotely Update Agent
 */
public class FIRUpdateAgent extends ContextOwnerTask implements IClearable {

	/** FIR download APK directory name */
	public static final String FIR_DIR = "FIR";

	/** FIR download APK file name for target version */
	public static final String FIR_APK = "fir.apk";

	/** FIR download APK state for target version */
	protected static Object sTargetVersionDownloading = null;

	/** Target version what you has known */
	protected String mTargetVersion;

	protected Alert mAlert;

	protected VersionUpdateListener mVersionUpdateListener;

	protected RequestCallBack<File> mDownloadCallBack;

	protected FIRUpdateTask mUpdateTask;

	public FIRUpdateAgent() {
		FIRUpdateReq req = new FIRUpdateReq();
		mUpdateTask = new FIRUpdateTask();
		mUpdateTask.setRequest(req);
		mUpdateTask.addListener(new FirUpdateTaskListener());
	}

	@Override
	protected void onStart() {
		mUpdateTask.setContext(mContext);
		mUpdateTask.start();
	}

	@Override
	protected void onStop() {
		mUpdateTask.stop();
		clear();
	}

	public void setListener(VersionUpdateListener listener) {
		mVersionUpdateListener = listener;
	}

	public void setDownloadCallBack(RequestCallBack<File> downloadCallBack) {
		mDownloadCallBack = downloadCallBack;
	}

	public void setBundleId(String bundleId) {
		if (mStarted || mStoped) {
			return;
		}
		mUpdateTask.getRequest().bundle_id = bundleId;
	}

	public void setApiToken(String apiToken) {
		if (mStarted || mStoped) {
			return;
		}
		mUpdateTask.getRequest().api_token = apiToken;
	}

	public void setTargetVersion(String targetVersion) {
		if (mStarted || mStoped) {
			return;
		}
		mTargetVersion = targetVersion;
	}

	/**
	 * Get download target version file
	 * 
	 * @return
	 */
	public File getTargetVersionFile() {
		if (mTargetVersion == null) {
			return null;
		}
		String file = AppUtil.getDiskCacheDir(mContext, FIR_DIR) + FIR_APK;
		try {
			if (!mTargetVersion.equals(AppUtil.getAppVersionName(mContext, file))) {
				return null;
			}
			return new File(file);
		} catch (Exception e) {
			return null;
		}
	}

	public void setAlert(Alert alert) {
		if (mStarted || mStoped) {
			return;
		}
		mAlert = alert;
	}

	public void clear() {
		mContext = null;
		mAlert = null;
		mVersionUpdateListener = null;
		mDownloadCallBack = null;
	}

	class FirUpdateTaskListener extends NetTaskListener<FIRUpdateTask.FIRUpdateReq, FIRUpdateTask.FIRUpdateRes> {

		@Override
		public void onException(INetTask<FIRUpdateReq, FIRUpdateRes> task, Exception e) {
			if (mVersionUpdateListener != null) {
				mVersionUpdateListener.onNo();
			}
		}

		@Override
		public void onComplete(INetTask<FIRUpdateReq, FIRUpdateRes> task, FIRUpdateRes response) {
			String version = AppUtil.getAppVersionName(mContext);
			String installUrl = response.installUrl;
			String versionShort = response.versionShort;
			FirUpdateAlertListener listener = new FirUpdateAlertListener();
			listener.setVersionShort(versionShort);
			listener.setInstallUrl(installUrl);
			if (mTargetVersion != null) {
				if (!mTargetVersion.equals(versionShort)) {
					if (mVersionUpdateListener != null) {
						mVersionUpdateListener.onNo();
					}
					clear();
					return;
				} else {
					if (mVersionUpdateListener != null) {
						if (mVersionUpdateListener.onYes(versionShort)) {
							clear();
							return;
						}
					}
					listener.onOK(null);
				}
				return;
			}
			if (StringUtil.compareVersion(version, versionShort) >= 0) {
				if (mVersionUpdateListener != null) {
					mVersionUpdateListener.onNo();
				}
				clear();
				return;
			}
			if (mVersionUpdateListener != null) {
				if (mVersionUpdateListener.onYes(versionShort)) {
					clear();
					return;
				}
			}
			StringBuilder sb = new StringBuilder();
			sb.append("最新版本:");
			sb.append(versionShort);
			if (response.binary != null) {
				sb.append("\n更新大小:");
				sb.append(MBFormat.format(response.binary.fsize / MBFormat.MILLION_SIZE));
			}
			sb.append("\n更新内容:\n");
			sb.append(response.changelog);
			Alert alert = mAlert != null ? mAlert : new Alert(mContext);
			alert.setTitle("发现新版本");
			alert.setMessage(sb.toString());
			alert.setOK("立即更新");
			alert.setCancel("以后再说");
			alert.setMessageGravity(Gravity.CENTER_VERTICAL);
			alert.setListener(listener);
			alert.show();
		}
	}

	class FirUpdateAlertListener extends AlertListener {

		protected String mVersionShort;

		protected String mInstallUrl;

		public void setVersionShort(String versionShort) {
			mVersionShort = versionShort;
		}

		public void setInstallUrl(String installUrl) {
			mInstallUrl = installUrl;
		}

		@Override
		public boolean onOK(Alert alert) {
			if (mVersionUpdateListener != null) {
				mVersionUpdateListener.onUpdate(mVersionShort);
			}
			StringBuilder sb = new StringBuilder();
			sb.append(AppUtil.getDiskCacheDir(mContext, FIR_DIR));
			sb.append(mVersionShort);
			sb.append(".apk");
			if (mTargetVersion == null) {
				new HttpUtils().download(mInstallUrl, sb.toString(), mDownloadCallBack);
				if (mDownloadCallBack == null) {
					clear();
				}
			} else {
				if (sTargetVersionDownloading == null) {
					sTargetVersionDownloading = new Object();
					new HttpUtils().download(mInstallUrl, sb.toString(), new FIRUpdateTargetVersionDownloadCallBack());
				}
			}
			return false;
		}

		@Override
		public boolean onCancel(Alert alert) {
			if (mVersionUpdateListener != null) {
				mVersionUpdateListener.onUpdateCancel(mVersionShort);
			}
			clear();
			return false;
		}
	}

	class FIRUpdateTargetVersionDownloadCallBack extends RequestCallBack<File> {

		@Override
		public void onSuccess(ResponseInfo<File> responseInfo) {
			Logs.i("FIRUpdateAgent", "Download target version success");
			sTargetVersionDownloading = null;
			File renameTo = new File(AppUtil.getDiskCacheDir(mContext, FIR_DIR) + FIR_APK);
			if (renameTo.exists()) {
				renameTo.delete();
			}
			responseInfo.result.renameTo(renameTo);
			clear();
		}

		@Override
		public void onFailure(HttpException error, String msg) {
			Logs.e("FIRUpdateAgent", "Download target version failure");
			sTargetVersionDownloading = null;
			File file = new File(AppUtil.getDiskCacheDir(mContext, FIR_DIR) + mTargetVersion + ".apk");
			if (file.exists()) {
				file.delete();
			}
			clear();
		}

	}

}

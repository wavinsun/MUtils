package cn.mutils.app.util.fir;

import android.view.Gravity;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.io.File;

import cn.mutils.app.net.INetTask;
import cn.mutils.app.net.NetTaskListener;
import cn.mutils.app.task.ContextOwnerTask;
import cn.mutils.app.ui.Alert;
import cn.mutils.app.ui.Alert.AlertListener;
import cn.mutils.app.util.AppUtil;
import cn.mutils.app.util.fir.FIRUpdateTask.FIRUpdateReq;
import cn.mutils.app.util.fir.FIRUpdateTask.FIRUpdateRes;
import cn.mutils.core.IClearable;
import cn.mutils.core.event.listener.VersionUpdateListener;
import cn.mutils.core.log.Logs;
import cn.mutils.core.text.MBFormat;
import cn.mutils.core.text.StringUtil;

/**
 * Fly It Remotely Update Agent
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "StringBufferReplaceableByString"})
public class FIRUpdateAgent extends ContextOwnerTask implements IClearable {

    /**
     * FIR download APK directory name
     */
    public static final String FIR_DIR = "FIR";

    /**
     * FIR download APK file name for target version
     */
    public static final String FIR_APK = "fir.apk";

    protected HttpHandler<File> mDownloadHandler;

    protected String mDownloadPath;

    protected String mDownloadPathOfTargetVersion;

    /**
     * FIR download APK state for target version
     */
    protected static Object sTargetVersionDownloading = null;

    /**
     * Target version what you has known
     */
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
        if (mDownloadHandler != null) {
            if (mTargetVersion == null) {
                mDownloadHandler.cancel();
                mDownloadHandler = null;
            }
        }
        clear();
    }

    public void setListener(VersionUpdateListener listener) {
        mVersionUpdateListener = listener;
    }

    public void setDownloadCallBack(RequestCallBack<File> downloadCallBack) {
        mDownloadCallBack = downloadCallBack;
    }

    public void setBundleId(String bundleId) {
        if (mStarted || mStopped) {
            return;
        }
        mUpdateTask.getRequest().bundle_id = bundleId;
    }

    public void setApiToken(String apiToken) {
        if (mStarted || mStopped) {
            return;
        }
        mUpdateTask.getRequest().api_token = apiToken;
    }

    public void setTargetVersion(String targetVersion) {
        if (mStarted || mStopped) {
            return;
        }
        mTargetVersion = targetVersion;
    }

    /**
     * Get download target version file
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
        if (mStarted || mStopped) {
            return;
        }
        mAlert = alert;
    }

    public void clear() {
        if (mTargetVersion == null) {
            mDownloadPath = null;
            mDownloadPathOfTargetVersion = null;
        }
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
                mDownloadPath = sb.toString();
                mDownloadHandler = new HttpUtils().download(mInstallUrl, mDownloadPath, mDownloadCallBack);
                if (mDownloadCallBack == null) {
                    clear();
                }
            } else {
                if (sTargetVersionDownloading == null) {
                    sTargetVersionDownloading = new Object();
                    mDownloadPathOfTargetVersion = AppUtil.getDiskCacheDir(mContext, FIR_DIR) + FIR_APK;
                    mDownloadPath = sb.toString();
                    mDownloadHandler = new HttpUtils().download(mInstallUrl, mDownloadPath, new FIRUpdateTargetVersionDownloadCallBack());
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
            File renameTo = new File(mDownloadPathOfTargetVersion);
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
            File file = new File(mDownloadPath);
            if (file.exists()) {
                file.delete();
            }
            clear();
        }

        @Override
        public void onCancelled() {
            Logs.e("FIRUpdateAgent", "Download target version cancelled");
            if (mDownloadPath != null) {
                sTargetVersionDownloading = null;
            }
        }

    }

}

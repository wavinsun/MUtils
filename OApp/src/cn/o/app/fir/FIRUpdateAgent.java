package cn.o.app.fir;

import java.io.File;

import android.view.Gravity;
import cn.o.app.LogCat;
import cn.o.app.OUtil;
import cn.o.app.event.listener.VersionUpdateListener;
import cn.o.app.fir.FIRUpdateTask.FIRUpdateReq;
import cn.o.app.fir.FIRUpdateTask.FIRUpdateRes;
import cn.o.app.net.INetTask;
import cn.o.app.net.NetTaskListener;
import cn.o.app.task.ContextOwnerTask;
import cn.o.app.text.StringUtil;
import cn.o.app.ui.Alert;
import cn.o.app.ui.Alert.AlertListener;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.callback.RequestCallBack;

// Fly It Remotely 更新代理
public class FIRUpdateAgent extends ContextOwnerTask {

	protected VersionUpdateListener mVersionUpdateListener;

	protected RequestCallBack<File> mDownloadCallBack;

	protected FIRUpdateTask mUpdateTask;

	public FIRUpdateAgent() {
		FIRUpdateReq req = new FIRUpdateReq();
		mUpdateTask = new FIRUpdateTask();
		mUpdateTask.setRequest(req);
		mUpdateTask
				.addListener(new NetTaskListener<FIRUpdateTask.FIRUpdateReq, FIRUpdateTask.FIRUpdateRes>() {

					@Override
					public void onException(
							INetTask<FIRUpdateReq, FIRUpdateRes> task,
							Exception e) {
						LogCat.e("FIRUpdateAgent", e.getMessage());
						if (mVersionUpdateListener != null) {
							mVersionUpdateListener.onNo();
						}
						FIRUpdateAgent.this.stop();
					}

					@Override
					public void onComplete(
							INetTask<FIRUpdateReq, FIRUpdateRes> task,
							FIRUpdateRes response) {
						FIRUpdateAgent.this.stop();
						String version = OUtil.getAppVersionName(mContext);
						final String installUrl = response.installUrl;
						final String versionShort = response.versionShort;
						if (StringUtil.compareVersion(version, versionShort) >= 0) {
							if (mVersionUpdateListener != null) {
								mVersionUpdateListener.onNo();
							}
							return;
						}
						if (mVersionUpdateListener != null) {
							if (mVersionUpdateListener.onYes(versionShort)) {
								return;
							}
						}
						StringBuilder sb = new StringBuilder();
						sb.append("最新版本:");
						sb.append(versionShort);
						sb.append("\n更新内容:\n");
						sb.append(response.changelog);
						Alert alert = new Alert(mContext);
						alert.setTitle("发现新版本");
						alert.setMessage(sb.toString());
						alert.setOK("立即更新");
						alert.setCancel("以后再说");
						alert.setMessageGravity(Gravity.CENTER_VERTICAL);
						alert.setListener(new AlertListener() {

							@Override
							public boolean onOK(Alert alert) {
								if (mVersionUpdateListener != null) {
									mVersionUpdateListener
											.onUpdate(versionShort);
								}
								StringBuilder sb = new StringBuilder();
								sb.append(OUtil
										.getDiskCacheDir(mContext, "FIR"));
								sb.append(versionShort);
								sb.append(".apk");
								new HttpUtils().download(installUrl,
										sb.toString(), mDownloadCallBack);
								return false;
							}

							@Override
							public boolean onCancel(Alert alert) {
								if (mVersionUpdateListener != null) {
									mVersionUpdateListener
											.onUpdateCancel(versionShort);
								}
								return false;
							}
						});
						alert.show();
					}
				});
	}

	@Override
	protected void onStart() {
		mUpdateTask.setContext(mContext);
		mUpdateTask.start();
	}

	@Override
	protected void onStop() {
		mUpdateTask.stop();
	}

	public void setListener(VersionUpdateListener listener) {
		mVersionUpdateListener = listener;
	}

	public void setDownloadCallBack(RequestCallBack<File> downloadCallBack) {
		mDownloadCallBack = downloadCallBack;
	}

	public void setIdOrAppid(String idOrAppid) {
		if (mStarted || mStoped) {
			return;
		}
		mUpdateTask.getRequest().idOrAppid = idOrAppid;
	}

}

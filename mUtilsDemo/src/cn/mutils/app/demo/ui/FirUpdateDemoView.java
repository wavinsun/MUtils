package cn.mutils.app.demo.ui;

import java.io.File;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.content.Context;
import android.util.AttributeSet;
import cn.mutils.app.AppUtil;
import cn.mutils.app.core.annotation.event.OnClick;
import cn.mutils.app.core.annotation.res.SetContentView;
import cn.mutils.app.core.event.listener.VersionUpdateListener;
import cn.mutils.app.core.text.MBFormat;
import cn.mutils.app.demo.R;
import cn.mutils.app.fir.FIRUpdateAgent;
import cn.mutils.app.ui.StateView;

@SetContentView(R.layout.view_fir_update)
public class FirUpdateDemoView extends StateView {

	public FirUpdateDemoView(Context context) {
		super(context);
	}

	public FirUpdateDemoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FirUpdateDemoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@OnClick(R.id.go)
	protected void onClickGo() {
		FIRUpdateAgent agent = new FIRUpdateAgent();
		agent.setContext(getContext());
		agent.setBundleId("cn.mutils.app.demo");
		agent.setApiToken("4dce255bc03f5809f5e6f0463c2761c8");
		agent.setDownloadCallBack(new RequestCallBack<File>() {

			@Override
			public void onSuccess(ResponseInfo<File> responseInfo) {
				AppUtil.installApp(getContext(), responseInfo.result);
			}

			@Override
			public void onFailure(HttpException e, String message) {
				toast("Download failure");
			}

			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				double totalMB = total;
				totalMB = totalMB / MBFormat.MILLION_SIZE;
				double currentMB = current;
				currentMB = currentMB / MBFormat.MILLION_SIZE;
				StringBuilder sb = new StringBuilder();
				sb.append("Updating:");
				sb.append(MBFormat.format(currentMB, "0.00"));
				sb.append("/");
				sb.append(MBFormat.format(totalMB, "0.00"));
				toast(sb.toString());
			}

		});
		agent.setListener(new VersionUpdateListener() {

			@Override
			public boolean onYes(String version) {
				toast("New version found");
				return false;
			}

			@Override
			public void onUpdateCancel(String version) {
				toast("Update cancel");
			}

			@Override
			public void onUpdate(String version) {
				toast("Update");
			}

			@Override
			public void onNo() {
				toast("No new version found");
			}
		});
		agent.start();
	}

	@OnClick(R.id.target_version_download)
	protected void onClickTargetVersionDownload() {
		FIRUpdateAgent agent = new FIRUpdateAgent();
		agent.setContext(getContext());
		agent.setBundleId("cn.mutils.app.demo");
		agent.setApiToken("4dce255bc03f5809f5e6f0463c2761c8");
		agent.setTargetVersion("1.1");
		agent.start();
	}

	@OnClick(R.id.target_version_install)
	protected void onClickTargetVersionInstall() {
		FIRUpdateAgent agent = new FIRUpdateAgent();
		agent.setContext(getContext());
		agent.setBundleId("cn.mutils.app.demo");
		agent.setApiToken("4dce255bc03f5809f5e6f0463c2761c8");
		agent.setTargetVersion("1.1");
		if (!AppUtil.installApp(getContext(), agent.getTargetVersionFile())) {
			toast("Install failed");
		}
	}

}

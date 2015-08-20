package cn.o.app.demo.ui;

import java.io.File;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.content.Context;
import android.util.AttributeSet;
import cn.o.app.OUtil;
import cn.o.app.core.annotation.event.OnClick;
import cn.o.app.core.annotation.res.SetContentView;
import cn.o.app.core.event.listener.VersionUpdateListener;
import cn.o.app.core.text.MBFormat;
import cn.o.app.demo.R;
import cn.o.app.fir.FIRUpdateAgent;
import cn.o.app.ui.StateView;

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
		agent.setBundleId("cn.o.app.demo");
		agent.setApiToken("75d00450e48d4beee3d4a37fe663bc6e");
		agent.setDownloadCallBack(new RequestCallBack<File>() {

			@Override
			public void onSuccess(ResponseInfo<File> responseInfo) {
				OUtil.installApp(getContext(), responseInfo.result);
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

}

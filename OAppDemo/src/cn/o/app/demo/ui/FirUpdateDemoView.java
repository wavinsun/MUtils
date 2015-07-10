package cn.o.app.demo.ui;

import java.io.File;

import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import android.content.Context;
import android.util.AttributeSet;
import cn.o.app.OUtil;
import cn.o.app.annotation.event.OnClick;
import cn.o.app.annotation.res.SetContentView;
import cn.o.app.demo.R;
import cn.o.app.event.listener.VersionUpdateListener;
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
		agent.setIdOrAppid("5592504febc7b8f777002c98");
		agent.setDownloadCallBack(new RequestCallBack<File>() {

			@Override
			public void onSuccess(ResponseInfo<File> responseInfo) {
				OUtil.installApp(getContext(), responseInfo.result);
			}

			@Override
			public void onFailure(HttpException e, String message) {
				toast("Download failure");
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

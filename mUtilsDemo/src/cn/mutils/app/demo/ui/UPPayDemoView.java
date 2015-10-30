package cn.mutils.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;
import cn.mutils.app.core.annotation.event.OnClick;
import cn.mutils.app.core.annotation.res.SetContentView;
import cn.mutils.app.demo.R;
import cn.mutils.app.demo.net.GetTNTask;
import cn.mutils.app.net.INetTask;
import cn.mutils.app.net.NetTaskListener;
import cn.mutils.app.pay.AppPayListener;
import cn.mutils.app.pay.AppPayTask;
import cn.mutils.app.pay.UPPayTask;
import cn.mutils.app.ui.Alert;
import cn.mutils.app.ui.StateView;

@SetContentView(R.layout.view_uppay)
public class UPPayDemoView extends StateView {

	public UPPayDemoView(Context context) {
		super(context);
	}

	public UPPayDemoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public UPPayDemoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@OnClick(R.id.go)
	protected void onClickGo() {
		GetTNTask task = new GetTNTask();
		task.addListener(new NetTaskListener<String, String>() {

			@Override
			public void onException(INetTask<String, String> task, Exception e) {

			}

			@Override
			public void onComplete(INetTask<String, String> task, String response) {
				pay(response);
			}
		});
		add(task);
	}

	protected void pay(String tn) {
		UPPayTask payTask = new UPPayTask();
		payTask.setContext(getContext());
		payTask.setDebug(true);
		payTask.setTradeNo(tn);
		payTask.addListener(new AppPayListener() {

			@Override
			public void onError(AppPayTask task, Exception e) {
				Alert alert = new Alert(getContext());
				alert.setTitle("error");
				alert.show();
			}

			@Override
			public void onComplete(AppPayTask task) {
				Alert alert = new Alert(getContext());
				alert.setTitle("complete");
				alert.show();
			}
		});
		payTask.start();
	}

}

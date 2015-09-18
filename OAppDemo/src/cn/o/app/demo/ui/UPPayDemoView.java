package cn.o.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;
import cn.o.app.core.annotation.event.OnClick;
import cn.o.app.core.annotation.res.SetContentView;
import cn.o.app.demo.R;
import cn.o.app.demo.net.GetTNTask;
import cn.o.app.net.INetTask;
import cn.o.app.net.NetTaskListener;
import cn.o.app.pay.OPayListener;
import cn.o.app.pay.OPayTask;
import cn.o.app.pay.UPPayTask;
import cn.o.app.ui.Alert;
import cn.o.app.ui.StateView;

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
		payTask.addListener(new OPayListener() {

			@Override
			public void onError(OPayTask task, Exception e) {
				Alert alert = new Alert(getContext());
				alert.setTitle("error");
				alert.show();
			}

			@Override
			public void onComplete(OPayTask task) {
				Alert alert = new Alert(getContext());
				alert.setTitle("complete");
				alert.show();
			}
		});
		payTask.start();
	}

}

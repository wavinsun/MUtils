package cn.o.app.demo;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import cn.o.app.annotation.event.OnClick;
import cn.o.app.annotation.res.SetContentView;
import cn.o.app.demo.ShellActivity.ShellExtra;
import cn.o.app.demo.ui.AESDemoView;
import cn.o.app.demo.ui.AlertDemoView;
import cn.o.app.demo.ui.AnimTaskDemoView;
import cn.o.app.demo.ui.BasicActivity;
import cn.o.app.demo.ui.ConfItemDemoView;
import cn.o.app.demo.ui.FirUpdateDemoView;
import cn.o.app.demo.ui.OAdapterDemoView;
import cn.o.app.demo.ui.ZipDemoView;

@SetContentView(R.layout.activity_main)
public class MainActivity extends BasicActivity {

	@OnClick(R.id.alert_by_activity)
	protected void onClickAlertByActivity() {
		startActivity(new Intent(this, AlertDemoActivity.class));
	}

	@OnClick(R.id.alert)
	protected void onClickAlert(View v) {
		Intent intent = new Intent(this, ShellActivity.class);
		ShellExtra extra = new ShellExtra();
		if (v instanceof TextView) {
			extra.setTitle(((TextView) v).getText().toString());
		}
		extra.setViewName(AlertDemoView.class.getName());
		extra.putTo(intent);
		startActivity(intent);
	}

	@OnClick({ R.id.oadapter, R.id.zip, R.id.conf_item, R.id.aes, R.id.fir, R.id.anim_task })
	protected void onClick(View v) {
		Intent intent = new Intent(this, ShellActivity.class);
		ShellExtra extra = new ShellExtra();
		if (v instanceof TextView) {
			extra.setTitle(((TextView) v).getText().toString());
		}
		switch (v.getId()) {
		case R.id.oadapter:
			extra.setViewName(OAdapterDemoView.class.getName());
			break;
		case R.id.zip:
			extra.setViewName(ZipDemoView.class.getName());
			break;
		case R.id.conf_item:
			extra.setViewName(ConfItemDemoView.class.getName());
			break;
		case R.id.aes:
			extra.setViewName(AESDemoView.class.getName());
			break;
		case R.id.fir:
			extra.setViewName(FirUpdateDemoView.class.getName());
			break;
		case R.id.anim_task:
			extra.setViewName(AnimTaskDemoView.class.getName());
			break;
		default:
			break;
		}
		extra.putTo(intent);
		startActivity(intent);
	}
}

package cn.o.app.demo;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import cn.o.app.annotation.event.OnClick;
import cn.o.app.annotation.res.SetContentView;
import cn.o.app.demo.ShellActivity.ShellExtra;
import cn.o.app.demo.ui.AlertView;
import cn.o.app.demo.ui.BasicActivity;

@SetContentView(R.layout.activity_main)
public class MainActivity extends BasicActivity {

	@OnClick(R.id.alert_by_activity)
	protected void onClickAlertByActivity() {
		startActivity(new Intent(this, AlertActivity.class));
	}

	@OnClick(R.id.alert)
	protected void onClickAlert(View v) {
		Intent intent = new Intent(this, ShellActivity.class);
		ShellExtra extra = new ShellExtra();
		if (v instanceof TextView) {
			extra.setTitle(((TextView) v).getText().toString());
		}
		extra.setViewName(AlertView.class.getName());
		extra.putTo(intent);
		startActivity(intent);
	}
}

package cn.o.app.demo;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import cn.o.app.annotation.event.OnClick;
import cn.o.app.annotation.res.SetContentView;
import cn.o.app.demo.ShellActivity.ShellExtra;
import cn.o.app.demo.net.PayDemoView;
import cn.o.app.demo.ui.AESDemoView;
import cn.o.app.demo.ui.AMapDemoView;
import cn.o.app.demo.ui.ActionSheetDemoView;
import cn.o.app.demo.ui.AlertDemoView;
import cn.o.app.demo.ui.AnimTaskDemoView;
import cn.o.app.demo.ui.BasicActivity;
import cn.o.app.demo.ui.BusyDemoView;
import cn.o.app.demo.ui.ConfItemDemoView;
import cn.o.app.demo.ui.DatePickerDemoView;
import cn.o.app.demo.ui.FirUpdateDemoView;
import cn.o.app.demo.ui.MediaDemoView;
import cn.o.app.demo.ui.NetTaskDemoView;
import cn.o.app.demo.ui.OAdapterDemoView;
import cn.o.app.demo.ui.PromptDemoView;
import cn.o.app.demo.ui.QRCodeDemoView;
import cn.o.app.demo.ui.RoundProgressBarDemoView;
import cn.o.app.demo.ui.ShareDemoView;
import cn.o.app.demo.ui.TipViewDemoView;
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

	@OnClick({ R.id.oadapter, R.id.zip, R.id.conf_item, R.id.aes, R.id.fir, R.id.anim_task, R.id.media, R.id.net_task,
			R.id.pay, R.id.qrcode, R.id.share, R.id.action_sheet, R.id.amap, R.id.date_picker, R.id.prompt,
			R.id.round_progress_bar, R.id.tip_view, R.id.busy })
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
		case R.id.media:
			extra.setViewName(MediaDemoView.class.getName());
			break;
		case R.id.net_task:
			extra.setViewName(NetTaskDemoView.class.getName());
			break;
		case R.id.pay:
			extra.setViewName(PayDemoView.class.getName());
			break;
		case R.id.qrcode:
			extra.setViewName(QRCodeDemoView.class.getName());
			break;
		case R.id.share:
			extra.setViewName(ShareDemoView.class.getName());
			break;
		case R.id.action_sheet:
			extra.setViewName(ActionSheetDemoView.class.getName());
			break;
		case R.id.amap:
			extra.setViewName(AMapDemoView.class.getName());
			break;
		case R.id.date_picker:
			extra.setViewName(DatePickerDemoView.class.getName());
			break;
		case R.id.prompt:
			extra.setViewName(PromptDemoView.class.getName());
			break;
		case R.id.round_progress_bar:
			extra.setViewName(RoundProgressBarDemoView.class.getName());
			break;
		case R.id.tip_view:
			extra.setViewName(TipViewDemoView.class.getName());
			break;
		case R.id.busy:
			extra.setViewName(BusyDemoView.class.getName());
			break;
		default:
			break;
		}
		extra.putTo(intent);
		startActivity(intent);
	}
}

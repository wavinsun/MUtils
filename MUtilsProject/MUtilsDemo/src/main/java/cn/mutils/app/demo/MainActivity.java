package cn.mutils.app.demo;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import cn.mutils.app.core.annotation.event.OnClick;
import cn.mutils.app.core.annotation.res.SetContentView;
import cn.mutils.app.demo.ShellActivity.ShellExtra;
import cn.mutils.app.demo.ui.AESDemoView;
import cn.mutils.app.demo.ui.AMapDemoView;
import cn.mutils.app.demo.ui.ActionSheetDemoView;
import cn.mutils.app.demo.ui.AlertDemoView;
import cn.mutils.app.demo.ui.AnimTaskDemoView;
import cn.mutils.app.demo.ui.BasicActivity;
import cn.mutils.app.demo.ui.BusyDemoView;
import cn.mutils.app.demo.ui.ChannelDemoView;
import cn.mutils.app.demo.ui.ConfItemDemoView;
import cn.mutils.app.demo.ui.DateChooserDemoView;
import cn.mutils.app.demo.ui.DemoPathButtonView;
import cn.mutils.app.demo.ui.FirUpdateDemoView;
import cn.mutils.app.demo.ui.HLinearAdapterDemoView;
import cn.mutils.app.demo.ui.IndexViewDemoView;
import cn.mutils.app.demo.ui.IntentExecutorDemoView;
import cn.mutils.app.demo.ui.MediaDemoView;
import cn.mutils.app.demo.ui.NetTaskDemoView;
import cn.mutils.app.demo.ui.PayDemoView;
import cn.mutils.app.demo.ui.ProgressIconDemoView;
import cn.mutils.app.demo.ui.ProgressViewDemoView;
import cn.mutils.app.demo.ui.PromptDemoView;
import cn.mutils.app.demo.ui.QRCodeDemoView;
import cn.mutils.app.demo.ui.ShareDemoView;
import cn.mutils.app.demo.ui.SurfaceViewerDemoView;
import cn.mutils.app.demo.ui.TestDemoView;
import cn.mutils.app.demo.ui.TipViewDemoView;
import cn.mutils.app.demo.ui.UIAdapterDemoView;
import cn.mutils.app.demo.ui.UmengDemoView;
import cn.mutils.app.demo.ui.WebFrameDemoView;
import cn.mutils.app.demo.ui.ZipDemoView;

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

    @OnClick({R.id.test, R.id.ui_adapter, R.id.zip, R.id.conf_item, R.id.aes, R.id.fir, R.id.anim_task, R.id.media, R.id.net_task,
            R.id.pay, R.id.qrcode, R.id.share, R.id.action_sheet, R.id.amap, R.id.date_chooser, R.id.prompt,
            R.id.progress_view, R.id.tip_view, R.id.busy, R.id.path_button, R.id.hlinear_adapter, R.id.index_view,
            R.id.web_frame, R.id.intent_executor, R.id.umeng, R.id.surface_viewer, R.id.progress_icon, R.id.channel})
    protected void onClick(View v) {
        Intent intent = new Intent(this, ShellActivity.class);
        ShellExtra extra = new ShellExtra();
        if (v instanceof TextView) {
            extra.setTitle(((TextView) v).getText().toString());
        }
        switch (v.getId()) {
            case R.id.test:
                extra.setViewName(TestDemoView.class.getName());
                break;
            case R.id.ui_adapter:
                extra.setViewName(UIAdapterDemoView.class.getName());
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
            case R.id.date_chooser:
                extra.setViewName(DateChooserDemoView.class.getName());
                break;
            case R.id.prompt:
                extra.setViewName(PromptDemoView.class.getName());
                break;
            case R.id.progress_view:
                extra.setViewName(ProgressViewDemoView.class.getName());
                break;
            case R.id.tip_view:
                extra.setViewName(TipViewDemoView.class.getName());
                break;
            case R.id.busy:
                extra.setViewName(BusyDemoView.class.getName());
                break;
            case R.id.path_button:
                extra.setViewName(DemoPathButtonView.class.getName());
                break;
            case R.id.hlinear_adapter:
                extra.setViewName(HLinearAdapterDemoView.class.getName());
                break;
            case R.id.index_view:
                extra.setViewName(IndexViewDemoView.class.getName());
                break;
            case R.id.web_frame:
                extra.setViewName(WebFrameDemoView.class.getName());
                break;
            case R.id.intent_executor:
                extra.setViewName(IntentExecutorDemoView.class.getName());
                break;
            case R.id.umeng:
                extra.setViewName(UmengDemoView.class.getName());
                break;
            case R.id.surface_viewer:
                extra.setViewName(SurfaceViewerDemoView.class.getName());
                break;
            case R.id.progress_icon:
                extra.setViewName(ProgressIconDemoView.class.getName());
                break;
            case R.id.channel:
                extra.setViewName(ChannelDemoView.class.getName());
                break;
            default:
                break;
        }
        extra.putTo(intent);
        startActivity(intent);
    }
}

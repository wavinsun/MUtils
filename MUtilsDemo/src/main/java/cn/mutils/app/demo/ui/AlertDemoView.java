package cn.mutils.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;

import cn.mutils.app.demo.R;
import cn.mutils.app.ui.Alert;
import cn.mutils.app.ui.Alert.AlertListener;
import cn.mutils.app.ui.Dialoger;
import cn.mutils.app.ui.StateView;
import cn.mutils.core.annotation.event.Click;
import cn.mutils.core.annotation.res.SetContentView;

@SetContentView(R.layout.view_alert)
public class AlertDemoView extends StateView {

    public AlertDemoView(Context context) {
        super(context);
    }

    public AlertDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AlertDemoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Click(R.id.go)
    protected void onClickGo() {
        Alert alert = new Alert(getContext());
        alert.setTitle("标题");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 50; i++) {
            if (i != 0) {
                sb.append("\n");
            }
            sb.append("消息");
        }
        alert.setMessage(sb);
        alert.setOK(R.string.ok);
        alert.setCancel(R.string.cancel);
        alert.setListener(new AlertListener() {

            @Override
            public boolean onOK(Alert alert) {
                toast("onOK");
                return false;
            }

            @Override
            public boolean onCancel(Alert alert) {
                toast("onCancel");
                return false;
            }
        });
        alert.show();
    }

    @Click(R.id.dialoger)
    protected void onClickDialoger() {
        Dialoger dialoger = new Dialoger(getContext());
        dialoger.setContentView(R.layout.dialog_edit);
        dialoger.requestFill();
        dialoger.show();
    }

}

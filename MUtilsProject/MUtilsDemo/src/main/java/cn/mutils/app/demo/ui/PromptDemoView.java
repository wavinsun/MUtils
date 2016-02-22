package cn.mutils.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;

import cn.mutils.app.demo.R;
import cn.mutils.app.ui.Alert;
import cn.mutils.app.ui.Alert.AlertListener;
import cn.mutils.app.ui.Prompt;
import cn.mutils.app.ui.StateView;
import cn.mutils.core.annotation.event.Click;
import cn.mutils.core.annotation.res.SetContentView;

@SetContentView(R.layout.view_prompt)
public class PromptDemoView extends StateView {

    public PromptDemoView(Context context) {
        super(context);
    }

    public PromptDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PromptDemoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Click(R.id.go)
    protected void onClickGo() {
        Prompt p = new Prompt(getContext());
        p.setTitle("Prompt");
        p.setOK(R.string.ok);
        p.setCancel(R.string.cancel);
        p.setListener(new AlertListener() {

            @Override
            public boolean onOK(Alert alert) {
                toast(((Prompt) alert).getEditText());
                return false;
            }

            @Override
            public boolean onCancel(Alert alert) {
                return false;
            }
        });
        p.show();
    }

}

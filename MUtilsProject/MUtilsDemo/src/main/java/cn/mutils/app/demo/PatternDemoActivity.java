package cn.mutils.app.demo;


import android.os.Bundle;

import cn.mutils.app.core.annotation.event.OnClick;
import cn.mutils.app.core.annotation.res.SetContentView;
import cn.mutils.app.demo.ui.BasicActivity;

@SetContentView(R.layout.activity_demo_pattern)
public class PatternDemoActivity extends BasicActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHeartbeatEnabled(true);
    }

    @Override
    public boolean checkPattern() {
        return true;
    }

    @OnClick(R.id.go)
    protected void onClickGo() {
        startPatternActivity();
    }


}

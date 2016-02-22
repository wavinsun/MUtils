package cn.mutils.app.demo;


import android.os.Bundle;

import cn.mutils.app.demo.ui.BasicActivity;
import cn.mutils.core.annotation.event.Click;
import cn.mutils.core.annotation.res.SetContentView;

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

    @Click(R.id.go)
    protected void onClickGo() {
        startPatternActivity();
    }


}

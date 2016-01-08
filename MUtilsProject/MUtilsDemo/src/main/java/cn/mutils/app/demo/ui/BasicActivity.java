package cn.mutils.app.demo.ui;

import android.content.Intent;

import cn.mutils.app.demo.PatternActivity;
import cn.mutils.app.ui.AppActivity;

public class BasicActivity extends AppActivity {

    @Override
    public void startPatternActivity() {
        startActivity(new Intent(this, PatternActivity.class));
    }
}

package cn.mutils.app.demo;

import android.os.Bundle;

import cn.mutils.app.core.annotation.res.FindViewById;
import cn.mutils.app.core.annotation.res.SetContentView;
import cn.mutils.app.demo.ui.BasicActivity;
import cn.mutils.app.ui.pattern.PatternCanvas;
import cn.mutils.app.ui.pattern.PatternIcon;
import cn.mutils.app.util.AppUtil;

@SetContentView(R.layout.activity_pattern)
public class PatternActivity extends BasicActivity {

    @FindViewById(R.id.icon)
    protected PatternIcon mPatternIcon;

    @FindViewById(R.id.canvas)
    protected PatternCanvas mPatternCanvas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHeartbeatEnabled(true);

        mPatternIcon.setPassword("03678");
        mPatternCanvas.setOnPasswordDrawnListener(new PatternCanvas.OnPasswordDrawnListener() {
            @Override
            public void onPasswordDrawn(PatternCanvas canvas, String password) {
                if (AppUtil.equals(password, mPatternIcon.getPassword())) {
                    finish();
                } else {
                    toast("Error pattern");
                    canvas.refresh();
                }
            }
        });
    }

    @Override
    public boolean onInterceptBackPressed() {
        if (super.onInterceptBackPressed()) {
            return true;
        }
        return true;
    }

}

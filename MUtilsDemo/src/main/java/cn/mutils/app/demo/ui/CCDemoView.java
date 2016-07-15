package cn.mutils.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;

import cn.mutils.app.demo.IUserService;
import cn.mutils.app.demo.R;
import cn.mutils.app.ui.StateView;
import cn.mutils.core.annotation.event.Click;
import cn.mutils.core.annotation.res.SetContentView;
import cn.mutils.core.runtime.CC;

@SetContentView(R.layout.view_cc)
public class CCDemoView extends StateView {

    public CCDemoView(Context context) {
        super(context);
    }

    public CCDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CCDemoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Click(R.id.go)
    protected void onClickGo() {
        IUserService userService = CC.getService(IUserService.class);
        if (userService != null) {
            toast(userService.getUserName());
        } else {
            toast("ERROR");
        }
    }

}

package cn.mutils.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;

import cn.mutils.app.demo.R;
import cn.mutils.app.demo.web.ByeByeWebDispatcher;
import cn.mutils.app.demo.web.GetAppInfoWebDispatcher;
import cn.mutils.app.demo.web.ToastWebDispatcher;
import cn.mutils.app.ui.StateView;
import cn.mutils.app.ui.web.WebFrame;
import cn.mutils.core.annotation.res.FindViewById;
import cn.mutils.core.annotation.res.SetContentView;

@SetContentView(R.layout.view_web_frame)
public class WebFrameDemoView extends StateView {

    @FindViewById(R.id.web_frame)
    protected WebFrame mWebFrame;

    public WebFrameDemoView(Context context) {
        super(context);
    }

    public WebFrameDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WebFrameDemoView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mWebFrame.add(ByeByeWebDispatcher.class);
        mWebFrame.add(GetAppInfoWebDispatcher.class);
        mWebFrame.add(ToastWebDispatcher.class);
        mWebFrame.getWebView().loadUrl("file:///android_asset/app-api.htm");
    }

}

package cn.o.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;
import cn.o.app.core.annotation.res.FindViewById;
import cn.o.app.core.annotation.res.SetContentView;
import cn.o.app.demo.R;
import cn.o.app.demo.web.ByeByeWebDispatcher;
import cn.o.app.demo.web.GetAppInfoWebDispatcher;
import cn.o.app.ui.StateView;
import cn.o.app.ui.web.WebFrame;

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
		mWebFrame.getWebView().loadUrl("file:///android_asset/app-api.htm");
	}

}

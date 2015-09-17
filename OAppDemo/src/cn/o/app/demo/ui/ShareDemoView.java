package cn.o.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import cn.o.app.core.annotation.event.OnClick;
import cn.o.app.core.annotation.res.SetContentView;
import cn.o.app.demo.R;
import cn.o.app.share.Share;
import cn.o.app.ui.StateView;

@SetContentView(R.layout.view_share)
public class ShareDemoView extends StateView {

	public ShareDemoView(Context context) {
		super(context);
	}

	public ShareDemoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ShareDemoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onCreate() {
		super.onCreate();

	}

	@OnClick({ R.id.qq, R.id.qzone, R.id.wechat, R.id.momoents, R.id.weibo })
	protected void onClick(View v) {
		Share.setTencentAppId("1104746550");
		Share.setWechatAppId("wx143c7417f8f7f690");
		Share.setWeiboAppId("");
		Share share = new Share(getContext());
		share.setTitle("Title");
		share.setText("Text");
		share.setUrl("http://www.baidu.com/");
		share.setContext(getContext());
		share.setImageUrl("http://www.baidu.com/favicon.ico");
		share.setMethod(Share.METHOD_API);
		switch (v.getId()) {
		case R.id.qq:
			share.setPlatform(Share.PLATFORM_QQ);
			break;
		case R.id.qzone:
			share.setPlatform(Share.PLATFORM_QZONE);
			break;
		case R.id.wechat:
			share.setPlatform(Share.PLATFORM_WECHAT);
			break;
		case R.id.momoents:
			share.setPlatform(Share.PLATFORM_WECHAT_MOMENTS);
			break;
		case R.id.weibo:
			share.setPlatform(Share.PLATFORM_WEIBO);
			break;

		default:
			break;
		}
		share.share();
	}

}

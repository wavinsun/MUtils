package cn.o.app.demo.ui;

import android.content.Context;
import android.util.AttributeSet;
import cn.o.app.core.annotation.event.OnClick;
import cn.o.app.core.annotation.res.SetContentView;
import cn.o.app.core.event.listener.VersionUpdateListener;
import cn.o.app.demo.R;
import cn.o.app.ui.StateView;

@SetContentView(R.layout.view_umeng)
public class UmengDemoView extends StateView {

	public UmengDemoView(Context context) {
		super(context);
	}

	public UmengDemoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public UmengDemoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@OnClick(R.id.umeng_update)
	protected void onClickUmengUpdate() {
		((BasicActivity) getContext()).checkNewVersion(new VersionUpdateListener() {

			@Override
			public boolean onYes(String version) {
				return false;
			}

			@Override
			public void onUpdateCancel(String version) {

			}

			@Override
			public void onUpdate(String version) {

			}

			@Override
			public void onNo() {

			}
		});
	}

	@OnClick(R.id.umeng_feedback)
	protected void onClickUmengFeedback() {
		((BasicActivity) getContext()).feedback();
	}

}

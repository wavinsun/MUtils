package cn.o.app.demo;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import cn.o.app.annotation.res.FindViewById;
import cn.o.app.annotation.res.SetContentView;
import cn.o.app.demo.ui.BasicActivity;
import cn.o.app.io.Extra;

@SuppressWarnings("unchecked")
@SetContentView(R.layout.activity_shell)
public class ShellActivity extends BasicActivity {

	@SuppressWarnings("serial")
	public static class ShellExtra extends Extra {

		protected String mTitle;

		protected String mViewName;

		public String getTitle() {
			return mTitle;
		}

		public void setTitle(String title) {
			mTitle = title;
		}

		public String getViewName() {
			return mViewName;
		}

		public void setViewName(String viewName) {
			mViewName = viewName;
		}
	}

	@FindViewById(R.id.shell_root)
	protected LinearLayout mShellRoot;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		ShellExtra extra = new ShellExtra();
		if (extra.getFrom(getIntent())) {
			mNavigationTitle.setText(extra.getTitle());
			try {
				Class<View> viewClass = (Class<View>) Class.forName(extra.getViewName());
				View v = viewClass.getConstructor(Context.class).newInstance(getContext());
				mShellRoot.addView(v);
				bindStateViews();
			} catch (Exception e) {

			}
		}
	}
}

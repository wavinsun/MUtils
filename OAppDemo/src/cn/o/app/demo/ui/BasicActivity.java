package cn.o.app.demo.ui;

import android.widget.ImageView;
import android.widget.TextView;
import cn.o.app.AppActivity;
import cn.o.app.core.annotation.event.OnClick;
import cn.o.app.core.annotation.res.FindViewById;
import cn.o.app.demo.R;

public class BasicActivity extends AppActivity {

	@FindViewById(R.id.navigation_back)
	protected ImageView mNavigationBackBtn;

	@FindViewById(R.id.navigation_title)
	protected TextView mNavigationTitle;

	@OnClick(R.id.navigation_back)
	protected void onClickNavigationBackBtn() {
		finish();
	}
}

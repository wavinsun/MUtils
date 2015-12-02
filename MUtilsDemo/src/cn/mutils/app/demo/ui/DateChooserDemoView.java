package cn.mutils.app.demo.ui;

import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import cn.mutils.app.core.annotation.event.OnClick;
import cn.mutils.app.core.annotation.res.SetContentView;
import cn.mutils.app.core.time.DateTime;
import cn.mutils.app.demo.R;
import cn.mutils.app.ui.DateChooser;
import cn.mutils.app.ui.DateChooser.OnChooseDateListener;
import cn.mutils.app.ui.StateView;
import cn.mutils.app.util.AppUtil;

@SetContentView(R.layout.view_date_chooser)
public class DateChooserDemoView extends StateView {

	protected DateTime mPickedDate;

	public DateChooserDemoView(Context context) {
		super(context);
	}

	public DateChooserDemoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DateChooserDemoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@OnClick(R.id.go)
	protected void onClickGo() {
		DateChooser picker = new DateChooser(getContext());
		picker.setPickTime(true);
		DateTime now = new DateTime();
		picker.setMaxDate(AppUtil.getDate(AppUtil.getYear(now) + 10, AppUtil.getMonth(now), AppUtil.getDay(now)));
		picker.setListener(new OnChooseDateListener() {

			@Override
			public void onChoosed(DateChooser chooser, Date date) {
				mPickedDate = new DateTime(date.getTime());
				mPickedDate.setFormat("yyyy-MM-dd HH:mm");
				toast(mPickedDate.toString());
			}

		});
		picker.show(mPickedDate);
	}

}

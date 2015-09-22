package cn.o.app.demo.ui;

import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import cn.o.app.AppUtil;
import cn.o.app.core.annotation.event.OnClick;
import cn.o.app.core.annotation.res.SetContentView;
import cn.o.app.core.time.ODate;
import cn.o.app.demo.R;
import cn.o.app.ui.DateChooser;
import cn.o.app.ui.DateChooser.OnChooseDateListener;
import cn.o.app.ui.StateView;

@SetContentView(R.layout.view_date_chooser)
public class DateChooserDemoView extends StateView {

	protected ODate mPickedDate;

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
		ODate now = new ODate();
		picker.setMaxDate(AppUtil.getDate(AppUtil.getYear(now) + 10, AppUtil.getMonth(now), AppUtil.getDay(now)));
		picker.setListener(new OnChooseDateListener() {

			@Override
			public void onChoosed(DateChooser chooser, Date date) {
				mPickedDate = new ODate(date.getTime());
				mPickedDate.setFormat("yyyy-MM-dd HH:mm");
				toast(mPickedDate.toString());
			}

		});
		picker.show(mPickedDate);
	}

}

package cn.o.app.demo.ui;

import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import cn.o.app.OUtil;
import cn.o.app.core.annotation.event.OnClick;
import cn.o.app.core.annotation.res.SetContentView;
import cn.o.app.core.io.ODate;
import cn.o.app.demo.R;
import cn.o.app.ui.ODatePicker;
import cn.o.app.ui.ODatePicker.OnPickDateListener;
import cn.o.app.ui.StateView;

@SetContentView(R.layout.view_date_picker)
public class DatePickerDemoView extends StateView {

	protected ODate mPickedDate;

	public DatePickerDemoView(Context context) {
		super(context);
	}

	public DatePickerDemoView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public DatePickerDemoView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@OnClick(R.id.go)
	protected void onClickGo() {
		ODatePicker picker = new ODatePicker(getContext());
		picker.setPickTime(true);
		ODate now = new ODate();
		picker.setMaxDate(OUtil.getDate(OUtil.getYear(now) + 10, OUtil.getMonth(now), OUtil.getDay(now)));
		picker.setListener(new OnPickDateListener() {

			@Override
			public void onPicked(ODatePicker picker, Date date) {
				mPickedDate = new ODate(date.getTime());
				mPickedDate.setFormat("yyyy-MM-dd HH:mm");
				toast(mPickedDate.toString());
			}

		});
		picker.show(mPickedDate);
	}

}

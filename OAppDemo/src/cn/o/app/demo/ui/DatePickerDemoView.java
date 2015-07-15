package cn.o.app.demo.ui;

import java.util.Date;

import android.content.Context;
import android.util.AttributeSet;
import cn.o.app.annotation.event.OnClick;
import cn.o.app.annotation.res.SetContentView;
import cn.o.app.demo.R;
import cn.o.app.io.ODate;
import cn.o.app.ui.ODatePicker;
import cn.o.app.ui.ODatePicker.OnPickDateListener;
import cn.o.app.ui.StateView;

@SetContentView(R.layout.view_date_picker)
public class DatePickerDemoView extends StateView {

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
		picker.setListener(new OnPickDateListener() {

			@Override
			public void onPicked(ODatePicker picker, Date date) {
				ODate d = new ODate(date.getTime());
				d.setFormat("yyyy-MM-dd");
				toast(d.toString());
			}

		});
		picker.show();
	}

}

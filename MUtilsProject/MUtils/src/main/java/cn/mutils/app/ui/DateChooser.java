package cn.mutils.app.ui;

import java.util.Date;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.mutils.app.R;
import cn.mutils.app.util.AppUtil;
import kankan.wheel.widget.NumericWheelAdapter;
import kankan.wheel.widget.OnWheelChangedListener;
import kankan.wheel.widget.WheelView;

/**
 * Date chooser like iOS
 */
@SuppressLint("InflateParams")
@SuppressWarnings("deprecation")
public class DateChooser {

	/**
	 * Date chooser listener
	 */
	public static abstract class OnChooseDateListener {

		/**
		 * Submit date choose
		 * 
		 * @param chooser
		 * @param date
		 */
		public abstract void onChoosed(DateChooser chooser, Date date);

		/**
		 * Give date while choosing
		 * 
		 * @param chooser
		 * @param date
		 */
		public void onChoosing(DateChooser chooser, Date date) {

		}

		/**
		 * Cancel choose
		 * 
		 * @param chooser
		 */
		public void onCancel(DateChooser chooser) {

		}

		/**
		 * Get text for InfoToast when choose date is above maximum date
		 * 
		 * @param chooser
		 * @param maxDate
		 * @return
		 */
		public String onChoosedAboveMaxDate(DateChooser chooser, Date maxDate) {
			return "无效时间";
		}

		/**
		 * Get text for InfoToast when choose date is down minimum date
		 * 
		 * @param chooser
		 * @param minDate
		 * @return
		 */
		public String onChoosedDownMinDate(DateChooser chooser, Date minDate) {
			return "无效时间";
		}

	}

	/** Listener */
	protected OnChooseDateListener mOnChooseDateListener;

	/** Dialog */
	protected Dialoger mDialog;

	protected Context mContext;

	/** Whether pick hour minute */
	protected boolean mPickTime;

	/** Start year to pick */
	protected Integer mStartYear;

	/** End year to pick */
	protected Integer mEndYear;

	protected View mContentView;

	/** Year wheel view */
	protected WheelView mYearView;

	/** Month wheel view */
	protected WheelView mMonthView;

	/** Day wheel view */
	protected WheelView mDayView;

	/** Hour wheel view */
	protected WheelView mHourView;

	/** Minute wheel view */
	protected WheelView mMinuteView;

	protected InfoToast mInfoToast;

	/** Date picked */
	protected Date mPickedDate;

	protected Date mMinDate;

	protected Date mMaxDate;

	public DateChooser(Context context) {
		mContext = context;
	}

	public void setMinDate(Date minDate) {
		mMinDate = minDate;
	}

	public void setMaxDate(Date maxDate) {
		mMaxDate = maxDate;
	}

	public void setStartYear(int startYear) {
		if (mDialog != null) {
			return;
		}
		mStartYear = startYear;
	}

	public void setEndYear(int endYear) {
		if (mDialog != null) {
			return;
		}
		mEndYear = endYear;
	}

	public boolean isPickTime() {
		return mPickTime;
	}

	public void setPickTime(boolean pickTime) {
		if (mDialog != null) {
			return;
		}
		mPickTime = pickTime;
	}

	public void setListener(OnChooseDateListener listener) {
		mOnChooseDateListener = listener;
	}

	public void ok() {
		if (mDialog == null) {
			return;
		}
		if (mOnChooseDateListener != null) {
			int year = mYearView.getCurrentItem() + mStartYear;
			int month = mMonthView.getCurrentItem() + 1;
			int day = mDayView.getCurrentItem() + 1;
			int hour = mPickTime ? mHourView.getCurrentItem() : 0;
			int minute = mPickTime ? mMinuteView.getCurrentItem() : 0;
			mPickedDate = AppUtil.getDate(year, month, day, hour, minute);
			if (mMaxDate != null && mPickedDate.getTime() > mMaxDate.getTime()) {
				if (mOnChooseDateListener != null) {
					mInfoToast.show(mOnChooseDateListener.onChoosedAboveMaxDate(DateChooser.this, mMaxDate), 3000);
				} else {
					mInfoToast.show("无效时间", 3000);
				}
				return;
			}
			if (mMinDate != null && mPickedDate.getTime() < mMinDate.getTime()) {
				if (mOnChooseDateListener != null) {
					mInfoToast.show(mOnChooseDateListener.onChoosedDownMinDate(DateChooser.this, mMinDate), 3000);
				} else {
					mInfoToast.show("无效时间", 3000);
				}
				return;
			}
			if (mOnChooseDateListener != null) {
				mOnChooseDateListener.onChoosed(DateChooser.this, mPickedDate);
			}
		}
		dismiss();
	}

	public void cancel() {
		if (mDialog == null) {
			return;
		}
		if (mOnChooseDateListener != null) {
			mOnChooseDateListener.onCancel(this);
		}
		mDialog.cancel();
		mDialog = null;
	}

	public void dismiss() {
		if (mDialog == null) {
			return;
		}
		mDialog.dismiss();
		mDialog = null;
	}

	public void show() {
		show(null);
	}

	public void show(Date pickedDate) {
		if (mDialog != null) {
			mDialog.show();
			return;
		}
		Date current = null;
		LayoutInflater inflater = LayoutInflater.from(mContext);
		mContentView = inflater.inflate(R.layout.date_chooser, null);
		mContentView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				cancel();
			}
		});
		mContentView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				cancel();
			}
		});
		mContentView.findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ok();
			}
		});
		mInfoToast = (InfoToast) mContentView.findViewById(R.id.info_toast);
		mYearView = (WheelView) mContentView.findViewById(R.id.year);
		if (mStartYear == null) {
			current = new Date();
			mStartYear = AppUtil.getYear(current) - 50;
		}
		if (mEndYear == null) {
			current = current != null ? current : new Date();
			mEndYear = AppUtil.getYear(current) + 50;
		}
		if (pickedDate == null) {
			current = current != null ? current : new Date();
			pickedDate = current;
		}
		mYearView.setAdapter(new NumericWheelAdapter(mStartYear, mEndYear));
		mYearView.setCyclic(true);
		mYearView.setLabel("年");
		mYearView.setCurrentItem(AppUtil.getYear(pickedDate) - mStartYear);
		mMonthView = (WheelView) mContentView.findViewById(R.id.month);
		mMonthView.setAdapter(new NumericWheelAdapter(1, 12));
		mMonthView.setCyclic(true);
		mMonthView.setLabel("月");
		mMonthView.setCurrentItem(AppUtil.getMonth(pickedDate) - 1);
		mDayView = (WheelView) mContentView.findViewById(R.id.day);
		mDayView.setCyclic(true);
		mDayView.setAdapter(new NumericWheelAdapter(1, AppUtil.getDaysOfMonth(pickedDate)));
		mDayView.setLabel("日");
		mDayView.setCurrentItem(AppUtil.getDay(pickedDate) - 1);
		mHourView = (WheelView) mContentView.findViewById(R.id.hour);
		mMinuteView = (WheelView) mContentView.findViewById(R.id.min);
		if (mPickTime) {
			mHourView.setVisibility(View.VISIBLE);
			mMinuteView.setVisibility(View.VISIBLE);
			mHourView.setAdapter(new NumericWheelAdapter(0, 23));
			mHourView.setCyclic(true);
			mHourView.setLabel("时");
			mHourView.setCurrentItem(pickedDate.getHours());
			mMinuteView.setAdapter(new NumericWheelAdapter(0, 59));
			mMinuteView.setCyclic(true);
			mMinuteView.setLabel("分");
			mMinuteView.setCurrentItem(pickedDate.getMinutes());
		} else {
			mHourView.setVisibility(View.GONE);
			mMinuteView.setVisibility(View.GONE);
		}

		OnWheelChangedListener onWheelChangedListener4Leap = new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int year = mYearView.getCurrentItem() + mStartYear;
				int month = mMonthView.getCurrentItem() + 1;
				int dayIndex = mDayView.getCurrentItem();
				int dayCount = AppUtil.getDaysOfMonth(year, month);
				mDayView.setAdapter(new NumericWheelAdapter(1, dayCount));
				if (dayIndex >= dayCount) {
					mDayView.setCurrentItem(0);
				}
			}
		};
		mYearView.addChangingListener(onWheelChangedListener4Leap);
		mMonthView.addChangingListener(onWheelChangedListener4Leap);

		int screenHeight = mContext.getResources().getDisplayMetrics().heightPixels;
		int textSize = (int) ((screenHeight / 100.0) * (mPickTime ? 3 : 4));
		mYearView.TEXT_SIZE = textSize;
		mMonthView.TEXT_SIZE = textSize;
		mDayView.TEXT_SIZE = textSize;
		mHourView.TEXT_SIZE = textSize;
		mMinuteView.TEXT_SIZE = textSize;

		OnWheelChangedListener onWheelChangedListener = new OnWheelChangedListener() {

			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				int year = mYearView.getCurrentItem() + mStartYear;
				int month = mMonthView.getCurrentItem() + 1;
				int day = mDayView.getCurrentItem() + 1;
				int hour = mPickTime ? mHourView.getCurrentItem() : 0;
				int minute = mPickTime ? mMinuteView.getCurrentItem() : 0;
				mPickedDate = AppUtil.getDate(year, month, day, hour, minute);
				if (mOnChooseDateListener != null) {
					mOnChooseDateListener.onChoosing(DateChooser.this, mPickedDate);
				}
			}

		};
		mYearView.addChangingListener(onWheelChangedListener);
		mMonthView.addChangingListener(onWheelChangedListener);
		mDayView.addChangingListener(onWheelChangedListener);
		mHourView.addChangingListener(onWheelChangedListener);
		mMinuteView.addChangingListener(onWheelChangedListener);

		mDialog = new Dialoger(mContext);
		mDialog.setWindowAnimations(R.style.DialogerOnBottomAnim);
		mDialog.setContentView(mContentView,
				new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
		mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				cancel();
			}

		});
		mDialog.requestHFill();
		mDialog.show();
	}
}
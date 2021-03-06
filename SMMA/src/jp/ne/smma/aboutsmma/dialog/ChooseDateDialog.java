package jp.ne.smma.aboutsmma.dialog;

import java.util.Calendar;
import jp.ne.smma.wheel.ArrayWheelAdapter;
import jp.ne.smma.wheel.NumericWheelAdapter;
import jp.ne.smma.wheel.OnWheelChangedListener;
import jp.ne.smma.wheel.WheelView;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout.LayoutParams;
import android.widget.TextView;

/**
 * Choose date dialog
 */
public class ChooseDateDialog extends Dialog {
	private Context Mcontex;

	private int NoOfYear = 40;

	/**
	 * Constructor
	 * 
	 * @param context
	 *            - context activity
	 * @param calendar
	 *            - calendar
	 * @param dtp
	 *            - event
	 */
	@SuppressWarnings("deprecation")
	public ChooseDateDialog(Context context, Calendar calendar,
			final DatePickerListner dtp) {

		super(context);
		Mcontex = context;
		LinearLayout lytmain = new LinearLayout(Mcontex);
		lytmain.setOrientation(LinearLayout.VERTICAL);
		LinearLayout lytdate = new LinearLayout(Mcontex);
		LinearLayout lytbutton = new LinearLayout(Mcontex);

		Button btnset = new Button(Mcontex);
		Button btncancel = new Button(Mcontex);

		btnset.setText("Set");
		btncancel.setText("Cancel");

		final WheelView month = new WheelView(Mcontex);
		final WheelView year = new WheelView(Mcontex);
		final WheelView day = new WheelView(Mcontex);
		final WheelView hour = new WheelView(Mcontex);
		final WheelView minute = new WheelView(Mcontex);
		lytdate.addView(year, new LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 0.9f)); // 1f
		lytdate.addView(month, new LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1.1f)); // 0.8
		lytdate.addView(day, new LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1.1f)); // 1.2
		// /////////////////ADD layout////////////////////
		lytdate.addView(hour, new LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1.1f));
		lytdate.addView(minute, new LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1.1f));
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		lytbutton.addView(btnset, new LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

		lytbutton.addView(btncancel, new LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
		lytbutton.setPadding(5, 5, 5, 5);
		lytmain.addView(lytdate);
		lytmain.addView(lytbutton);

		setContentView(lytmain);

		getWindow().setLayout(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		OnWheelChangedListener listener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateDays(year, month, day, hour, minute);

			}
		};

		// month
		int curMonth = calendar.get(Calendar.MONTH);
		String months[] = new String[] { "1月", "2月", "3月", "4月", "5月", "6月",
				"7月", "8月", "9月", "10月", "11月", "12月" };
		month.setViewAdapter(new DateArrayAdapter(context, months, curMonth));
		month.setCurrentItem(curMonth);
		month.addChangingListener(listener);

		Calendar cal = Calendar.getInstance();
		// year
		int curYear = calendar.get(Calendar.YEAR);
		int Year = cal.get(Calendar.YEAR);

		year.setViewAdapter(new YearAdapter(context, Year - NoOfYear,
				Year + NoOfYear, NoOfYear));
		year.setCurrentItem(curYear - (Year - NoOfYear));
		year.addChangingListener(listener);

		// day
		updateDays(year, month, day, hour, minute);
		day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);

		// hour and minute
		hour.setViewAdapter(new NumericWheelAdapter(Mcontex, 0, 23, "%2d 時"));
		minute.setViewAdapter(new NumericWheelAdapter(Mcontex, 0, 59, "%02d 分"));
		// Thiết lập thời gian hiện tại
		Calendar c = Calendar.getInstance();
		int curHours = c.get(Calendar.HOUR_OF_DAY);
		int curMinutes = c.get(Calendar.MINUTE);
		// int curSeconds = c.get(Calendar.SECOND);

		hour.setCurrentItem(curHours);
		minute.setCurrentItem(curMinutes);

		btnset.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Calendar c = updateDays(year, month, day, hour, minute);
				dtp.OnDoneButton(ChooseDateDialog.this, c);
			}
		});
		btncancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dtp.OnCancelButton(ChooseDateDialog.this);

			}
		});

	}

	/*
	 * Update days
	 */
	Calendar updateDays(WheelView year, WheelView month, WheelView day,
			WheelView hour, WheelView minute) {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR,
				calendar.get(Calendar.YEAR)
						+ (year.getCurrentItem() - NoOfYear));
		calendar.set(Calendar.MONTH, month.getCurrentItem());

		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		day.setViewAdapter(new DayAdapter(Mcontex, 1, maxDays, calendar
				.get(Calendar.DAY_OF_MONTH) - 1));
		int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
		day.setCurrentItem(curDay - 1, true);
		calendar.set(Calendar.DAY_OF_MONTH, curDay);

		// //Add hour and minute ///
		calendar.set(Calendar.HOUR_OF_DAY, hour.getCurrentItem());
		calendar.set(Calendar.MINUTE, minute.getCurrentItem());
		return calendar;

	}
	/*
	 * Day adapter
	 */
	private class YearAdapter extends NumericWheelAdapter {
		int currentItem;
		int currentValue;

		int minValue;
		@SuppressWarnings("unused")
		int maxValue;

		public YearAdapter(Context context, int minValue, int maxValue,
				int current) {
			super(context, minValue, maxValue);
			this.currentValue = current;
			this.minValue = minValue;
			setTextSize(14);
		}
		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			if (currentItem == currentValue) {
				// set color current day
				// view.setTextColor(0xFF0000F0);
			}
			view.setTypeface(null, Typeface.BOLD);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;

			return super.getItem(index, cachedView, parent);
		}

		@Override
		public CharSequence getItemText(int index) {
//			if (index >= 0 && index < getItemsCount()) {
				int value = minValue + index;
				return String.format("%02d 年", value);
//			}
//			return null;
		}

	}
	/*
	 * Day adapter
	 */
	private class DayAdapter extends NumericWheelAdapter {
		int currentItem;
		int currentValue;

		int minValue;
		@SuppressWarnings("unused")
		int maxValue;

		public DayAdapter(Context context, int minValue, int maxValue,
				int current) {
			super(context, minValue, maxValue);
			this.currentValue = current;
			this.minValue = minValue;
			setTextSize(14);
		}
		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			if (currentItem == currentValue) {
				// set color current day
				// view.setTextColor(0xFF0000F0);
			}
			view.setTypeface(null, Typeface.BOLD);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;

			return super.getItem(index, cachedView, parent);
		}

		@Override
		public CharSequence getItemText(int index) {
//			if (index >= 0 && index < getItemsCount()) {
				int value = minValue + index;
				return String.format("%02d 日", value);
//			}
//			return null;
		}

//		@Override
//		public int getItemsCount() {
//			return maxValue - minValue + 1;
//		}

	}

	/*
	 * Date numeric adapter
	 */
	private class DateNumericAdapter extends NumericWheelAdapter {
		int currentItem;
		int currentValue;

		public DateNumericAdapter(Context context, int minValue, int maxValue,
				int current) {
			super(context, minValue, maxValue);
			this.currentValue = current;
			setTextSize(16);
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			if (currentItem == currentValue) {
				// set color current day
				// view.setTextColor(0xFF0000F0);
			}
			view.setTypeface(null, Typeface.BOLD);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;

			return super.getItem(index, cachedView, parent);
		}
	}

	/*
	 * Date array adapter
	 */
	private class DateArrayAdapter extends ArrayWheelAdapter<String> {
		int currentItem;
		int currentValue;

		public DateArrayAdapter(Context context, String[] items, int current) {
			super(context, items);
			this.currentValue = current;
			setTextSize(14);
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			if (currentItem == currentValue) {
				// set text color
				// view.setTextColor(0xFF0000F0);
			}
			view.setTypeface(null, Typeface.BOLD);
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			currentItem = index;
			return super.getItem(index, cachedView, parent);
		}
	}

	/*
	 * Interface date picker
	 */
	public interface DatePickerListner {
		public void OnDoneButton(Dialog datedialog, Calendar c);

		public void OnCancelButton(Dialog datedialog);
	}
}

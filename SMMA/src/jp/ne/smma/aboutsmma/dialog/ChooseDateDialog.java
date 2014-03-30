package jp.ne.smma.aboutsmma.dialog;

import java.util.Calendar;
import java.util.Locale;

import jp.ne.smma.wheel.ArrayWheelAdapter;
import jp.ne.smma.wheel.NumericWheelAdapter;
import jp.ne.smma.wheel.OnWheelChangedListener;
import jp.ne.smma.wheel.WheelView;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
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
		// ////////////////ADD HH:PP:SS//////////////////////////
		final WheelView hour = new WheelView(Mcontex);
		final WheelView minute = new WheelView(Mcontex);
		// final WheelView second = new WheelView(Mcontex);
		// /////////////////////////////////////////
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
		// lytdate.addView(second, new LayoutParams(
		// android.view.ViewGroup.LayoutParams.FILL_PARENT,
		// android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1f));
		// //////////////////////////////////////
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
		String years[] = new String[] { "2010年", "2011年", "2012年", "2013年",
				"2014年", "2015年", "2016年", "2017年", "2018年", "2019年", "2020年",
				"2021年", "2022年", "2023年", "2024年", "2025年", "2026年", "2027年",
				"2028年", "2029年", "2030年", "2031年", "2032年", "2033年", "2034年",
				"2035年" };
		year.setViewAdapter(new DateArrayAdapter(context, years, curYear));
//		year.setViewAdapter(new DateNumericAdapter(context, Year - NoOfYear,
//				Year + NoOfYear, NoOfYear));
//		year.setCurrentItem(curYear - (Year - NoOfYear));
//		year.addChangingListener(listener);
		// /////Add value hour, minute,
		// second////
		// set adapter
		int curHour = calendar.get(Calendar.HOUR_OF_DAY);
		String hours[] = new String[] { "0時", "1時", "2時", "3時", "4時", "5時",
				"6時", "7時", "8時", "9時", "10時", "11時", "12時", "13時", "14時",
				"15時", "16時", "17時", "18時", "19時", "20時", "21時", "22時", "23時" };
		hour.setViewAdapter(new DateArrayAdapter(Mcontex, hours, curHour));
		// hour.setViewAdapter(new NumericWheelAdapter(Mcontex, 0, 23));
		int curMinute = calendar.get(Calendar.MINUTE);
		String minutes[] = new String[] { "00分", "01分", "02分", "03分", "04分",
				"05分", "06分", "07分", "08分", "09分", "10分", "11分", "12分", "13分",
				"14分", "15分", "16分", "17分", "18分", "19分", "20分", "21分", "22分",
				"23分", "24分", "25分", "26分", "27分", "28分", "29分", "30分", "31分",
				"32分", "33分", "34分", "35分", "36分", "37分", "38分", "39分", "40分",
				"41分", "42分", "43分", "44分", "45分", "46分", "47分", "48分", "49分",
				"50分", "51分", "52分", "53分", "54分", "55分", "56分", "57分", "58分",
				"59分" };
		minute.setViewAdapter(new DateArrayAdapter(Mcontex, minutes, curMinute));
		// minute.setViewAdapter(new NumericWheelAdapter(Mcontex, 0, 59,
		// "%02d"));
		// /second.setViewAdapter(new NumericWheelAdapter(Mcontex, 0, 59,
		// "%02d"));
		// Thiết lập thời gian hiện tại
		Calendar c = Calendar.getInstance();
		int curHours = c.get(Calendar.HOUR_OF_DAY);
		int curMinutes = c.get(Calendar.MINUTE);
		// int curSeconds = c.get(Calendar.SECOND);

		hour.setCurrentItem(curHours);
		minute.setCurrentItem(curMinutes);
		// second.setCurrentItem(curSeconds);
		// day
		updateDays(year, month, day, hour, minute);
		day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);

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
		calendar.set(Calendar.MONTH, calendar.get(Calendar.YEAR)+month.getCurrentItem());

		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		// day.setViewAdapter(new DateNumericAdapter(Mcontex, 1, maxDays,
		// calendar
		// .get(Calendar.DAY_OF_MONTH) - 1));
		int curDay = Math.min(maxDays, day.getCurrentItem() + 1);

		String days[] = new String[] { "1日", "2日", "3日", "4日", "5日",
				"6日", "7日", "8日", "9日", "10日", "11日", "12日", "13日", "14日",
				"15日", "16日", "17日", "18日", "19日", "20日", "21日", "22日", "23日",
				"24日", "25日", "26日", "27日", "28日", "29日", "30日", "31日" };
		day.setViewAdapter(new DateArrayAdapter(Mcontex, days, curDay));
		
		day.setCurrentItem(curDay - 1, true);
		calendar.set(Calendar.DAY_OF_MONTH, curDay);
		// //Add hour and minute ///
		calendar.set(Calendar.HOUR_OF_DAY, hour.getCurrentItem());
		calendar.set(Calendar.MINUTE, minute.getCurrentItem());
		// ////
		return calendar;

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
			// setTextSize(20);
		}

		@Override
		protected void configureTextView(TextView view) {
			super.configureTextView(view);
			if (currentItem == currentValue) {
				// set color current day
				//view.setTextColor(0xFF0000F0);
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
				//view.setTextColor(0xFF0000F0);
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

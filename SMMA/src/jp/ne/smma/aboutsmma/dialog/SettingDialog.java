package jp.ne.smma.aboutsmma.dialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import jp.ne.smma.R;
import jp.ne.smma.Ultis.ApplicationUntils;
import jp.ne.smma.Ultis.Constance;
import jp.ne.smma.aboutsmma.DAO.NotificationDataSource;
import jp.ne.smma.notification.ReceiverNotification;
import jp.ne.smma.wheel.ArrayWheelAdapter;
import jp.ne.smma.wheel.NumericWheelAdapter;
import jp.ne.smma.wheel.OnWheelChangedListener;
import jp.ne.smma.wheel.WheelView;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Setting dialog
 */
public class SettingDialog extends Dialog implements
		android.view.View.OnClickListener {
	// date time
	private int NoOfYear = 40;
	// activity
	public Activity c;
	// button
	private Button btnCancel;
	private Button btnComplete;
	private Button btnRemove;
	// radio button
	private RadioButton radioOneWeek;
	private RadioButton radioThreeDay;
	private RadioButton radioOneDay;
	private RadioButton radioAdvanced;
	// wheel view one
	private WheelView hourOne;
	private WheelView minuteOne;
	// wheel view two
	private WheelView year;
	private WheelView month;
	private WheelView day;
	private WheelView hour;
	private WheelView minute;
	// textview
	private TextView txtInfo;
	// calendar
	private Calendar calendar;
	// constructor
	private int idevent;
	private String nameEvent;
	private String strDateTime;
	private String endday;
	private String value = "1"; // 1: one week, 2: 3day, 3: 1day, 4:advanced
	private String note;
	// click code
	private Calendar mCalendar;
	private int hourSetting;
	private int minuteSetting;
	private String ideventlist;
	/**
	 * Constructor class
	 * 
	 * @param a
	 *            -Current activity
	 */
	public SettingDialog(Activity a, Calendar calendar, int idevent,
			String nameEvent, String strDateTime, String endday, String value,
			String note,String ideventlist) {
		super(a, android.R.style.Theme_DeviceDefault_Light_Dialog);
		// TODO Auto-generated constructor stub
		this.c = a;
		this.calendar = calendar;
		this.idevent = idevent;

		this.nameEvent = nameEvent;
		this.strDateTime = strDateTime;
		this.endday = endday;
		// this.value = value;
		this.note = note;
		this.ideventlist=ideventlist;

	}

	/**
	 * Event click dialog
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void onClick(View v) {
		if (v == btnCancel) {
			dismiss();
		}

		if (v == btnRemove) {
			if (checkValidate()) {
				showAlertDialog();
			} else {
				Toast.makeText(c, "You not set notification",
						Toast.LENGTH_SHORT).show();
			}

		} else if (v == btnComplete) {
			if (checkValidate()) {
				if (radioOneDay.isChecked() || radioOneWeek.isChecked()
						|| radioThreeDay.isChecked()) {
					mCalendar = updateSortDays(hourOne, minuteOne);
					hourSetting = mCalendar.getTime().getHours();
					minuteSetting = mCalendar.getTime().getMinutes();
					setDataForNotification(mCalendar, hourSetting,
							minuteSetting);
				} else {
					mCalendar = updateDays(year, month, day, hour, minute);
					hourSetting = mCalendar.getTime().getHours();
					minuteSetting = mCalendar.getTime().getMinutes();
					setDataForNotification(mCalendar, hourSetting,
							minuteSetting);
				}

				setTextTitle(c
						.getString(R.string.dialog_setting_title_complete));
			} else {
				Toast.makeText(c, "You not set notification",
						Toast.LENGTH_SHORT).show();
			}
		}

	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_dialog);
		// get data from xml
		ini();
		// set click
		setClick();
		// process db
		processCheckBox();
		// set calendar
		calendar = convertGetCalendar(endday);
		Log.e("SettingDialog", "EndDay: " + endday);
		Log.e("SettingDialog", "Calendar: " + calendar.getTime().toString());
		// set day
		iniFullDay(calendar);
		// set sort day
		intSortDay(calendar);
		// set click radio button
		setClickRadio();
	}

	/**
	 * Get data from xml
	 */
	private void ini() {
		// button
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnComplete = (Button) findViewById(R.id.btnSettingComplete);
		btnRemove = (Button) findViewById(R.id.btnRemove);
		// radio button
		radioOneWeek = (RadioButton) findViewById(R.id.checkOneWeek);
		radioThreeDay = (RadioButton) findViewById(R.id.checkThreeDay);
		radioOneDay = (RadioButton) findViewById(R.id.checkOneDay);
		radioAdvanced = (RadioButton) findViewById(R.id.checkAdvanced);
		// wheel view one
		hourOne = (WheelView) findViewById(R.id.hourOneDay);
		minuteOne = (WheelView) findViewById(R.id.minuteOneDay);
		// wheel view two
		year = (WheelView) findViewById(R.id.year);
		month = (WheelView) findViewById(R.id.month);
		day = (WheelView) findViewById(R.id.day);
		hour = (WheelView) findViewById(R.id.hour);
		minute = (WheelView) findViewById(R.id.minute);
		// text view
		txtInfo = (TextView) findViewById(R.id.infoDialog);
	}

	/**
	 * Set click for button
	 */
	private void setClick() {
		btnCancel.setOnClickListener(this);
		btnComplete.setOnClickListener(this);
		btnRemove.setOnClickListener(this);
	}

	/**
	 * Create sortday
	 * 
	 * @param calendar
	 */
	protected void intSortDay(Calendar calendar) {
		@SuppressWarnings("unused")
		OnWheelChangedListener listener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateSortDays(hourOne, minuteOne);

			}
		};

		// hour and minute
		hourOne.setViewAdapter(new NumericWheelAdapter(c, 0, 23, "%2d 時"));
		minuteOne.setViewAdapter(new NumericWheelAdapter(c, 0, 59, "%02d 分"));
		// Thiết lập thời gian hiện tại
		// Calendar c = Calendar.getInstance();
		int curHours = calendar.get(Calendar.HOUR_OF_DAY);
		int curMinutes = calendar.get(Calendar.MINUTE);

		hourOne.setCurrentItem(curHours);
		minuteOne.setCurrentItem(curMinutes);
	}

	/**
	 * Create full day adv
	 * 
	 * @param calendar
	 */
	protected void iniFullDay(Calendar calendar) {
		OnWheelChangedListener listener = new OnWheelChangedListener() {
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				updateDays(year, month, day, hour, minute);

			}
		};

		// month
		int curMonth = calendar.get(Calendar.MONTH);
		String months[] = new String[] { "1月", "2月", "3月", "4月", "5月", "6月",
				"7月", "8月", "9月", "10月", "11月", "12月" };
		month.setViewAdapter(new DateArrayAdapter(c, months, curMonth));
		month.setCurrentItem(curMonth);
		month.addChangingListener(listener);

		// Calendar cal = Calendar.getInstance();
		// year
		int curYear = calendar.get(Calendar.YEAR);
		int Year = calendar.get(Calendar.YEAR);

		year.setViewAdapter(new YearAdapter(c, Year - NoOfYear,
				Year + NoOfYear, NoOfYear));
		year.setCurrentItem(curYear - (Year - NoOfYear));
		year.addChangingListener(listener);

		// day
		updateDays(year, month, day, hour, minute);
		// day.setCurrentItem(calendar.getTime().getDay());
		day.setCurrentItem(calendar.get(Calendar.DAY_OF_MONTH) - 1);

		// hour and minute
		hour.setViewAdapter(new NumericWheelAdapter(c, 0, 23, "%2d 時"));
		minute.setViewAdapter(new NumericWheelAdapter(c, 0, 59, "%02d 分"));
		// Thiết lập thời gian hiện tại
		// Calendar c = Calendar.getInstance();
		int curHours = calendar.get(Calendar.HOUR_OF_DAY);
		int curMinutes = calendar.get(Calendar.MINUTE);
		// int curSeconds = c.get(Calendar.SECOND);

		hour.setCurrentItem(curHours);
		minute.setCurrentItem(curMinutes);

	}

	/*
	 * Update days
	 */
	Calendar updateSortDays(WheelView hour, WheelView minute) {
		// Calendar calendar = Calendar.getInstance();

		// //Add hour and minute ///
		calendar.set(Calendar.HOUR_OF_DAY, hour.getCurrentItem());
		calendar.set(Calendar.MINUTE, minute.getCurrentItem());
		return calendar;

	}

	/*
	 * Update sort days
	 */
	Calendar updateDays(WheelView year, WheelView month, WheelView day,
			WheelView hour, WheelView minute) {
		// Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR,
				calendar.get(Calendar.YEAR)
						+ (year.getCurrentItem() - NoOfYear));
		calendar.set(Calendar.MONTH, month.getCurrentItem());

		int maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		// day.setViewAdapter(new DayAdapter(c, 1, maxDays, calendar
		// .get(Calendar.DAY_OF_MONTH) - 1));
		// int curDay = Math.min(maxDays, day.getCurrentItem() + 1);
		// day.setCurrentItem(curDay - 1, true);
		// calendar.set(Calendar.DAY_OF_MONTH, curDay);
		Calendar calendar = Calendar.getInstance();
		day.setViewAdapter(new DayAdapter(c, 1, maxDays, calendar
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
			// if (index >= 0 && index < getItemsCount()) {
			int value = minValue + index;
			return String.format("%02d 年", value);
			// }
			// return null;
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
			int value = minValue + index;
			return String.format("%02d 日", value);
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

	/**
	 * Setup On off radio button
	 * 
	 * @param value
	 *            - 0: 1 week, 1: 3 day, 2: sameday, 3: advanced
	 */
	protected void setupOnOffRadioButton(int value) {
		switch (value) {
		case 0:
			// 1 week
			radioOneWeek.setChecked(true);
			radioThreeDay.setChecked(false);
			radioOneDay.setChecked(false);
			radioAdvanced.setChecked(false);
			break;
		case 1:
			// 3 day
			radioOneWeek.setChecked(false);
			radioThreeDay.setChecked(true);
			radioOneDay.setChecked(false);
			radioAdvanced.setChecked(false);
			break;
		case 2:
			// 1 day
			radioOneWeek.setChecked(false);
			radioThreeDay.setChecked(false);
			radioOneDay.setChecked(true);
			radioAdvanced.setChecked(false);
			break;
		case 3:
			// advanced
			radioOneWeek.setChecked(false);
			radioThreeDay.setChecked(false);
			radioOneDay.setChecked(false);
			radioAdvanced.setChecked(true);
			break;
		}
	}

	/**
	 * Set click radio
	 */
	protected void setClickRadio() {
		// week
		radioOneWeek.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setupOnOffRadioButton(0);
				txtInfo.setVisibility(View.GONE);
				Constance.strCheckBoxNotifiation = 3;
				value = "1";
			}
		});
		// 3 days
		radioThreeDay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setupOnOffRadioButton(1);
				txtInfo.setVisibility(View.GONE);
				Constance.strCheckBoxNotifiation = 2;
				value = "2";
			}
		});
		// day
		radioOneDay.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setupOnOffRadioButton(2);
				txtInfo.setVisibility(View.GONE);
				Constance.strCheckBoxNotifiation = 1;
				value = "3";
			}
		});
		// advanced
		radioAdvanced.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setupOnOffRadioButton(3);
				txtInfo.setVisibility(View.GONE);
				Constance.strCheckBoxNotifiation = 4;
				value = "4";
				//
			}
		});
	}

	/**
	 * Set text info title dialog
	 */
	protected void setTextTitle(String txtValue) {
		txtInfo.setVisibility(View.VISIBLE);
		txtInfo.setText(txtValue);

	}

	/**
	 * Show alert dialog
	 */
	protected void showAlertDialog() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(c);

		// set dialog message
		alertDialogBuilder
				.setMessage(c.getString(R.string.dialog_setting_dialog_confirm))
				.setCancelable(false)
				.setNegativeButton(c.getString(R.string.dialog_cancel),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, close
								// current activity
								dialog.cancel();
							}
						})
				.setPositiveButton(c.getString(R.string.dialog_ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								// delete database
								NotificationDataSource notificationSource = new NotificationDataSource(
										c);
								notificationSource.open();
								notificationSource.removeValueRecord(idevent);
								int idRecord = notificationSource
										.getIdViaIdEvent(idevent);
								notificationSource.close();
								// remove checkbox
								radioOneWeek.setChecked(false);
								radioThreeDay.setChecked(false);
								radioOneDay.setChecked(false);
								radioAdvanced.setChecked(false);
								// remove notifiaction
								Log.i("SettingDialog", "ID record: "+idRecord);
								Intent intent = new Intent(c,
										ReceiverNotification.class);
								PendingIntent pendingIntent = PendingIntent
										.getBroadcast(
												c,
												idRecord,
												intent,
												PendingIntent.FLAG_UPDATE_CURRENT);
								AlarmManager alarmManager = (AlarmManager) c
										.getSystemService(Context.ALARM_SERVICE);
								alarmManager.cancel(pendingIntent);
								// set ttile
								setTextTitle(c
										.getString(R.string.dialog_setting_title_remove));
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}

	/**
	 * Set data notification
	 */
	protected void setDataForNotification(Calendar dateandtime, int hour,
			int minute) {
		String checkSettting = "0";
		if (radioAdvanced.isChecked() || radioOneDay.isChecked()
				|| radioOneWeek.isChecked() || radioThreeDay.isChecked()) {
			checkSettting = "1";
		}
		// Calendar dateandtime = Calendar.getInstance(); //
		String fDate = new SimpleDateFormat("yyyy/MM/dd hh:mm")
				.format(dateandtime.getTime());
		// check data time
		fDate = previewDay(fDate, hour, minute);
		// save data database
		NotificationDataSource notificationSource = new NotificationDataSource(
				c);
		notificationSource.open();
		notificationSource.createNotification(idevent, nameEvent, strDateTime,
				endday, fDate, value, note, "1", checkSettting); // 1 have
																	// notification,
																	// 0
																	// no have
																	// notification
		// get id via id event
		int id = notificationSource.getIdViaIdEvent(idevent);

		notificationSource.close();
		// set notification
		ApplicationUntils.setNotification(c, fDate, calendar, id,nameEvent,ideventlist);
	}

	/**
	 * Preview day
	 * 
	 * @param oldday
	 * @return
	 */
	public String previewDay(String oldday, int hour, int minute) {
		Log.i("SettingDialog", "Start Date: " + oldday);
		Log.i("SettingDialog", "Start hour: " + hour);
		Log.i("SettingDialog", "Start minute: " + minute);
		Date oldDate = ApplicationUntils.converStringtoDate(oldday);
		java.text.DateFormat dateFormat = new SimpleDateFormat(
				"yyyy/MM/dd hh:mm");
		Calendar cal = Calendar.getInstance();
		cal.setTime(oldDate);
		switch (Constance.strCheckBoxNotifiation) {
		case 1:
			// 1 day
			cal.add(Calendar.DATE, -1);
			cal.set(Calendar.HOUR_OF_DAY, hour);
			cal.set(Calendar.MINUTE, minute);
			// cal.add(Calendar.HOUR_OF_DAY, hour);
			// cal.add(Calendar.MINUTE, minute);
			Log.i("SettingDialog", "Date: " + dateFormat.format(cal.getTime()));
			return dateFormat.format(cal.getTime());
		case 2:
			// 3 day
			cal.add(Calendar.DATE, -3);
			cal.set(Calendar.HOUR_OF_DAY, hour);
			cal.set(Calendar.MINUTE, minute);
			Log.i("SettingDialog", "Date: " + dateFormat.format(cal.getTime()));
			return dateFormat.format(cal.getTime());
		case 3:
			// 1 week

			cal.add(Calendar.DATE, -7);
			cal.set(Calendar.HOUR_OF_DAY, hour);
			cal.set(Calendar.MINUTE, minute);
			Log.i("SettingDialog", "Date: " + dateFormat.format(cal.getTime()));
			return dateFormat.format(cal.getTime());
		default:
			// nothing
			return oldday;
		}
	}

	/**
	 * Get choose day from DB
	 * 
	 * @return
	 */
	protected String getChooseDayDB() {
		String strChooseday;
		// get data from DB
		NotificationDataSource notificationSource = new NotificationDataSource(
				c);
		notificationSource.open();
		strChooseday = notificationSource.getDayChooseDay(idevent);
		notificationSource.close();
		//
		return strChooseday;
	}

	/**
	 * Get value day from DB
	 * 
	 * @return
	 */
	protected String getValueDayDB() {
		String strChooseday;
		// get data from DB
		NotificationDataSource notificationSource = new NotificationDataSource(
				c);
		notificationSource.open();
		strChooseday = notificationSource.getValueDay(idevent);
		notificationSource.close();
		//
		return strChooseday;
	}

	/**
	 * Process dialog when show dialog screen
	 */
	protected void processCheckBox() {
		String strChooseDay = getChooseDayDB();
		String strGetValue = getValueDayDB();
		Log.e("Setting Dialog", "strgetValue: "+strGetValue);
		// if have no data
		if (strChooseDay == null || strChooseDay.equals("")) {
			radioOneDay.setChecked(false);
			radioOneWeek.setChecked(false);
			radioThreeDay.setChecked(false);
			radioAdvanced.setChecked(false);
		} else {
			// have data
			if (strGetValue == null || strGetValue.equals("")) {
				Log.e("DialogSetting", "Data null");
			} else {
				if (strGetValue.equals("1")) {
					// 1 week
					setupOnOffRadioButton(0);
				} else if (strGetValue.equals("2")) {
					// 3 day
					setupOnOffRadioButton(1);
				} else if (strGetValue.equals("3")) {
					// 1 day
					setupOnOffRadioButton(2);
				} else if (strGetValue.equals("4")) {
					// advanced
					setupOnOffRadioButton(3);
				}
			}
		}
	}

	/**
	 * Convert day calendar
	 */
	protected Calendar convertGetCalendar(String endDay) {
		String strChooseDay = getChooseDayDB();
		Calendar cal = Calendar.getInstance();

		// if have no data
		if (strChooseDay == null || strChooseDay.equals("")) {
			Log.i("SettingDialog", "Choose day null convert calendar");
			try {
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
				cal.setTime(sdf1.parse(endDay));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
				cal.setTime(sdf.parse(strChooseDay));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// all done
		}
		return cal;

	}

	/**
	 * Check validate checkbox
	 */
	protected Boolean checkValidate() {
		if (radioAdvanced.isChecked() || radioOneDay.isChecked()
				|| radioOneWeek.isChecked() || radioThreeDay.isChecked()) {
			return true;
		} else {
			return false;
		}
	}
}

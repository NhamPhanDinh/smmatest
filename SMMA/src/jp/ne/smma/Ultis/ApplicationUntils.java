package jp.ne.smma.Ultis;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import jp.ne.smma.aboutsmma.DAO.NotificationDataSource;
import jp.ne.smma.aboutsmma.dialog.ChooseDateDialog;
import jp.ne.smma.notification.ReceiverNotification;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Class defind application until
 */
public class ApplicationUntils {
	static int id = 0;

	/**
	 * Go to Place detail Activity
	 */
	public static void gotoActivityPlaceDetail(Context context, Class<?> cls,
			String colorCode, String name, String id, double latutide,
			double longitude) {
		Intent intent = new Intent(context, cls);
		intent.putExtra(Constance.COLOR_ITEM_ABOUT, colorCode);
		intent.putExtra(Constance.COLOR_TEXT_INDEX_ABOUT, name);
		intent.putExtra(Constance.KEY_ABOUT_PLACE, id);
		intent.putExtra(Constance.LATITUDE_ABOUT, latutide);
		intent.putExtra(Constance.LONGITUDE_ABOUT, longitude);
		context.startActivity(intent);
	}

	/**
	 * Show dialog date at detail screen
	 * 
	 * @param strDateTime
	 *            - DateTime yyyy/MM/dd
	 */
	public static void showDialogChooseDateEventDetail(final Context mContext,
			final int idevent, final String nameEvent,
			final String strDateTime, final String endday, final String value,
			final String note) {
		final Calendar dateandtime = Calendar.getInstance(); // set location
		ChooseDateDialog dp = new ChooseDateDialog(mContext, dateandtime,
				new ChooseDateDialog.DatePickerListner() {

					@Override
					public void OnDoneButton(Dialog datedialog, Calendar c) {
						datedialog.dismiss();
						// check ON OFF setting
						String checkSettting = "1";
						if (!Constance.bCheckOnOff) {
							checkSettting = "0";
						}
						dateandtime.setTime(converStringtoDate(strDateTime));
						Log.i("", "You choose: " + c.getTime());
						String fDate = new SimpleDateFormat("yyyy/MM/dd hh:mm")
								.format(c.getTime());
						// check data time
						fDate = previewDaybyDay(fDate);
						// save data database
						NotificationDataSource notificationSource = new NotificationDataSource(
								mContext);
						notificationSource.open();
						notificationSource.createNotification(idevent,
								nameEvent, strDateTime, endday, fDate, value,
								note, "0", checkSettting,value); // 1 have
															// notification, 0
															// no have
															// notification

						notificationSource.close();
						// set notification
						// setNotification(mContext, fDate,c);

					}

					@Override
					public void OnCancelButton(Dialog datedialog) {
						// TODO Auto-generated method stub
						datedialog.dismiss();
					}
				});
		dp.show();
	}

	/**
	 * Convert String to Date via yyyy/MM/dd
	 * 
	 * @param dateString
	 * @return date
	 */
	public static Date converStringtoDate(String dateString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
		Date convertedDate = new Date();
		try {
			convertedDate = dateFormat.parse(dateString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertedDate;
	}

	/**
	 * Convert String to Date via yyyy/MM/dd
	 * 
	 * @param dateString
	 * @return date
	 */
	public static Date converStringtoDateNotifition(String dateString) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd hh:mm");
		Date convertedDate = new Date();
		try {
			convertedDate = dateFormat.parse(dateString);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return convertedDate;
	}

	/**
	 * Preview day when click checkbox
	 * 
	 * @param valueCheckbox
	 *            - 0,1,2,3 same 1day, 3 day, 1week equal
	 *            Constance.strCheckBoxNotifiation
	 * @param oldday
	 *            - old day yyyy/MM/dd
	 */
	public static String previewDaybyDay(String oldday) {
		Log.i("ApplicationUntil", "Old Date: " + oldday);
		Date oldDate = converStringtoDate(oldday);
		java.text.DateFormat dateFormat = new SimpleDateFormat(
				"yyyy/MM/dd hh:mm");
		Calendar cal = Calendar.getInstance();
		cal.setTime(oldDate);
		switch (Constance.strCheckBoxNotifiation) {
		case 1:
			// 1 day
			cal.add(Calendar.DATE, -1);
			Log.i("ApplicationUntil",
					"Date: " + dateFormat.format(cal.getTime()));
			return dateFormat.format(cal.getTime());
		case 2:
			// 3 day
			cal.add(Calendar.DATE, -3);
			Log.i("ApplicationUntil",
					"Date: " + dateFormat.format(cal.getTime()));
			return dateFormat.format(cal.getTime());
		case 3:
			// 1 week

			cal.add(Calendar.DATE, -7);
			Log.i("ApplicationUntil",
					"Date: " + dateFormat.format(cal.getTime()));
			return dateFormat.format(cal.getTime());
		default:
			// nothing
			return oldday;
		}
	}

	/**
	 * Set notification
	 * 
	 * @param datetime
	 */
	public static void setNotification(Context mContext, String time,
			Calendar calendar, int idEvent, String name, String ideventlist) {
		Log.i("Application Until", "Time: " + time);
		// go to receiver class
		Intent myIntent = new Intent(mContext, ReceiverNotification.class);
		// send intent
		myIntent.putExtra("itemId", ideventlist);
		myIntent.putExtra("name", name);
		// setup flag
		PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext,
				idEvent, myIntent, PendingIntent.FLAG_ONE_SHOT); // PendingIntent.FLAG_ONE_SHOT

		calendar = convertGetCalendar(time);
		// check calendar
		// Calendar cal = Calendar.getInstance();
		//
		// if (cal.after(calendar)) {
		//
		// }
		// set alarm
		@SuppressWarnings("static-access")
		AlarmManager alarmManager = (AlarmManager) mContext
				.getSystemService(mContext.ALARM_SERVICE);
		// set time
		Log.e("Application until", "Calendar notification: "
				+ calendar.getTime().toString());
		alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(),
				pendingIntent);
		// (AlarmManager.RTC, calendar.getTimeInMillis(),
		// AlarmManager.RTC_WAKEUP, pendingIntent);
		// alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(),
		// pendingIntent);
	}

	/**
	 * Convert day calendar
	 */
	protected static Calendar convertGetCalendar(String endDay) {
		Calendar cal = Calendar.getInstance();
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
			cal.setTime(sdf.parse(endDay));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}// all done
		return cal;

	}

	public static float distance(Context context, float x1, float y1,
			float x2, float y2) {
		float dx = x1 - x2;
		float dy = y1 - y2;
		float distanceInPx = (float) Math.sqrt(dx * dx + dy * dy);
		return pxToDp(context, distanceInPx);
	}

	public static float pxToDp(Context context, float px) {
		return px / context.getResources().getDisplayMetrics().density;
	}
}

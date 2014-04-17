package jp.ne.smma.EventList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jp.ne.smma.R;
import jp.ne.smma.Ultis.ApplicationUntils;
import jp.ne.smma.Ultis.Constance;
import jp.ne.smma.aboutsmma.DAO.NotificationDataSource;
import jp.ne.smma.aboutsmma.DTO.EventNotificationItem;
import jp.ne.smma.aboutsmma.DTO.NotificationItem;
import jp.ne.smma.aboutsmma.adapter.ListEventNotificationAdapter;
import jp.ne.smma.aboutsmma.dialog.ChooseDateDialog;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * List Event notification
 * 
 */
public class ListEventNotificationActivity extends Activity {
	/* Calendar show dialog */
	private Calendar dateandtime;

	private ListView listViewEventNotification;

	ListEventNotificationAdapter adapter;

	List<EventNotificationItem> rowItems;
	NotificationDataSource notificationSource;
	List<NotificationItem> notificationItem;
	ArrayList<String> getStatus;
	ArrayList<Boolean> checkNotification = new ArrayList<Boolean>();
	ProgressDialog pDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_event_notification);
		// get data from XML
		listViewEventNotification = (ListView) findViewById(R.id.listViewEventNotification);

		// load data
		pDialog = new ProgressDialog(ListEventNotificationActivity.this);
		new loadDataEventNotification().execute("");
		// set click item listview
		listViewEventNotification
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View v,
							int pos, long id) {
						// TODO Auto-generated method stub
						showDialogChooseDate(Constance.countDatabaseNotification
								+ pos);
					}
				});
	}

	/**
	 * Show dialog date
	 * 
	 * @param v
	 *            - View
	 */
	protected void showDialogChooseDate(final int id) {
		dateandtime = Calendar.getInstance(); // set location
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		Date date;
		try {

			date = format.parse(getChooseday(id));
			dateandtime.setTime(date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ChooseDateDialog dp = new ChooseDateDialog(
				ListEventNotificationActivity.this, dateandtime,
				new ChooseDateDialog.DatePickerListner() {

					@Override
					public void OnDoneButton(Dialog datedialog, Calendar c) {
						datedialog.dismiss();

						dateandtime.set(Calendar.YEAR, c.get(Calendar.YEAR));
						dateandtime.set(Calendar.MONTH, c.get(Calendar.MONTH));
						dateandtime.set(Calendar.DAY_OF_MONTH,
								c.get(Calendar.DAY_OF_MONTH));
						String fDate = new SimpleDateFormat("yyyy/MM/dd")
								.format(c.getTime());
						// update date
						notificationSource = new NotificationDataSource(
								ListEventNotificationActivity.this);
						notificationSource.open();
						Boolean bCheck = notificationSource.updateChooseDay(
								fDate, id);
						notificationSource.close();
						// show log
						if (bCheck) {
							Toast.makeText(ListEventNotificationActivity.this,
									"Update day sucess", Toast.LENGTH_SHORT)
									.show();
						} else {
							Toast.makeText(ListEventNotificationActivity.this,
									"Update day not sucess. Please try again",
									Toast.LENGTH_SHORT).show();
						}
						// set notification
//						ApplicationUntils.setNotification(
//								ListEventNotificationActivity.this, fDate);
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
	 * Assytask load data from database
	 */
	private class loadDataEventNotification extends
			AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String... params) {
			// add data list view
			notificationSource = new NotificationDataSource(
					ListEventNotificationActivity.this);
			notificationSource.open();
			notificationItem = notificationSource.getAllNotification();
			// get status
			getStatus = notificationSource.getStatus();
			notificationSource.close();
			// check check box
			for (int i = 0; i < getStatus.size(); i++) {
				if (getStatus.get(i).equals("1")) {
					// if equal true
					checkNotification.add(true);
				} else {
					checkNotification.add(false);
				}
			}
			// fill data
			rowItems = new ArrayList<EventNotificationItem>();
			for (int i = 0; i < notificationItem.size(); i++) {
				EventNotificationItem item = new EventNotificationItem(
						notificationItem.get(i).getName(), notificationItem
								.get(i).getNote(), notificationItem.get(i)
								.getValue(), checkNotification.get(i));
				rowItems.add(item);
			}

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (pDialog != null) {
				pDialog.dismiss();
			}
			adapter = new ListEventNotificationAdapter(
					ListEventNotificationActivity.this,
					R.layout.item_list_event_notification, rowItems);
			listViewEventNotification.setAdapter(adapter);

		}

		@Override
		protected void onPreExecute() {

			pDialog.setMessage("Loading data...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (pDialog != null) {
			pDialog.dismiss();
		}
	}

	/**
	 * Get choose day from database
	 * 
	 * @param id
	 * @return chooseday value
	 */
	protected String getChooseday(int id) {
		String strDay = null;
		notificationSource = new NotificationDataSource(
				ListEventNotificationActivity.this);
		notificationSource.open();
		strDay = notificationSource.getDayChooseDay(id);
		notificationSource.close();
		return strDay;
	}
}

package jp.ne.smma.EventList;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import jp.ne.smma.R;
import jp.ne.smma.aboutsmma.DAO.NotificationDataSource;
import jp.ne.smma.aboutsmma.DTO.EventNotificationItem;
import jp.ne.smma.aboutsmma.DTO.NotificationItem;
import jp.ne.smma.aboutsmma.adapter.ListEventNotificationAdapter;
import jp.ne.smma.aboutsmma.dialog.ChooseDateDialog;
import jp.ne.smma.aboutsmma.dialog.SettingDialog;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
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

	private Button btnCancel;
	private Button btnDelete;
	ArrayList<String> checkedValue = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_event_notification);
		// get data from XML
		listViewEventNotification = (ListView) findViewById(R.id.listViewEventNotification);

		// xml
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnDelete = (Button) findViewById(R.id.btnDelete);
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
						// showDialogChooseDate(Constance.countDatabaseNotification
						// + pos);
						//
						// //get data
						// for (int i = 0; i < notificationItem.size(); i++) {
						// if (pos==notificationItem.get(i).) {
						//
						// }
						// }

						int idevent = notificationItem.get(pos).getId_event();
						String dateEnd = notificationItem.get(pos).getEndday();
						String dateFrom = notificationItem.get(pos)
								.getStartday();
						String strName = notificationItem.get(pos).getName();
						String strTitle = notificationItem.get(pos)
								.getValueNotification();
						String strDate = notificationItem.get(pos).getNote();
						String ideventlist = notificationItem.get(pos)
								.getId_event() + "";

						Log.i("", "IDEvent: " + idevent);
						Log.i("", "dateEnd: " + dateEnd);
						Log.i("", "dateFrom: " + dateFrom);
						Log.i("", "strName: " + strName);
						Log.i("", "strTitle: " + strTitle);
						Log.i("", "strDate: " + strDate);
						Log.i("", "ideventlist: " + ideventlist);
						// show popup
						java.text.DateFormat df = new java.text.SimpleDateFormat(
								"yyyy年MM月dd日"); // old M
						java.text.DateFormat df1 = new java.text.SimpleDateFormat(
								"yyyy/MM/dd");
						df.setTimeZone(java.util.TimeZone.getDefault());
						try {
							java.util.Date dateE = df1.parse(dateEnd);
							java.util.Date dateF = df1.parse(dateFrom);

							// show notification
							final Calendar dateandtime = Calendar.getInstance();
							SettingDialog setting = new SettingDialog(
									ListEventNotificationActivity.this,
									dateandtime, idevent, strName, df1
											.format(dateF), df1.format(dateE),
									strTitle, strDate, ideventlist);
							setting.show();
						} catch (java.text.ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				});
		// set click button
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		btnDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showDialogDelete();
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
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm");
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
						dateandtime.set(Calendar.HOUR_OF_DAY,
								c.get(Calendar.HOUR_OF_DAY));
						dateandtime.set(Calendar.MINUTE, c.get(Calendar.MINUTE));
						String fDate = new SimpleDateFormat("yyyy/MM/dd hh:mm")
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
						// ApplicationUntils.setNotification(
						// ListEventNotificationActivity.this, fDate);
					}

					@Override
					public void OnCancelButton(Dialog datedialog) {
						// TODO Auto-generated method stub
						datedialog.dismiss();
					}
				});
		dp.setCancelable(false);
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
			Log.e("", "notificationItem.size(): "+notificationItem.size());
			for (int i = 0; i < notificationItem.size(); i++) {
				// check have 1 - true
				if (notificationItem.get(i).getStatus().equals("1")) {
					Log.e("", "i: "+i);
					Log.e("", "id: "+notificationItem.get(i).getId());
					EventNotificationItem item = new EventNotificationItem(
							notificationItem.get(i).getName(), notificationItem
									.get(i).getNote(), notificationItem.get(i)
									.getValueNotification(),
							checkNotification.get(i));
					rowItems.add(item);
				}

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
			// set click
			// listViewEventNotification
			// .setOnItemClickListener(new OnItemClickListener() {
			//
			// @Override
			// public void onItemClick(AdapterView<?> arg0, View v,
			// int pos, long index) {
			// // TODO Auto-generated method stub
			// CheckBox cb = (CheckBox) v
			// .findViewById(R.id.checkEvent);
			// if (cb.isChecked()) {
			//
			// checkedValue.add("1"); // true
			// } else if (!cb.isChecked()) {
			// checkedValue.remove("0"); // false
			// }
			// }
			// });

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
		strDay = notificationSource.getDayChooseDayIdRecord(id);
		notificationSource.close();
		return strDay;
	}

	/**
	 * Alert dialog
	 */
	protected void showDialogDelete() {
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(
				ListEventNotificationActivity.this);

		// Setting Dialog Title
		alertDialog.setCancelable(false);

		// Setting Dialog Message
		alertDialog.setMessage("イベントをクリアします。よろしいですか？");

		// Setting Positive "Yes" Button
		alertDialog.setPositiveButton("削除",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						//check and remove
						for (int i = 0; i < ListEventNotificationAdapter.checkbox.size(); i++) {
							
							if (ListEventNotificationAdapter.checkbox.get(i).equals("1")) {
								Log.i("", "oooooo: "+ListEventNotificationAdapter.checkbox.get(i));
								notificationSource = new NotificationDataSource(ListEventNotificationActivity.this);
								notificationSource.open();
								notificationSource.updateStatus("1", i);
								notificationSource.close();
							}else{
								Log.i("", "ooooDDD: "+ListEventNotificationAdapter.checkbox.get(i));
								notificationSource = new NotificationDataSource(ListEventNotificationActivity.this);
								notificationSource.open();
								 notificationSource.updateStatus("0", i);
								notificationSource.close();
							}
						}
						
						// log ressult

						for (int i = 0; i < checkedValue.size(); i++) {
							Log.i("ListEventNotification", "Value: "
									+ checkedValue.get(i));
						}
						// update listview
						// fill data
//						rowItems = new ArrayList<EventNotificationItem>();
//						for (int i = 0; i < notificationItem.size(); i++) {
//							// check have 1 - true
//							if (notificationItem.get(i).getStatus().equals("1")) {
//								EventNotificationItem item = new EventNotificationItem(
//										notificationItem.get(i).getName(),
//										notificationItem.get(i).getNote(),
//										notificationItem.get(i)
//												.getValueNotification(),
//										checkNotification.get(i));
//								rowItems.add(item);
//							}
//						}
//						adapter = new ListEventNotificationAdapter(
//								ListEventNotificationActivity.this,
//								R.layout.item_list_event_notification, rowItems);
//						listViewEventNotification.setAdapter(adapter);
						adapter.notifyDataSetChanged();
						listViewEventNotification.invalidateViews();
//						listViewEventNotification.notify();
						listViewEventNotification.invalidate();
						dialog.cancel();
						// toast show
						Toast toast = Toast.makeText(ListEventNotificationActivity.this, "イベントをクリアしました。", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.FILL_HORIZONTAL|Gravity.CENTER_VERTICAL, 0, 0);
						toast.setDuration(Toast.LENGTH_SHORT);
						toast.getView().setBackgroundColor(Color.parseColor("#FFE599"));
						toast.show();
					}
				});

		// Setting Negative "NO" Button
		alertDialog.setNegativeButton("戻る",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						// Write your code here to invoke NO event
						dialog.cancel();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}
}

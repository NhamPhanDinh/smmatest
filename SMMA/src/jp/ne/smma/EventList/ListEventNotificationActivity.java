package jp.ne.smma.EventList;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import jp.ne.smma.R;
import jp.ne.smma.aboutsmma.DTO.EventNotificationItem;
import jp.ne.smma.aboutsmma.adapter.ListEventNotificationAdapter;
import jp.ne.smma.aboutsmma.dialog.ChooseDateDialog;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class ListEventNotificationActivity extends Activity {
	/* Calendar show dialog */
	private Calendar dateandtime;

	private ListView listViewEventNotification;

	/* Adapter content listview */
	public static final String[] strContent = new String[] { "仙台文学館", "仙台市天文台",
			"仙台市歴史民俗資料館", "仙台市八木山動物公園", "せんだいメディアテーク", "宮城県美術館", "仙台市縄文の森広場",
			"仙台市科学館", "仙台市博物館", "SMMA", "東北大学総合学術博物館", "地底の森ミュージアム仙台市富沢遺跡保存館",
			"東北福祉大学芹沢銈介美術工芸館" };

	ListEventNotificationAdapter adapter;

	List<EventNotificationItem> rowItems;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_event_notification);
		// get data from XML
		listViewEventNotification = (ListView) findViewById(R.id.listViewEventNotification);
		// add data list view
		rowItems = new ArrayList<EventNotificationItem>();
		for (int i = 0; i < strContent.length; i++) {
			EventNotificationItem item = new EventNotificationItem(
					strContent[i], "TestFunction", strContent[i], false);
			rowItems.add(item);
		}

		adapter = new ListEventNotificationAdapter(
				ListEventNotificationActivity.this,
				R.layout.item_list_event_notification, rowItems);
		listViewEventNotification.setAdapter(adapter);
		// set click item listview
		listViewEventNotification
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View v,
							int pos, long id) {
						// TODO Auto-generated method stub
						showDialogChooseDate(v);
					}
				});
	}

	/**
	 * Show dialog date
	 * 
	 * @param v
	 *            - View
	 */
	protected void showDialogChooseDate(final View v) {
		dateandtime = Calendar.getInstance(); // set location
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
						// set value when click here
						// ((Button)v).setText(new
						// SimpleDateFormat("MMMM dd yyyy")
						// .format(c.getTime()));
						Toast.makeText(getApplicationContext(),
								"You choose: " + c.getTime(),
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void OnCancelButton(Dialog datedialog) {
						// TODO Auto-generated method stub
						datedialog.dismiss();
					}
				});
		dp.show();
	}
}

package jp.ne.smma.aboutsmma.adapter;

import java.util.List;

import jp.ne.smma.R;
import jp.ne.smma.aboutsmma.DAO.NotificationDataSource;
import jp.ne.smma.aboutsmma.DTO.EventNotificationItem;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

/**
 * List event notification adapter
 */
public class ListEventNotificationAdapter extends
		ArrayAdapter<EventNotificationItem> {

	Context context;
	NotificationDataSource notificationSource;
	ProgressDialog pDialog;
	String value = "0";
	int id;

	/**
	 * Cosntructor
	 * 
	 * @param context
	 * @param resourceId
	 * @param items
	 */
	public ListEventNotificationAdapter(Context context, int resourceId,
			List<EventNotificationItem> items) {
		super(context, resourceId, items);
		this.context = context;
	}

	/* private view holder class */
	public class ViewHolder {
		CheckBox checkbox;
		TextView txtContentOne;
		TextView txtContentTwo;
		TextView txtContentThree;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		EventNotificationItem rowItem = getItem(position);

		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		if (convertView == null) {
			convertView = mInflater.inflate(
					R.layout.item_list_event_notification, null);
			holder = new ViewHolder();

			holder.txtContentOne = (TextView) convertView
					.findViewById(R.id.txtTextOne);
			holder.txtContentTwo = (TextView) convertView
					.findViewById(R.id.txtTextTwo);
			holder.txtContentThree = (TextView) convertView
					.findViewById(R.id.txtTextThree);
			holder.checkbox = (CheckBox) convertView
					.findViewById(R.id.checkEvent);
			convertView.setTag(holder);
		} else
			holder = (ViewHolder) convertView.getTag();
		/* Set item */
		holder.txtContentOne.setText(rowItem.getStrTextOne());
		holder.txtContentTwo.setText(rowItem.getStrTextTwo());
		holder.txtContentThree.setText(rowItem.getStrTextThree());
		holder.checkbox.setChecked(rowItem.isSelected());
		holder.checkbox.setTag(rowItem);
		/* Event click checkbox */
		holder.checkbox.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				CheckBox cb = (CheckBox) v;
				EventNotificationItem country = (EventNotificationItem) cb
						.getTag();

				// load data
				pDialog = new ProgressDialog(context);
				// check data
				if (cb.isChecked()) {
					value = "1";
				} else {
					value = "0";
				}
				id = position;
				new updateStatus().execute("");
				// click check
				country.setSelected(cb.isChecked());

			}
		});

		return convertView;
	}

	/**
	 * Assytask update status
	 */
	private class updateStatus extends AsyncTask<String, String, String> {
		private boolean bCheck = false;

		@Override
		protected String doInBackground(String... params) {
			// add data list view
			notificationSource = new NotificationDataSource(context);
			notificationSource.open();
			bCheck = notificationSource.updateStatus(value, id);
			notificationSource.close();
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (pDialog != null) {
				pDialog.dismiss();
			}
			if (bCheck) {
				Toast.makeText(context, "Change notification success",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(context,
						"Change notification fail, please try again",
						Toast.LENGTH_LONG).show();
			}

		}

		@Override
		protected void onPreExecute() {

			pDialog.setMessage("Updating data...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
		}

	}

}

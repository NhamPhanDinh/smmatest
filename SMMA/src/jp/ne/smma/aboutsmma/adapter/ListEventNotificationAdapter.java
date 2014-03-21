package jp.ne.smma.aboutsmma.adapter;

import java.util.List;

import jp.ne.smma.R;
import jp.ne.smma.aboutsmma.DTO.EventNotificationItem;
import android.app.Activity;
import android.content.Context;
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

	public View getView(int position, View convertView, ViewGroup parent) {
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
				Toast.makeText(
						context,
						"Clicked on Checkbox: " + cb.getText() + " is "
								+ cb.isChecked(), Toast.LENGTH_LONG).show();
				country.setSelected(cb.isChecked());
			}
		});

		return convertView;
	}
}

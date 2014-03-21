package jp.ne.smma.EventCalendar.Custom;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import jp.ne.smma.R;
import jp.ne.smma.EventList.EventCalendarFragment;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HorzGridViewAdapter extends BaseAdapter {
	List<String> listDay;
	List<String> listDayOfWeek;
	HashMap<String, Integer> hm_DayOfWeek;
	LayoutInflater inflater;
	Context mContext;

	// HorzGridView stuff
	private int columns;// Used to set childSize in TwoWayGridView
	private int rows;// used with TwoWayGridView
	private int itemPadding;
	public int columnWidth;
	public int rowHeight;

	public HorzGridViewAdapter(List<String> listDay,
			List<String> listDayOfWeek, HashMap<String, Integer> hm_DayOfWeek,
			LayoutInflater inflater, Context mContext) {
		super();
		this.listDay = listDay;
		this.listDayOfWeek = listDayOfWeek;
		this.hm_DayOfWeek = hm_DayOfWeek;
		this.inflater = LayoutInflater.from(mContext);
		this.mContext = mContext;
		
		if (this.inflater==null) {
			Log.i("aaaa", "fuck sdsdasdsadsadsa");
		}
		// Get dimensions from values folders; note that the value will change
		// based on the device size but the dimension name will remain the same
		Resources res = mContext.getResources();
		itemPadding = (int) res.getDimension(R.dimen.horz_item_padding);
		int[] rowsColumns = res.getIntArray(R.array.horz_gv_rows_columns);
		rows = 1;
		columns = listDay.size();

		// Initialize the layout params
		EventCalendarFragment.mListView.setNumRows(rows);

		// HorzGridView size not established yet, so need to set it using a
		// viewtreeobserver
		ViewTreeObserver vto = EventCalendarFragment.mListView.getViewTreeObserver();

		OnGlobalLayoutListener onGlobalLayoutListener = new OnGlobalLayoutListener() {

			@SuppressWarnings("deprecation")
			@SuppressLint("NewApi")
			@Override
			public void onGlobalLayout() {
				// First use the gridview height and width to determine child
				// values
				// rowHeight = (int) ((float)
				// (EventCalendarActivity.mListView.getHeight() / rows) - 2 *
				// itemPadding);
				rowHeight = LayoutParams.MATCH_PARENT;
				columnWidth = (int) ((float) (EventCalendarFragment.mListView.getWidth() / columns) - 2 * itemPadding);

				EventCalendarFragment.mListView.setRowHeight(rowHeight);

				// Then remove the listener
				ViewTreeObserver vto = EventCalendarFragment.mListView
						.getViewTreeObserver();

				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
					vto.removeOnGlobalLayoutListener(this);
				} else {
					vto.removeGlobalOnLayoutListener(this);
				}

			}
		};

		vto.addOnGlobalLayoutListener(onGlobalLayoutListener);

		// ///////////////get adapter listview/////////////////////
		// rowItems = new ArrayList<RowItem>();
		// for (int i = 0; i < strContentOne.length; i++) {
		// RowItem item = new RowItem(imagesLine[i], imagesItem[i],
		// strContentOne[i], strContentTwo[i]);
		// rowItems.add(item);
		// }
		//
		// adapter = new CustomListViewAdapter(mContext,
		// R.layout.item_list_item,
		// rowItems);
		// ListView listView = (ListView) v.findViewById(R.id.listContent);
		// listView.setAdapter(adapter);
		// ////////////////////////////////////////////////////////
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		View myView = null;
		if (inflater==null) {
			Log.i("aaaa", "dsadsadasdsadsad");
		}
		if (v == null || myView ==null) {
			v = inflater.inflate(R.layout.item_date, parent, false);
			myView = inflater.inflate(R.layout.item_content,parent, false);
		}
		int keyDay = hm_DayOfWeek.get(listDayOfWeek.get(position));
		
//		TextView txtContent = (TextView)myView.findViewById(R.id.tv_item_content);
		LinearLayout linearItem = (LinearLayout)v.findViewById(R.id.linearItem);
		linearItem.addView(myView);
		
		TextView tv_day = (TextView) v.findViewById(R.id.tv_day);
		TextView tv_day_of_week = (TextView) v
				.findViewById(R.id.tv_day_of_week);
		tv_day.setText(listDay.get(position));
		if (keyDay == 7) {
			tv_day_of_week.setBackgroundResource(R.drawable.bg_saturday);
		} else if (keyDay == 1) {
			tv_day_of_week.setBackgroundResource(R.drawable.bg_sunday);
		} else {
			tv_day_of_week.setBackgroundColor(Color.WHITE);
		}
		tv_day_of_week.setText(listDayOfWeek.get(position));
		// get data from xml

		
		return v;

	}

	@Override
	public int getCount() {

		return listDayOfWeek.size();
	}

	@Override
	public Object getItem(int position) {

		return listDayOfWeek.get(position);
	}

	@Override
	public long getItemId(int position) {

		return 0;
	}

}

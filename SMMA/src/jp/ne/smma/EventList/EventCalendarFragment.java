package jp.ne.smma.EventList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import jp.ne.smma.R;
import jp.ne.smma.EventCalendar.Controller.TwoWayAbsListView;
import jp.ne.smma.EventCalendar.Custom.HorzGridViewAdapter;
import jp.ne.smma.EventCalendar.Custom.MyGridView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

public class EventCalendarFragment extends Fragment {

	// public static TwoWayGridView mListView;
	public static MyGridView mListView;
	TextView tv_month;
	LayoutInflater inflater;
	HorzGridViewAdapter adapter;

	GridView grid;

	Calendar mCalendar;

	List<String> listDay = new ArrayList<String>();// mảng chứa ngày của tháng
	HashMap<String, Integer> hm_DayOfMonth = new HashMap<String, Integer>();// lưu
																			// thứ
																			// và
																			// key
																			// tương
																			// ứng
																			// của
																			// nó
	List<String> listDayOfWeek = new ArrayList<String>();

	// -------------------------------------------------------//
	// ------mảng chứa các kiểu ngày của các tháng---//
	List<String> list28 = new ArrayList<String>();
	List<String> list29 = new ArrayList<String>();
	List<String> list30 = new ArrayList<String>();
	List<String> list31 = new ArrayList<String>();
	// ------------------------------------------------------//

	// ngày tháng năm hiện tại
	public static int current_Month;
	public static int current_Year;
	public static int current_Day;


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.event_calendar,
				container, false);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		setContentView(R.layout.event_calendar);
		// set time zone japan
		// mCalendar= new GregorianCalendar(TimeZone.getTimeZone("Japan"));
		mCalendar = Calendar.getInstance();
		Date dateCurrent = mCalendar.getTime();
		SimpleDateFormat dayFormat = new SimpleDateFormat("MMMM", Locale.JAPAN);
		String dayCurrent = dayFormat.format(dateCurrent);
		
		createList();
		getCurrentDate();
		fitDayInMonth(current_Month);
		getListDayOfWeek();

		// Log.d("day", listDay.length+"");
		// Log.d("dow", listDayOfWeek.length+"");
		//
		tv_month = (TextView)rootView. findViewById(R.id.tv_month);
		tv_month.setText(dayCurrent);

		// mListView = (TwoWayGridView) findViewById(R.id.horz_gridview);
		mListView = (MyGridView) rootView.findViewById(R.id.horz_gridview);
		LayoutInflater layoutInflater = (LayoutInflater)getActivity().getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		adapter = new HorzGridViewAdapter(listDay, listDayOfWeek,
				hm_DayOfMonth, layoutInflater, getActivity());
		mListView.setAdapter(adapter);
		mListView.setSelection(current_Day - 1);
		mListView.setOnScrollListener(new TwoWayAbsListView.OnScrollListener() {

			@Override
			public void onScrollStateChanged(TwoWayAbsListView view,
					int scrollState) {
				// TODO Auto-generated method stub
				// if(scrollState==0)
				// Toast.makeText(getApplicationContext(), "item ="+scrollState,
				// Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onScroll(TwoWayAbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if (firstVisibleItem + visibleItemCount == totalItemCount) {
					nextMonth();
				}
			}
		});
		float scalefactor = getResources().getDisplayMetrics().density * 100;
		int number = getActivity().getWindowManager().getDefaultDisplay().getWidth();
		int columns = (int) ((float) number / (float) scalefactor);
		Log.i("Column", "Column: " + columns);
		mListView.setNumColumns(columns);

		// add row
		// LayoutInflater factory = LayoutInflater.from(this);
		// View myView = factory.inflate(R.layout.item_content, null);
		// mListView.addHeaderView(myView,);
		return rootView;
	}

	/*
	 * khởi tạo danh sách ngày
	 */
	void createList() {
		// list28 = getResources().getStringArray(R.array.list28);
		// list29 = getResources().getStringArray(R.array.list29);
		// list30 = getResources().getStringArray(R.array.list30);
		// list31 = getResources().getStringArray(R.array.list31);

		for (int i = 1; i <= 28; i++) {
			list28.add(i + "");
		}
		for (int i = 1; i <= 29; i++) {
			list29.add(i + "");
		}
		for (int i = 1; i <= 30; i++) {
			list30.add(i + "");
		}
		for (int i = 1; i <= 31; i++) {
			list31.add(i + "");
		}
	}

	int pos_listDay = 0;

	/**
	 * Tháng tiếp theo
	 */
	void nextMonth() {
		pos_listDay = listDay.size();
		if (current_Month < 11) {
			current_Month++;
		} else {
			current_Month = 0;
			current_Year++;
		}

		// settext title
		mCalendar.set(current_Year, current_Month, 1);
		Long time = mCalendar.getTimeInMillis();
		Date dateCurrent = new Date(time);
		SimpleDateFormat dayFormat = new SimpleDateFormat("MMMM", Locale.JAPAN);
		String dayCurrent = dayFormat.format(dateCurrent);
		tv_month.setText(dayCurrent + "-" + current_Year);

		fitDayInMonth(current_Month);
		getListDayOfWeek();
		adapter = new HorzGridViewAdapter(listDay, listDayOfWeek,
				hm_DayOfMonth, inflater, getActivity());
		mListView.setAdapter(adapter);
		mListView.setSelection(pos_listDay - 1);
	}

	// protected void setPreviousMonth() {
	// if (month.get(GregorianCalendar.MONTH) == month
	// .getActualMinimum(GregorianCalendar.MONTH)) {
	// month.set((month.get(GregorianCalendar.YEAR) - 1),
	// month.getActualMaximum(GregorianCalendar.MONTH), 1);
	// } else {
	// month.set(GregorianCalendar.MONTH,
	// month.get(GregorianCalendar.MONTH) - 1);
	// }
	//
	// }
	/*
	 * lấy ngày tháng năm hiện tại
	 */
	void getCurrentDate() {
		current_Year = mCalendar.get(Calendar.YEAR);
		current_Month = mCalendar.get(Calendar.MONTH);
		current_Day = mCalendar.get(Calendar.DAY_OF_MONTH);
		// current_Year = 2014;
		// current_Month = 2;
		// current_Day = 3;
	}

	/*
	 * get day of week by date
	 */
	String getDayOfWeek_ByDate(int year, int month, int day) {
		// month--;
		// day--;
		mCalendar.set(year, month, day);
		Long time = mCalendar.getTimeInMillis();
		Date date = new Date(time);
		SimpleDateFormat formatDayOfWeek = new SimpleDateFormat("EEE",
				Locale.JAPAN);
		return formatDayOfWeek.format(date);
	}

	/*
	 * get list day of week
	 */
	void getListDayOfWeek() {
		for (int i = pos_listDay; i < listDay.size(); i++) {
			int day = Integer.parseInt(listDay.get(i));
			String dayweek = getDayOfWeek_ByDate(current_Year, current_Month,
					day);
			listDayOfWeek.add(dayweek);
			mCalendar.set(current_Year, current_Month, day);

			hm_DayOfMonth.put(dayweek, mCalendar.get(Calendar.DAY_OF_WEEK));
		}
	}

	// danh sach ngay cho tung tháng
	void fitDayInMonth(int month) {
		month++;
		if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8
				|| month == 10 || month == 12) {
			for (String st : list31) {

				listDay.add(st);
			}
		}

		if (month == 4 || month == 6 || month == 9 || month == 11) {
			for (String st : list30) {

				listDay.add(st);
			}
		}

		if (month == 2) {
			if (Nhuan(current_Year)) {
				for (String st : list29) {
					listDay.add(st);
				}
			} else {
				for (String st : list28) {
					listDay.add(st);
				}
			}
		}
	}

	/*
	 * Kiểm tra năm nhuân
	 */
	boolean Nhuan(int year) {
		if (year % 4 == 0) {
			return true;
		} else {
			return false;
		}
	}

//	@Override
//	public void onBackPressed() {
//		// TODO Auto-generated method stub
//		Intent i = new Intent(getApplicationContext(),EventListActivity.class);
//		startActivity(i);
//		finish();
//		super.onBackPressed();
//	}
	
}

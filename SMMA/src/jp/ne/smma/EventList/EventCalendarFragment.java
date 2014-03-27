package jp.ne.smma.EventList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import jp.ne.smma.R;
import jp.ne.smma.EventCalendar.Custom.CalendarView;
import jp.ne.smma.EventList.Controller.GetDataEventCalendar;
import jp.ne.smma.Ultis.MonthInfo;
import jp.ne.smma.aboutsmma.DTO.ItemCalendar;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Scroller;

public class EventCalendarFragment extends Fragment implements OnTouchListener {
	/** Called when the activity is first created. */
	Calendar mCalendar;
	int pos_listDay = 0;
	List<String> listDay;
	HashMap<String, Integer> hm_DayOfMonth;
	List<String> listDayOfWeek;
	ArrayList<MonthInfo> listMonthInfor = new ArrayList<MonthInfo>();
	// -------------------------------------------------------//
	// ------mảng chứa các kiểu ngày của các tháng---//
	List<String> list28;
	List<String> list29;
	List<String> list30;
	List<String> list31;
	// ------------------------------------------------------//

	// ngày tháng năm hiện tại
	public static int current_Month;
	public static int current_Year;
	public static int current_Day;

	private float _xDelta;
	private float _yDelta;
	CalendarView viewLabel;
	CalendarView viewContent;
	Scroller mScroller;
	private GestureDetector gestureDetector;
	private ValueAnimator mScrollAnimator;
	boolean checkRightLeft = false;
	private int month_lenght = 80; // View month height

	GetDataEventCalendar getDataEvent;
	List<ItemCalendar> rowCalendar = new ArrayList<ItemCalendar>();
	FrameLayout linearIncludeCalendar;
	CalendarView view;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.event_calendar, container,
				false);
		linearIncludeCalendar = (FrameLayout) rootView
				.findViewById(R.id.linearIncludeCalendar);
		// set time zone japan
		mCalendar = Calendar.getInstance();
		Date dateCurrent = mCalendar.getTime();
		SimpleDateFormat dayFormat = new SimpleDateFormat("MMMM", Locale.JAPAN);
		String dayCurrent = dayFormat.format(dateCurrent);
		createList();
		getCurrentDate();
		getListMonthInfor();

		
		///test
				linearIncludeCalendar.setOnTouchListener(this);
		// //////////////tesst get dataa
		getDataEvent = new GetDataEventCalendar(getActivity(), 0) {

			@Override
			public void OnTaskCompleted() {
				// TODO Auto-generated method stub
				CalendarView view = new CalendarView(getActivity()
						.getApplicationContext(), listMonthInfor,
						getDataEvent.getItemCalendar());
				// add view
				linearIncludeCalendar.addView(view, new ViewGroup.LayoutParams(
						ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.MATCH_PARENT));
			}
		};
		

		return rootView;
	}

	void getListMonthInfor() {
		for (int i = 0; i < 12; i++) {
			createList();
			fitDayInMonth(current_Month);
			getListDayOfWeek();
			MonthInfo monthInfor = new MonthInfo();
			// fitDayInMonth(current_Month);
			// getListDayOfWeek();
			monthInfor.setmYear(current_Year);
			monthInfor.setmMonth(current_Month);
			monthInfor.setListDay(listDay);
			monthInfor.setListDayOfWeek(listDayOfWeek);
			monthInfor.setHm_DayOfMonth(hm_DayOfMonth);
			listMonthInfor.add(monthInfor);
			current_Month++;
			if (current_Month == 12) {
				current_Month = 0;
				current_Year++;
			}
		}
	}

	void createList() {
		listDay = new ArrayList<String>();// mảng chứa ngày của tháng
		hm_DayOfMonth = new HashMap<String, Integer>();
		listDayOfWeek = new ArrayList<String>();
		list28 = new ArrayList<String>();
		list29 = new ArrayList<String>();
		list30 = new ArrayList<String>();
		list31 = new ArrayList<String>();
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

	void getCurrentDate() {
		current_Year = mCalendar.get(Calendar.YEAR);
		current_Month = mCalendar.get(Calendar.MONTH);
		current_Day = mCalendar.get(Calendar.DAY_OF_MONTH);
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

	/**
	 * Update data
	 * 
	 * @param rowCalendar
	 */
	public void updateArrayValue(List<ItemCalendar> rowCalendar) {
		// TODO Auto-generated method stub
		this.rowCalendar = rowCalendar;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		Log.e("TestTouch", "-----------------------------------");
		switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
        	Log.e("TestTouch", "----------ACTION down-------------------------");
            v.getParent().requestDisallowInterceptTouchEvent(true);
            break;
        case MotionEvent.ACTION_CANCEL:
        case MotionEvent.ACTION_UP:
        	Log.e("TestTouch", "-------------ACTION Up----------------------");
            v.getParent().requestDisallowInterceptTouchEvent(false);
            break;
        default:
            break;
        }
		return false;
	}
}

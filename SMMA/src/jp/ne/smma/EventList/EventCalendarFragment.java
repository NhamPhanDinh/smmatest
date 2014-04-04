package jp.ne.smma.EventList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import jp.ne.smma.R;
import jp.ne.smma.EventCalendar.Controller.ActivitySwipeMotion;
import jp.ne.smma.EventCalendar.Custom.CalendarView;
import jp.ne.smma.EventList.Controller.GetDataEventCalendar;
import jp.ne.smma.Ultis.MonthInfo;
import jp.ne.smma.aboutsmma.DTO.ItemCalendar;
import jp.ne.smma.aboutsmma.dialog.DialogFillterCalendar;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

public class EventCalendarFragment extends Fragment {
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

	ImageView imgFillter;
	private float _xDelta;
	private float _yDelta;
	CalendarView viewLabel;
	CalendarView viewContent;
	Scroller mScroller;
	private GestureDetector gesDetectorContent;
	private GestureDetector gesDetectorLabel;
	private ValueAnimator mScrollAnimator;
	boolean checkRightLeft = false;
	private int month_lenght = 80; // View month height

	DialogFillterCalendar dialogFillter;
	private ArrayList<Integer> idCompanyFillter = new ArrayList<Integer>();
	GetDataEventCalendar getDataEvent;
	List<ItemCalendar> rowCalendar = new ArrayList<ItemCalendar>();
	FrameLayout linearIncludeCalendar;
	CalendarView view;
	private final int SWIPE_MIN_DISTANCE = 20;
	private final int SWIPE_THRESHOLD_VELOCITY = 200;
	TextView monthTv;
	private float posUpX = 0;
	private float posUpY = 0;

	RelativeLayout footer;
	private int WITH_SCREEN;
	private int HEIGHT_SCREEN;
	private int idGetDataCalendar = 1;
	boolean checkHeader = false;
	private Handler handler = new Handler();
	private Runnable runnable;

	LinearLayout linearBanner;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View rootView = inflater.inflate(R.layout.event_calendar, container,
				false);
		imgFillter = (ImageView) rootView.findViewById(R.id.imgFillter);
		footer = (RelativeLayout) rootView
				.findViewById(R.id.event_calendar_footer);
		footer.setVisibility(View.GONE);
		footer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getDataCalendar();
			}
		});

		WindowManager wm = (WindowManager) getActivity()
				.getApplicationContext().getSystemService(
						Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		WITH_SCREEN = display.getWidth(); // deprecated
		HEIGHT_SCREEN = display.getHeight(); // deprecated

		linearIncludeCalendar = (FrameLayout) rootView
				.findViewById(R.id.linearIncludeCalendar);
		monthTv = (TextView) rootView.findViewById(R.id.monthLabel);

		// set time zone japan
		mCalendar = Calendar.getInstance();
		Date dateCurrent = mCalendar.getTime();
		SimpleDateFormat dayFormat = new SimpleDateFormat("MMMM", Locale.JAPAN);
		String dayCurrent = dayFormat.format(dateCurrent);
		createList();
		getCurrentDate();
		getListMonthInfor();

		gesDetectorContent = new GestureDetector(new GestureListener(false));
		gesDetectorLabel = new GestureDetector(new GestureListener(true));

		getDataEvent = new GetDataEventCalendar(getActivity(),
				idGetDataCalendar) {

			@Override
			public void OnTaskCompleted() {
				// TODO Auto-generated method stub
				viewContent = new CalendarView(getActivity()
						.getApplicationContext(), listMonthInfor,
						getDataEvent.getItemCalendar());
				viewLabel = new CalendarView(getActivity()
						.getApplicationContext(), listMonthInfor,
						getDataEvent.getItemCalendar(), true, monthTv);
				// add view
				linearIncludeCalendar.addView(viewContent,
						new ViewGroup.LayoutParams(
								ViewGroup.LayoutParams.MATCH_PARENT,
								ViewGroup.LayoutParams.MATCH_PARENT));
				linearIncludeCalendar.addView(viewLabel,
						new ViewGroup.LayoutParams(
								ViewGroup.LayoutParams.MATCH_PARENT, 300));
				// Set onTouchListener
				viewContent.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						gesDetectorContent.onTouchEvent(event);
						// boolean detectedUp = event.getAction() ==
						// MotionEvent.ACTION_UP;
						// if (!gesDetectorContent.onTouchEvent(event)
						// && detectedUp) {
						// Log.d("UpAction", "UpAction");
						//
						// return true;
						// }
						return false;
					}
				});
				viewLabel.setOnTouchListener(new OnTouchListener() {

					@Override
					public boolean onTouch(View v, MotionEvent event) {
						// TODO Auto-generated method stub
						gesDetectorLabel.onTouchEvent(event);
						return false;
					}
				});

				idGetDataCalendar++;

				rowCalendar = getDataEvent.getItemCalendar();
			}
		};

		mScroller = new Scroller(getActivity().getApplicationContext(), null,
				true);
		mScrollAnimator = ValueAnimator.ofFloat(0, 1);
		mScrollAnimator
				.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					public void onAnimationUpdate(ValueAnimator valueAnimator) {
						// translate();
					}
				});
		imgFillter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialogFillter = new DialogFillterCalendar(getActivity(),
						rowCalendar) {

					@Override
					public void OnTaskCompleted() {
						// TODO Auto-generated method stub
						idCompanyFillter = dialogFillter.GetIDCompanyComplete();
						// filt event , don't draw event 27/3/2014
						dialogFillter.filtCompanyId(idCompanyFillter);
						viewContent.invalidate();
						// log value
						for (int i = 0; i < rowCalendar.size(); i++) {
							Log.i("EventCalendar", "Id company: "
									+ rowCalendar.get(i).isChosen());
						}
					}
				};

			}
		});
		// test swipe down
		linearBanner = (LinearLayout) rootView.findViewById(R.id.linearBanner);
		linearBanner.setOnTouchListener(mActivitySwipeMotion);

		return rootView;
	}

	ActivitySwipeMotion mActivitySwipeMotion = new ActivitySwipeMotion(
			getActivity()) {
		public void onSwipeLeft() {
			Log.i("Calendar", "Swiping Left");
		}

		public void onSwipeRight() {
			Log.i("Calendar", "Swiping Right");
		}

		public void onSwipeDown() {
			Log.i("Calendar", "Swiping Down");
			MainActivity.showHideHeader(true);
			checkHeader = true;
			handler.postDelayed(sendData, 3000);
		}

		public void onSwipeUp() {
			Log.i("Calendar", "Swiping Up");
		}
	};

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		if (handler != null || sendData != null) {
			handler.removeCallbacks(sendData);
		}
	}

	public void getDataCalendar() {
		getDataEvent = new GetDataEventCalendar(getActivity(),
				idGetDataCalendar) {

			@Override
			public void OnTaskCompleted() {
				// TODO Auto-generated method stub
				if (getDataEvent.getItemCalendar().size() != 0) {
					viewContent = new CalendarView(getActivity()
							.getApplicationContext(), listMonthInfor,
							getDataEvent.getItemCalendar());
					viewLabel = new CalendarView(getActivity()
							.getApplicationContext(), listMonthInfor,
							getDataEvent.getItemCalendar(), true, monthTv);
					// add view
					linearIncludeCalendar.removeAllViews();
					linearIncludeCalendar.addView(viewContent,
							new ViewGroup.LayoutParams(
									ViewGroup.LayoutParams.MATCH_PARENT,
									ViewGroup.LayoutParams.MATCH_PARENT));
					linearIncludeCalendar.addView(viewLabel,
							new ViewGroup.LayoutParams(
									ViewGroup.LayoutParams.MATCH_PARENT, 300));
					// Set onTouchListener
					viewContent.setOnTouchListener(new OnTouchListener() {

						@Override
						public boolean onTouch(View v, MotionEvent event) {
							// TODO Auto-generated method stub
							gesDetectorContent.onTouchEvent(event);
							return false;
						}
					});
					viewLabel.setOnTouchListener(new OnTouchListener() {

						@Override
						public boolean onTouch(View v, MotionEvent event) {
							// TODO Auto-generated method stub
							gesDetectorLabel.onTouchEvent(event);
							return false;
						}
					});

					rowCalendar = getDataEvent.getItemCalendar();
					idGetDataCalendar++;
				}

				// viewContent.invalidate();
			}
		};
	}

	public void translate() {
		if (!mScroller.isFinished()) {
			mScroller.computeScrollOffset();

			if (posUpX == 0) {
				posUpX = mScroller.getCurrX();
			}

			float dx = mScroller.getCurrX() - posUpX;
			// float dy = mScroller.getCurrY() - viewContent.getPosY();
			Log.d("Scroll", "Goodddddddddddddd: " + mScroller.getCurrX() + ": "
					+ posUpX + " : " + dx);
			translateX(viewContent, dx);
			translateX(viewLabel, dx);
			posUpX = mScroller.getCurrX();
			// translateY(viewContent, dy);
		} else {
			mScrollAnimator.cancel();
		}
	}

	private class GestureListener extends SimpleOnGestureListener {

		boolean isLabel = false;

		public GestureListener(boolean isLabel) {
			// TODO Auto-generated constructor stub
			this.isLabel = isLabel;
		}

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			final float X = e2.getX();
			final float Y = e2.getY();

			if (Y > _yDelta) {
				// MainActivity.showHideHeader(true);
				// checkHeader = true;
				// handler.postDelayed(sendData,3000);
			}

			float speed = getActivity().getResources().getDisplayMetrics().heightPixels * 0.002f;
			Log.d("Toc Do", "Speed: " + speed);
			// translateX(viewContent, (X - _xDelta));
			// translateX(viewLabel,(X - _xDelta));
			//
			// if (!isLabel) {
			// translateY(viewContent, (Y - _yDelta));
			// }

			translateX(viewContent, (-distanceX));
			translateX(viewLabel, (-distanceX));

			if (!isLabel) {
				translateY(viewContent, (-distanceY));
			}

			if (viewContent.getPosX() > 0)
				viewContent.setPosX(0);
			if (viewContent.getPosX() < -viewContent.getLimitWidth())
				viewContent.setPosX(-viewContent.getLimitWidth());

			if (viewContent.getPosY() > 0)
				viewContent.setPosY(0);

			// Check footer visible
			boolean check = false;
			if (-viewContent.getPosY() >= (viewContent.getLimitHeight() - HEIGHT_SCREEN)) {
				if ((viewContent.getLimitHeight() - HEIGHT_SCREEN) < 0) {
					viewContent.setPosY(0);
				} else {
					viewContent
							.setPosY(-(viewContent.getLimitHeight() - HEIGHT_SCREEN));
				}
				footer.setVisibility(View.VISIBLE);
				check = true;
			} else if (-viewContent.getPosY() < (viewContent.getLimitHeight()
					- HEIGHT_SCREEN - 50)) {
				// TranslateAnimation animate = new TranslateAnimation(0, 0, 0,
				// footer.getHeight());
				// animate.setDuration(500);
				// animate.setFillAfter(true);
				// footer.startAnimation(animate);
				footer.setVisibility(View.GONE);
			}

			if (viewLabel.getPosX() > 0)
				viewLabel.setPosX(0);
			if (viewLabel.getPosX() < -viewContent.getLimitWidth())
				viewLabel.setPosX(-viewContent.getLimitWidth());

			_xDelta = X;
			_yDelta = Y;
			return super.onScroll(e1, e2, distanceX, distanceY);
		}

		@Override
		public boolean onDown(MotionEvent e) {
			_xDelta = e.getX();
			_yDelta = e.getY();

			if (!mScroller.isFinished()) {
				mScroller.forceFinished(true);
			}

			if (handler != null || runnable != null) {
				// handler.removeCallbacks(runnable);
			}

			return super.onDown(e);
		}

		@Override
		public boolean onSingleTapUp(MotionEvent e) {
			// TODO Auto-generated method stub
			_xDelta = e.getX();
			_yDelta = e.getY();

			viewContent.clickEvent(_xDelta - viewContent.getPosX(), _yDelta
					- viewContent.getPosY());
			return super.onSingleTapUp(e);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {

			// mScroller.fling((int) viewContent.getPosX(),
			// 0, 1400, 0, Integer.MIN_VALUE,
			// Integer.MAX_VALUE, 0,0);

			if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

				// Log.d("Gesture", "..................... Right to left");
				//
				// mScroller.fling((int) viewContent.getPosX(),
				// (int) viewContent.getPosY(), 1700, 1700,
				// Integer.MIN_VALUE, Integer.MAX_VALUE,
				// Integer.MIN_VALUE, Integer.MAX_VALUE);
				// mScrollAnimator.setDuration(5 * mScroller.getDuration());
				// mScrollAnimator.start();
				return false; // Right to left
			} else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
				// Log.d("Gesture", "..................... Left to right");
				// mScroller.fling((int) viewContent.getPosX(),
				// (int) viewContent.getPosY(), 1700, 1700,
				// Integer.MIN_VALUE, Integer.MAX_VALUE,
				// Integer.MIN_VALUE, Integer.MAX_VALUE);
				// mScrollAnimator.setDuration(5 * mScroller.getDuration());
				// mScrollAnimator.start();
				return false; // Left to right
			}

			if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
				Log.d("Gesture", "..................... Bottom to top");
				return false; // Bottom to top
			} else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE
					&& Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
				Log.d("Gesture", "..................... Top to bottom");
				return false; // Top to bottom
			}

			return false;
		}
	}

	private final Runnable sendData = new Runnable() {
		public void run() {
			try {
				// prepare and send the data here..

				MainActivity.showHideHeader(false);
				checkHeader = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};

	public void translateX(CalendarView view, float dx) {
		view.translateX(dx);
	}

	public void translateY(CalendarView view, float dy) {
		view.translateY(dy);
	}

	public float getPosX(CalendarView view) {
		return view.getPosX();
	}

	public float getPosY(CalendarView view) {
		return view.getPosY();
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
	public void onDestroy() {
		// TODO Auto-generated method stub
		if (handler != null || runnable != null) {
			// handler.removeCallbacks(runnable);
		}
		super.onDestroy();
	}
}

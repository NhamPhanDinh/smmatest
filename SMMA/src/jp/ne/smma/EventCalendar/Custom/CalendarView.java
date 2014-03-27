package jp.ne.smma.EventCalendar.Custom;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import jp.ne.smma.R;
import jp.ne.smma.EventCalendar.Controller.VersionedGestureDetector;
import jp.ne.smma.Ultis.MonthInfo;
import jp.ne.smma.Ultis.UntilDateTime;
import jp.ne.smma.aboutsmma.DTO.ItemCalendar;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.WindowManager;

public class CalendarView extends View {
	// canvas
	private Drawable mIcon;
	private float mPosX;
	private float mPosY;
	Paint mPaint;
	Paint contentTextPain;
	Paint saturdayPaint;
	Paint sundaypaint;
	Paint backgroundCommonPaint;
	Paint weekendPaint;
	Paint backgroundHeaderPaint;
	private VersionedGestureDetector mDetector;
	private float mScaleFactor = 1.f;
	// calendar
	List<String> listDay;
	List<String> listDayOfWeek;
	HashMap<String, Integer> hm_DayOfWeek;
	Calendar mCalendar;
	Date dateCurrent;
	ArrayList<MonthInfo> listMonthInfo = new ArrayList<MonthInfo>();
	ArrayList<ItemCalendar> listContent = new ArrayList<ItemCalendar>();
	public float max_length_calendar = 28119;
	// screen size and height
	public float WITH_SCREEN;

	public float HEIGHT_SCREEN;

	private boolean isTitle = false;
	
	float square = 0;
	private float month_height = 0;
	
	Paint dayPaint;

	public CalendarView(Context context, ArrayList<MonthInfo> monthInfo,
			ArrayList<ItemCalendar> listContent) {
		this(context, null, 0);
		this.listMonthInfo = monthInfo;
		this.backgroundCommonPaint = new Paint();
		this.backgroundCommonPaint.setColor(Color.parseColor("#FFFAD5"));
		this.backgroundHeaderPaint = new Paint();
		this.backgroundHeaderPaint.setColor(Color.parseColor("#FFFFFF"));
		this.weekendPaint = new Paint();
		this.weekendPaint.setColor(Color.parseColor("#F1F1F1"));
		mPaint = new Paint();
		mPaint.setTextSize(30);
		mPaint.setTextAlign(Align.CENTER);
		mPaint.setColor(Color.BLACK);
		contentTextPain = new Paint();
		contentTextPain.setTextSize(30);
		contentTextPain.setTextAlign(Align.LEFT);
		contentTextPain.setColor(Color.BLACK);
		this.saturdayPaint = new Paint();
		this.saturdayPaint.setColor(Color.parseColor("#C7E5F5"));
		this.sundaypaint = new Paint();
		this.sundaypaint.setColor(Color.parseColor("#F4D2E2"));
		mCalendar = Calendar.getInstance();
		dateCurrent = mCalendar.getTime();
		this.listContent = listContent;
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		this.WITH_SCREEN = display.getWidth(); // deprecated
		this.HEIGHT_SCREEN = display.getHeight(); // deprecated
		square = this.WITH_SCREEN / 14;
		month_height = square * 4 / 3;
		dayPaint = new Paint();
		dayPaint.setColor(Color.TRANSPARENT);
		// this.max_length_calendar = getMaxLengthCalendar();
	}

	public CalendarView(Context context, ArrayList<MonthInfo> monthInfo,
			ArrayList<ItemCalendar> listContent, boolean isTitle) {
		this(context, null, 0);
		this.listMonthInfo = monthInfo;
		this.backgroundCommonPaint = new Paint();
		this.backgroundCommonPaint.setColor(Color.parseColor("#FFFAD5"));
		this.backgroundHeaderPaint = new Paint();
		this.backgroundHeaderPaint.setColor(Color.parseColor("#FFFFFF"));
		this.weekendPaint = new Paint();
		this.weekendPaint.setColor(Color.parseColor("#F1F1F1"));
		mPaint = new Paint();
		mPaint.setTextSize(30);
		mPaint.setTextAlign(Align.CENTER);
		mPaint.setColor(Color.BLACK);
		contentTextPain = new Paint();
		contentTextPain.setTextSize(30);
		contentTextPain.setTextAlign(Align.LEFT);
		contentTextPain.setColor(Color.BLACK);
		this.saturdayPaint = new Paint();
		this.saturdayPaint.setColor(Color.parseColor("#C7E5F5"));
		this.sundaypaint = new Paint();
		this.sundaypaint.setColor(Color.parseColor("#F4D2E2"));
		mCalendar = Calendar.getInstance();
		dateCurrent = mCalendar.getTime();
		this.listContent = listContent;
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		this.WITH_SCREEN = display.getWidth(); // deprecated
		this.HEIGHT_SCREEN = display.getHeight(); // deprecated
		square = this.WITH_SCREEN / 14;
		month_height = square * 4 / 3;
		// this.max_length_calendar = getMaxLengthCalendar();
		this.isTitle = isTitle;
		dayPaint = new Paint();
		dayPaint.setColor(Color.TRANSPARENT);
	}

	public CalendarView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public CalendarView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mIcon = context.getResources().getDrawable(R.drawable.ic_launcher);
		mIcon.setBounds(0, 0, mIcon.getIntrinsicWidth(),
				mIcon.getIntrinsicHeight());
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		ViewParent parent = getParent();
		// or get a reference to the ViewPager and cast it to ViewParent
		parent.requestDisallowInterceptTouchEvent(true);
		return true;
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		canvas.save();
		canvas.translate(mPosX, mPosY);
		canvas.scale(mScaleFactor, mScaleFactor);
		if (isTitle) {
			drawHeaderCalendar(canvas);
		} else {
			drawBackgroundCommon(canvas);
			drawWeekend(canvas);
			drawEvent(canvas);
		}
		canvas.restore();
	}

	public void drawHeaderCalendar(Canvas canvas) {
		// draw title
		int positionX = 0;
		float text_lenght = 10;
		
		canvas.drawRect(0, 0, 32000, 2 * square + this.month_height,this.backgroundHeaderPaint);
		canvas.drawRect(0, 0, 32000, this.month_height,this.weekendPaint);
		
		for (int i = 0; i < listMonthInfo.size(); i++) {
			MonthInfo monthInfo = listMonthInfo.get(i);
			int currentPosition = positionX;
			for (int position = 0; position < monthInfo.getListDay().size(); position++) {
				int keyDay = monthInfo.getHm_DayOfMonth().get(
						monthInfo.getListDayOfWeek().get(position));
				if (keyDay == 7)
					canvas.drawRect(currentPosition + square * position, square
							+ this.month_height, currentPosition + square
							+ square * position,
							2 * square + this.month_height, saturdayPaint);
				else if (keyDay == 1)
					canvas.drawRect(currentPosition + square * position, square
							+ this.month_height, currentPosition + square
							+ square * position,
							2 * square + this.month_height, sundaypaint);
				canvas.drawText(monthInfo.getListDay().get(position) + "",
						currentPosition + square / 2 + square * position,
						square / 2 + text_lenght + this.month_height, mPaint);
				canvas.drawText(
						monthInfo.getListDayOfWeek().get(position) + "",
						currentPosition + square / 2 + square * position, 3
								* square / 2 + text_lenght + this.month_height,
						mPaint);
				if (monthInfo.getListDay().get(position).equals("1"))
					canvas.drawText((monthInfo.getmMonth() + 1) + "月",
							currentPosition + square / 2 + square * position,
							square / 2 + text_lenght, mPaint);
				positionX += square;
			}
		}
	}

	public void drawBackgroundCommon(Canvas canvas) {
		canvas.drawRect(0, 2 * square + this.month_height, 32000, 8000 + +this.month_height, this.backgroundCommonPaint);
		canvas.drawRect(0, 0, 32000, this.month_height, this.backgroundCommonPaint);
	}

	public void drawWeekend(Canvas canvas) {
		int positionX = 0;
		for (int i = 0; i < listMonthInfo.size(); i++) {
			int currentPosition = positionX;
			MonthInfo monthInfo = listMonthInfo.get(i);
			for (int position = 0; position < monthInfo.getListDay().size(); position++) {
				int keyDay = monthInfo.getHm_DayOfMonth().get(
						monthInfo.getListDayOfWeek().get(position));
				if (keyDay == 7 || keyDay == 1)
					canvas.drawRect(currentPosition + square * position, square
							+ this.month_height, currentPosition + square
							+ square * position, 8000 + this.month_height,
							this.weekendPaint);
				positionX += square;
			}
		}
	}

	private float getMaxLengthCalendar() {
		float max_lenght = 0;
		for (int i = 0; i < listMonthInfo.size(); i++) {
			MonthInfo monthInfo = listMonthInfo.get(i);
			for (int j = 0; j < monthInfo.getListDay().size(); j++) {
				max_lenght += 80;
			}
		}
		return max_lenght;
	}

	public void drawEvent(Canvas canvas) {
		int width_icon = 300;
		int height_icon = 80;
		float text_lenght = 10;
		Paint bgPaint = new Paint();
		bgPaint.setColor(Color.WHITE);
		float paddingTop = 60;
		float headerLength = square;
		float contentLength = 200;
		float contentWith = 2200;
		float startTop = paddingTop;
		for (int i = 0; i < listContent.size(); i++) {
			
			dayPaint.setColor(Color.parseColor(listContent.get(i)
					.getColorCode()));
			long beetweenDays = UntilDateTime.betweenDates(UntilDateTime
					.toDateFormat(listContent.get(i).getStartDay()),
					UntilDateTime.toDateFormat(listContent.get(i).getEndDay())) + 1;
			long longBegin = UntilDateTime.betweenDates(this.dateCurrent,
					UntilDateTime
							.toDateFormat(listContent.get(i).getStartDay())) - 1;
			// draw header for event
			canvas.drawRect((longBegin + dateCurrent.getDate()) * square, 2
					* square + startTop + this.month_height,
					(longBegin + dateCurrent.getDate()) * square + square
							* beetweenDays, 2 * square + startTop
							+ headerLength + this.month_height, dayPaint);
			// draw content for event
			canvas.drawRect((longBegin + dateCurrent.getDate()) * square, 2
					* square + startTop + headerLength + this.month_height,
					(longBegin + dateCurrent.getDate()) * square + contentWith,
					2 * square + startTop + headerLength + square
							+ this.month_height, bgPaint);
			Bitmap bmp = BitmapFactory.decodeResource(getResources(),
					listContent.get(i).getIconUrl());// listContent.get(i).getIconUrl()
			Bitmap image = Bitmap.createScaledBitmap(bmp, 7 * (int) square,
					(int) square, true);
			canvas.drawBitmap(image, (longBegin + dateCurrent.getDate())
					* square, 2 * square + startTop + headerLength
					+ this.month_height, null);
			// draw event name
			canvas.drawText(listContent.get(i).getEventName() + "",
					(longBegin + dateCurrent.getDate()) * square + 7
							* (int) square, 2 * square + startTop
							+ headerLength + text_lenght + square / 2
							+ this.month_height, contentTextPain);
			// draw company name
			canvas.drawText(listContent.get(i).getCompanyName() + "",
					(longBegin + dateCurrent.getDate()) * square + 7
							* (int) square
							+ listContent.get(i).getEventName().length()
							* text_lenght * 3, 2 * square + startTop
							+ headerLength + text_lenght + square / 2
							+ this.month_height, contentTextPain);
			paddingTop = 260;
			startTop += paddingTop;
		}
	}

	public void translateX(float dx) {
		mPosX += dx;
		invalidate();
	}

	public void translateY(float dy) {
		mPosY += dy;
		invalidate();
	}

	public float getPosX() {
		return this.mPosX;
	}

	public void setPosX(float mPosX) {
		this.mPosX = mPosX;
	}

	public float getPosY() {
		return this.mPosY;
	}

	public void setPosY(float mPosY) {
		this.mPosY = mPosY;
	}

}

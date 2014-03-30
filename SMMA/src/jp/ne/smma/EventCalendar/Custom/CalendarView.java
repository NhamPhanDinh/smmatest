package jp.ne.smma.EventCalendar.Custom;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import jp.ne.smma.R;
import jp.ne.smma.EventCalendar.Controller.VersionedGestureDetector;
import jp.ne.smma.EventList.ProductActivity;
import jp.ne.smma.Ultis.MonthInfo;
import jp.ne.smma.Ultis.UntilDateTime;
import jp.ne.smma.aboutsmma.DTO.ItemCalendar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
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
	int currentDay;
	ArrayList<MonthInfo> listMonthInfo = new ArrayList<MonthInfo>();
	ArrayList<ItemCalendar> listContent = new ArrayList<ItemCalendar>();
	// This list save the coordinates
	RectF[] arrayRect;

	public float max_length_calendar = 28119;
	// screen size and height
	public float WITH_SCREEN;

	public float HEIGHT_SCREEN;

	private boolean isTitle = false;
	private boolean isFooter = false;

	float square = 0;
	private float month_height = 0;
	private float limitWidth = 32000;
	private float limitHeight = 8000;

	Paint dayPaint;

	Context context;

	public CalendarView(Context context, ArrayList<MonthInfo> monthInfo,
			ArrayList<ItemCalendar> listContent) {
		this(context, null, 0);
		this.context = context;
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		this.WITH_SCREEN = display.getWidth(); // deprecated
		this.HEIGHT_SCREEN = display.getHeight(); // deprecated
		this.listMonthInfo = monthInfo;
		this.backgroundCommonPaint = new Paint();
		this.backgroundCommonPaint.setColor(Color.parseColor("#FFFAD5"));
		this.backgroundHeaderPaint = new Paint();
		this.backgroundHeaderPaint.setColor(Color.parseColor("#FFFFFF"));
		this.weekendPaint = new Paint();
		this.weekendPaint.setColor(Color.parseColor("#F1F1F1"));
		mPaint = new Paint();
		mPaint.setTextSize(30 * this.WITH_SCREEN / 1080);
		mPaint.setTextAlign(Align.CENTER);
		mPaint.setColor(Color.BLACK);
		contentTextPain = new Paint();
		contentTextPain.setTextSize(30 * this.WITH_SCREEN / 1080);
		contentTextPain.setTextAlign(Align.LEFT);
		contentTextPain.setColor(Color.BLACK);
		this.saturdayPaint = new Paint();
		this.saturdayPaint.setColor(Color.parseColor("#C7E5F5"));
		this.sundaypaint = new Paint();
		this.sundaypaint.setColor(Color.parseColor("#F4D2E2"));
		mCalendar = Calendar.getInstance();
		dateCurrent = mCalendar.getTime();
		this.listContent = listContent;
		square = this.WITH_SCREEN / 14;
		month_height = square;
		dayPaint = new Paint();
		dayPaint.setColor(Color.TRANSPARENT);
		// this.max_length_calendar = getMaxLengthCalendar();
	}

	public CalendarView(Context context, ArrayList<MonthInfo> monthInfo,
			ArrayList<ItemCalendar> listContent, boolean isTitle) {
		this(context, null, 0);
		this.context = context;
		WindowManager wm = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		this.WITH_SCREEN = display.getWidth(); // deprecated
		this.HEIGHT_SCREEN = display.getHeight(); // deprecated
		this.listMonthInfo = monthInfo;
		this.backgroundCommonPaint = new Paint();
		this.backgroundCommonPaint.setColor(Color.parseColor("#FFFAD5"));
		this.backgroundHeaderPaint = new Paint();
		this.backgroundHeaderPaint.setColor(Color.parseColor("#FFFFFF"));
		this.weekendPaint = new Paint();
		this.weekendPaint.setColor(Color.parseColor("#F1F1F1"));
		mPaint = new Paint();
		mPaint.setTextSize(30 * this.WITH_SCREEN / 1080);
		mPaint.setTextAlign(Align.CENTER);
		mPaint.setColor(Color.BLACK);
		contentTextPain = new Paint();
		contentTextPain.setTextSize(30 * this.WITH_SCREEN / 1080);
		contentTextPain.setTextAlign(Align.LEFT);
		contentTextPain.setColor(Color.BLACK);
		this.saturdayPaint = new Paint();
		this.saturdayPaint.setColor(Color.parseColor("#C7E5F5"));
		this.sundaypaint = new Paint();
		this.sundaypaint.setColor(Color.parseColor("#F4D2E2"));
		mCalendar = Calendar.getInstance();
		dateCurrent = mCalendar.getTime();
		this.listContent = listContent;
		square = this.WITH_SCREEN / 14;
		month_height = square;
		// this.max_length_calendar = getMaxLengthCalendar();
		this.isTitle = isTitle;
		dayPaint = new Paint();
		dayPaint.setColor(Color.TRANSPARENT);
		// clear first month
		MonthInfo monthIn = listMonthInfo.get(0);
		currentDay = this.dateCurrent.getDate();
		for (int j = 0; j < currentDay - 1; j++) {
			monthIn.getListDay().remove(0);
			monthIn.getListDayOfWeek().remove(0);
		}
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
		float text_lenght = 10 * this.WITH_SCREEN / 1080;

		canvas.drawRect(0, 0, limitWidth, 2 * square + this.month_height,
				this.backgroundHeaderPaint);
		canvas.drawRect(0, 0, limitWidth, this.month_height, this.weekendPaint);

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
		canvas.drawRect(0, 2 * square + this.month_height, 32000 * 2,
				limitHeight * 2, this.backgroundCommonPaint);
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
							+ square * position, limitHeight * 2,
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
		float text_lenght = 10 * this.WITH_SCREEN / 1080;
		Paint bgPaint = new Paint();
		bgPaint.setColor(Color.WHITE);
		float paddingTop = 0;
		float headerLength = square;
		float contentLength = 200 * this.WITH_SCREEN / 1080;
		float widthContentEvent = 2200 * this.WITH_SCREEN / 1080;
		float startTop = paddingTop;
		// Padding left of event
		float paddingLeftEvent;

		float tempLimitHeight = 0;
		float tempLimitWidth = 0;

		Bitmap footer = BitmapFactory.decodeResource(getResources(),
				R.drawable.footer);// listContent.get(i).getIconUrl()
		float ratio_footer = footer.getWidth() / footer.getHeight();
		Bitmap footer_image = Bitmap.createScaledBitmap(footer,
				(int) ratio_footer * (int) square, (int) square, true);

		arrayRect = new RectF[listContent.size()];

		for (int i = 0; i < listContent.size(); i++) {

			String eventName = listContent.get(i).getEventName() + "";
			String companyName = listContent.get(i).getCompanyName() + "";

			dayPaint.setColor(Color.parseColor(listContent.get(i)
					.getColorCode()));
			long beetweenDays = UntilDateTime.betweenDates(UntilDateTime
					.toDateFormat(listContent.get(i).getStartDay()),
					UntilDateTime.toDateFormat(listContent.get(i).getEndDay())) + 1;
			long longBegin = UntilDateTime.betweenDates(this.dateCurrent,
					UntilDateTime
							.toDateFormat(listContent.get(i).getStartDay())) + 1;
			long longEnd = UntilDateTime.betweenDates(this.dateCurrent,
					UntilDateTime.toDateFormat(listContent.get(i).getEndDay())) + 1;

			if (longBegin < 0 && longEnd > 0) {
				longBegin = 0;
			}

			paddingLeftEvent = longBegin * square;

			if (!listContent.get(i).isChosen() && longBegin <= 90
					&& longBegin >= 0) {

				Bitmap bmp = BitmapFactory.decodeResource(getResources(),
						listContent.get(i).getIconUrl());// listContent.get(i).getIconUrl()
				// float ratio = bmp.getWidth() / bmp.getHeight();
				// Bitmap image = Bitmap.createScaledBitmap(bmp, (int) ratio
				// * (int) square, (int) square, true);
				float scaleTofit = getScaleRatio(bmp.getWidth())
						* this.WITH_SCREEN / 1080;
				float ratio = bmp.getWidth() / bmp.getHeight();
				int width = bmp.getWidth();
				Log.d("size", width + "");
				int height = bmp.getHeight();
				float scaleWidth = (float) ((square * ratio * scaleTofit) / width);
				float scaleHeight = (square) / height;
				Matrix matrix = new Matrix();
				matrix.postScale(scaleWidth, scaleHeight);
				// Bitmap image = Bitmap.createScaledBitmap(bmp, (int) ratio
				// * (int) square, (int) square, true);
				Bitmap image = Bitmap.createBitmap(bmp, 0, 0, width, height,
						matrix, false);
				float widthText = contentTextPain.measureText(eventName
						+ companyName);
				widthContentEvent = widthText + bmp.getWidth() + 2 * square;

				// draw header for event
				canvas.drawRect(paddingLeftEvent, 3 * square + startTop
						+ square / 2, paddingLeftEvent + square * beetweenDays,
						3 * square + startTop + headerLength, dayPaint);
				// draw content for event
				canvas.drawRect(paddingLeftEvent, 3 * square + startTop
						+ headerLength, paddingLeftEvent + widthContentEvent, 3
						* square + startTop + headerLength + square, bgPaint);
				// draw logo event
				canvas.drawBitmap(image, paddingLeftEvent, 3 * square
						+ startTop + headerLength, null);
				// draw event name
				canvas.drawText(eventName, paddingLeftEvent
						+ (ratio * scaleTofit + 1) * (int) square, 3 * square
						+ startTop + headerLength + text_lenght + square / 2,
						contentTextPain);
				// draw company name
				canvas.drawText(companyName,
						paddingLeftEvent + (ratio + 2) * (int) square
								+ contentTextPain.measureText(eventName), 3
								* square + startTop + headerLength
								+ text_lenght + square / 2, contentTextPain);

				tempLimitHeight = 4 * square + startTop;
				if (tempLimitWidth > (paddingLeftEvent + square
						* (beetweenDays + 1))
						&& tempLimitWidth != 32000) {
					tempLimitWidth = paddingLeftEvent + square * (beetweenDays)
							- 1000;
				}

				// save Rect Coordinates of event
				arrayRect[i] = new RectF(paddingLeftEvent, 3 * square
						+ startTop + headerLength, paddingLeftEvent
						+ widthContentEvent, 3 * square + startTop
						+ headerLength + square);

				paddingTop = square;
				startTop += (2 * paddingTop);
			}

		}
		// limitHeight = tempLimitHeight;
		// limitWidth = -tempLimitWidth;
		Log.i("Limit", "Width: " + limitWidth + " :Height " + limitHeight);
	}

	private float getScaleRatio(int width) {

		float result = 0;
		if (width == 900 || width == 882)
			result = 1;
		else if (width == 444 || width == 438)
			result = (float) 1.23;
		else if (width == 393)
			result = (float) 1.09;
		else if (width == 588)
			result = (float) 1.195;
		else if (width == 588)
			result = (float) 2.21;
		return result;
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

	public float getLimitWidth() {
		return this.limitWidth;
	}

	public float getLimitHeight() {
		return this.limitHeight;
	}

	public void clickEvent(float x, float y) {
		for (int i = 0; i < arrayRect.length; i++) {
			if (arrayRect[i] != null) {
				if (x > arrayRect[i].left && x < arrayRect[i].right
						&& y > arrayRect[i].top && y < arrayRect[i].bottom) {
					Log.d("ClickEvent", "ClickEvent.............." + i);

					Intent intent = new Intent(context, ProductActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.putExtra("itemId", listContent.get(i).getEventId());
					intent.putExtra("name", listContent.get(i).getCompanyName());

					Log.d("GetIntent", "Id: " + listContent.get(i).getEventId()
							+ " ;Name: " + listContent.get(i).getCompanyName());

					context.startActivity(intent);
				}
			}

		}
	}

}

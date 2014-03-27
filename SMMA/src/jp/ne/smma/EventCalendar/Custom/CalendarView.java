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
import android.view.MotionEvent;
import android.view.View;

public class CalendarView extends View {
	// canvas
		private Drawable mIcon;
		private float mPosX;
		private float mPosY;
		Paint mPaint;
		Paint saturdayPaint;
		Paint sundaypaint;
		Paint backgroundCommonPaint;
		Paint weekendPaint;
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
		// Padding
		private int month_lenght = 80;

		public float max_length_calendar = 28119;
		public CalendarView(Context context, ArrayList<MonthInfo> monthInfo,ArrayList<ItemCalendar> listContent) {
			this(context, null, 0);
			this.listMonthInfo = monthInfo;
			this.backgroundCommonPaint = new Paint();
			this.backgroundCommonPaint.setColor(Color.parseColor("#FFFAD5"));
			this.weekendPaint = new Paint();
			this.weekendPaint.setColor(Color.parseColor("#F1F1F1"));
			mPaint = new Paint();
			mPaint.setTextSize(30);
			mPaint.setTextAlign(Align.CENTER);
			mPaint.setColor(Color.BLACK);
			this.saturdayPaint = new Paint();
			this.saturdayPaint.setColor(Color.parseColor("#C7E5F5"));
			this.sundaypaint = new Paint();
			this.sundaypaint.setColor(Color.parseColor("#F4D2E2"));
			mCalendar = Calendar.getInstance();
			dateCurrent = mCalendar.getTime();
			this.listContent=listContent;
			// this.max_length_calendar = getMaxLengthCalendar();
		}

		public CalendarView(Context context, AttributeSet attrs) {
			this(context, attrs, 0);
		}

		public CalendarView(Context context, AttributeSet attrs, int defStyle) {
			super(context, attrs, defStyle);
			mIcon = context.getResources().getDrawable(R.drawable.ic_launcher);
			mIcon.setBounds(0, 0, mIcon.getIntrinsicWidth(),
					mIcon.getIntrinsicHeight());
			mDetector = VersionedGestureDetector.newInstance(context,
					new GestureCallback());
		}

		@Override
		public boolean onTouchEvent(MotionEvent ev) {
			mDetector.onTouchEvent(ev);
			return true;
		}

		@Override
		public void onDraw(Canvas canvas) {
			super.onDraw(canvas);
			canvas.save();
			canvas.translate(mPosX, mPosY);
			canvas.scale(mScaleFactor, mScaleFactor);
			drawBackgroundCommon(canvas);
			drawWeekend(canvas);
			drawHeaderCalendar(canvas);
			drawEvent(canvas);
			canvas.restore();
		}

		public void drawHeaderCalendar(Canvas canvas) {
			// draw title
			int positionX = 0;
			for (int i = 0; i < listMonthInfo.size(); i++) {
				MonthInfo monthInfo = listMonthInfo.get(i);
				int currentPosition = positionX;
				for (int position = 0; position < monthInfo.getListDay().size(); position++) {
					int keyDay = monthInfo.getHm_DayOfMonth().get(
							monthInfo.getListDayOfWeek().get(position));
					if (keyDay == 7)
						canvas.drawRect(currentPosition + 80 * position,
								80 + this.month_lenght, currentPosition + 80 + 80
										* position, 160 + this.month_lenght,
								saturdayPaint);
					else if (keyDay == 1)
						canvas.drawRect(currentPosition + 80 * position,
								80 + this.month_lenght, currentPosition + 80 + 80
										* position, 160 + +this.month_lenght,
								sundaypaint);
					canvas.drawText(monthInfo.getListDay().get(position) + "",
							currentPosition + 40 + 80 * position,
							50 + +this.month_lenght, mPaint);
					canvas.drawText(
							monthInfo.getListDayOfWeek().get(position) + "",
							currentPosition + 40 + 80 * position,
							125 + this.month_lenght, mPaint);
					if (monthInfo.getListDay().get(position).equals("1"))
						canvas.drawText((monthInfo.getmMonth() + 1) + "月",
								currentPosition + 40 + 80 * position, 45, mPaint);
					positionX += 80;
				}
			}
		}

		public void drawBackgroundCommon(Canvas canvas) {
			canvas.drawRect(0, 160 + this.month_lenght, 32000,
					8000 + +this.month_lenght, this.backgroundCommonPaint);
			canvas.drawRect(0, 0, 32000, this.month_lenght,
					this.backgroundCommonPaint);
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
						canvas.drawRect(currentPosition + 80 * position,
								80 + +this.month_lenght, currentPosition + 80 + 80
										* position, 8000 + +this.month_lenght,
								this.weekendPaint);
					positionX += 80;
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
			Paint bgPaint = new Paint();
			bgPaint.setColor(Color.WHITE);
			int paddingTop = 60;
			int headerLength = 80;
			int contentLength = 200;
			int contentWith = 1000;
			int startTop = paddingTop;
			for (int i = 0; i < listContent.size(); i++) {
				Paint dayPaint = new Paint();
				dayPaint.setColor(Color.parseColor(listContent.get(i)
						.getColorCode()));
				long beetweenDays = UntilDateTime.betweenDates(
						UntilDateTime.toDateFormat(listContent.get(i).getStartDay()),
						UntilDateTime.toDateFormat(listContent.get(i).getEndDay()));
				long longBegin = UntilDateTime.betweenDates(this.dateCurrent,
						UntilDateTime.toDateFormat(listContent.get(i).getStartDay()));
				// draw header for event
				canvas.drawRect((longBegin + dateCurrent.getDate()) * 80, 160
						+ startTop + this.month_lenght,
						(longBegin + dateCurrent.getDate()) * 80 + 80
								* beetweenDays, 160 + startTop + headerLength
								+ this.month_lenght, dayPaint);
				// draw content for event
				canvas.drawRect((longBegin + dateCurrent.getDate()) * 80, 160
						+ startTop + headerLength + this.month_lenght,
						(longBegin + dateCurrent.getDate()) * 80 + 80
								* beetweenDays + contentWith, 160 + startTop
								+ contentLength + this.month_lenght, bgPaint);
				Bitmap bmp = BitmapFactory.decodeResource(getResources(),
						listContent.get(i).getIconUrl());//listContent.get(i).getIconUrl()
				canvas.drawBitmap(bmp, (longBegin + dateCurrent.getDate()) * 80,
						160 + startTop + headerLength + this.month_lenght, null);
				// draw event name
				canvas.drawText(listContent.get(i).getEventName() + "",
						(longBegin + dateCurrent.getDate()) * 80 + 820, 160
								+ startTop + headerLength + 65 + this.month_lenght,
						mPaint);
				// draw company name
				canvas.drawText(listContent.get(i).getCompanyName() + "",
						(longBegin + dateCurrent.getDate()) * 80 + 1200, 160
								+ startTop + headerLength + 65 + this.month_lenght,
						mPaint);
				paddingTop = 260;
				startTop += paddingTop;
			}
		}

		private class GestureCallback implements
				VersionedGestureDetector.OnGestureListener {
			
			public void onDrag(float dx, float dy) {
				mPosX += dx;
				if (mPosX > 0)
					mPosX = 0;
				if (mPosX < -max_length_calendar)
					mPosX = -max_length_calendar;
				// mPosY += dy;
				invalidate();
			}

			public void onScale(float scaleFactor) {
				// mScaleFactor *= scaleFactor;
				// // Don't let the object get too small or too large.
				// mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
				invalidate();
			}
		}
}

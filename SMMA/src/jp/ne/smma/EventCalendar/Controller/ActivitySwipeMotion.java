package jp.ne.smma.EventCalendar.Controller;

import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.MotionEvent.PointerCoords;
import android.view.View;

public class ActivitySwipeMotion implements ActivitySwipeInterface,
		View.OnTouchListener {
	static final int MIN_DISTANCE = 100;
	private PointerCoords mDownPos = new PointerCoords();
	private PointerCoords mUpPos = new PointerCoords();
	private PointerCoords mMovePos = new PointerCoords();

	public ActivitySwipeMotion(Activity activity) {
	}

	public void onSwipeLeft() {
	}

	public void onSwipeRight() {
	}

	public void onSwipeDown() {
	}

	public void onSwipeUp() {
	}

	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		// Capture the position where swipe begins
		case MotionEvent.ACTION_DOWN: {
			event.getPointerCoords(0, mDownPos);
			Log.i("SwipeMotion", "SwipeMotion Down");
			return true;
		}
		case MotionEvent.ACTION_MOVE: {
			event.getPointerCoords(0, mMovePos);
			Log.i("SwipeMotion", "SwipeMotion Move");
			float dy = mDownPos.y - mMovePos.y;

			// Check for vertical wipe
			if (dy > 0)
				onSwipeUp();
			else
				onSwipeDown();
			return true;
		}
		// Get the position where swipe ends
		case MotionEvent.ACTION_UP: {
			event.getPointerCoords(0, mUpPos);

			float dx = mDownPos.x - mUpPos.x;

			// Check for horizontal wipe
			if (Math.abs(dx) > MIN_DISTANCE) {
				if (dx > 0)
					onSwipeLeft();
				else
					onSwipeRight();
				return true;
			}

			float dy = mDownPos.y - mUpPos.y;

			// Check for vertical wipe
			if (Math.abs(dy) > MIN_DISTANCE) {
				if (dy > 0)
					onSwipeUp();
				else
					onSwipeDown();
				return true;
			}
		}
		}
		return false;
	}
}

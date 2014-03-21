package jp.ne.smma.EventList;

import java.util.Calendar;

import jp.ne.smma.R;
import jp.ne.smma.EventCalendar.Custom.Switch;
import jp.ne.smma.Ultis.Constance;
import jp.ne.smma.Ultis.DialogUtil;
import jp.ne.smma.notification.ReceiverNotification;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

/**
 * Setting activity
 */
public class SettingActivity extends Activity implements OnClickListener {
	private Button btnSetEvent;
	private Button btnClearEvent;
	private Switch btnOnOff;
	private RadioButton checkOneWeek;
	private RadioButton checkThreeDay;
	private RadioButton checkOneDay;
	private RadioButton btnActive;
	private RadioButton btnDeactive;

	private RadioGroup radioOrientation;
	private RadioGroup radioCheckBox;
	private LinearLayout linearShow;

	private int clickRadio = 0;
	// set value send notification
	private PendingIntent pendingIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_activity);
		// get data from xml
		ini();
		// set click button
		btnSetEvent.setOnClickListener(this);
		btnClearEvent.setOnClickListener(this);
		// click switch
		// check event
		if (!Constance.bCheckOnOff) {
			linearShow.setVisibility(View.GONE);
			btnOnOff.setChecked(false);
		} else {
			btnOnOff.setChecked(true);
			linearShow.setVisibility(View.VISIBLE);
		}
		btnOnOff.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				if (isChecked) {
					linearShow.setVisibility(View.VISIBLE);
					Constance.bCheckOnOff = true;
				}

				else {
					linearShow.setVisibility(View.GONE);
					Constance.bCheckOnOff = false;
				}
			}
		});
		// check event bOrientation
		if (Constance.bOrientation) {
			btnActive.setChecked(true);
		} else {
			btnDeactive.setChecked(true);
		}
		// detect click radiobutton

		radioOrientation
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
					public void onCheckedChanged(RadioGroup rGroup,
							int checkedId) {
						clickRadio = rGroup.getCheckedRadioButtonId();
						// If the radiobutton that has changed in check state is
						// now checked...
						switch (clickRadio) {
						case R.id.btnActive:
							setAutoOrientationEnabled(getContentResolver(),
									true);
							Constance.bOrientation = true;
							break;

						case R.id.btnDeactive:
							setAutoOrientationEnabled(getContentResolver(),
									false);
							Constance.bOrientation = false;
							break;
						}
					}
				});
		// detect check box
	}

	/**
	 * Set auto orentation enabled
	 * 
	 * @param resolver
	 * @param enabled
	 */
	public static void setAutoOrientationEnabled(ContentResolver resolver,
			boolean enabled) {
		Settings.System.putInt(resolver,
				Settings.System.ACCELEROMETER_ROTATION, enabled ? 1 : 0);
	}

	/**
	 * Ini object from xml
	 */
	private void ini() {
		btnSetEvent = (Button) findViewById(R.id.btnSetEvent);
		btnClearEvent = (Button) findViewById(R.id.btnClearEvent);
		btnOnOff = (Switch) findViewById(R.id.btn_on_off);
		checkOneWeek = (RadioButton) findViewById(R.id.checkOneWeek);
		checkThreeDay = (RadioButton) findViewById(R.id.checkThreeDay);
		checkOneDay = (RadioButton) findViewById(R.id.checkOneDay);
		btnActive = (RadioButton) findViewById(R.id.btnActive);
		btnDeactive = (RadioButton) findViewById(R.id.btnDeactive);
		radioOrientation = (RadioGroup) findViewById(R.id.radioOrientation);
		linearShow = (LinearLayout) findViewById(R.id.linearShow);
		radioCheckBox = (RadioGroup) findViewById(R.id.radioCheckBox);
	}

	/**
	 * Show dialog clear event
	 */
	protected void showDialogClearEvent() {
		DialogUtil.doMessageDialog(SettingActivity.this, null,
				getString(R.string.content_message),
				getString(R.string.dialog_ok),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				}, getString(R.string.dialog_cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
	}

	/**
	 * Set notification
	 * 
	 * @param datetime
	 */
	protected void setNotification() {
		Calendar calendar = Calendar.getInstance();
		
		calendar.set(2014, 2, 20, 16, 37, 00); //month 3// month +1
		Log.i("", "Set calandar: "+calendar.getTime());
		// go to receiver class
		Intent myIntent = new Intent(SettingActivity.this,
				ReceiverNotification.class);
		pendingIntent = PendingIntent.getBroadcast(SettingActivity.this, 0,
				myIntent, 0);
		// set alarm
		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.set(AlarmManager.RTC, calendar.getTimeInMillis(),
				pendingIntent);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnClearEvent) {
			showDialogClearEvent();
		}
		if (v==btnSetEvent) {
			//setNotification();
			Intent intent = new Intent(getApplicationContext(),
					ListEventNotificationActivity.class);
			startActivity(intent);
		}
		
	}

}

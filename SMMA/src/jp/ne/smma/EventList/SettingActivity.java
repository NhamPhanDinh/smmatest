package jp.ne.smma.EventList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import jp.ne.smma.R;
import jp.ne.smma.EventCalendar.Custom.Switch;
import jp.ne.smma.Ultis.ApplicationUntils;
import jp.ne.smma.Ultis.Constance;
import jp.ne.smma.Ultis.DialogUtil;
import jp.ne.smma.aboutsmma.DAO.NotificationDataSource;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
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
	private RadioButton btnPortraint;
	private RadioButton btnLanscape;
	private RadioButton btnAutoRotate;

	private RadioGroup radioOrientation;
	private RadioGroup radioCheckBox;
	private LinearLayout linearShow;

	private int clickRadio = 0;
	// set value send notification

	NotificationDataSource notificationSource;

	ProgressDialog pDialog;

	// preferences
	SharedPreferences preferences;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_activity);
		if (Constance.checkPortrait==1) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		} else if (Constance.checkPortrait==2){
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}else{
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
		}
//		// get data from xml
		ini();
		pDialog = new ProgressDialog(SettingActivity.this);
		// set click button
		btnSetEvent.setOnClickListener(this);
		btnClearEvent.setOnClickListener(this);
		// share Preference
		preferences = PreferenceManager
				.getDefaultSharedPreferences(SettingActivity.this);
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
					// update all status
					notificationSource = new NotificationDataSource(
							SettingActivity.this);
					notificationSource.open();
					notificationSource.updateAllStatus("0");
					notificationSource.close();
				}

				else {
					linearShow.setVisibility(View.GONE);
					Constance.bCheckOnOff = false;
				}
			}
		});
		// check event bOrientation
		if (Constance.checkPortrait==1) {
			btnPortraint.setChecked(true);
		} else if(Constance.checkPortrait==2){			
			btnLanscape.setChecked(true);
		}else{
			btnAutoRotate.setChecked(true);
		}
		// detect click radiobutton

	
		// detect check box
		// check checkbox
		checkBoxShowing();
		radioCheckBox
				.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub
						int click = group.getCheckedRadioButtonId();
						// If the radiobutton that has changed in check state is
						// now checked...
						switch (click) {
						case R.id.checkOneDay:

							// set variable check
							Constance.strCheckBoxNotifiation = 1;
							checkOneDay.setChecked(true);
							// Update value
							new updateTimeShowNotification().execute("");
							break;

						case R.id.checkThreeDay:
							// set variable check
							Constance.strCheckBoxNotifiation = 2;
							checkThreeDay.setChecked(true);
							// Update value
							new updateTimeShowNotification().execute("");
							break;
						case R.id.checkOneWeek:
							// set variable check
							Constance.strCheckBoxNotifiation = 3;
							checkOneWeek.setChecked(true);
							// Update value
							new updateTimeShowNotification().execute("");
							break;
						}
					}
				});
		////////////////////////////////////////
		
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
		btnPortraint = (RadioButton) findViewById(R.id.btnActive);
		btnLanscape = (RadioButton) findViewById(R.id.btnDeactive);
		btnAutoRotate=(RadioButton)findViewById(R.id.btnAutoRotate);
		radioOrientation = (RadioGroup) findViewById(R.id.radioOrientation);
		linearShow = (LinearLayout) findViewById(R.id.linearShow);
		radioCheckBox = (RadioGroup) findViewById(R.id.radioCheckBox);
	}

	/**
	 * Show dialog clear event
	 */
	protected void showDialogClearEvent() {
		DialogUtil.doMessageDialog(
				SettingActivity.this,
				null,
				getString(R.string.content_message),
				getString(R.string.dialog_ok),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						// clear data database
						notificationSource = new NotificationDataSource(
								SettingActivity.this);
						notificationSource.open();
						// update count
						Constance.countDatabaseNotification = Constance.countDatabaseNotification
								+ notificationSource.getNotificationCount();
						// delete data db
						notificationSource.deleteAllNotication();
						notificationSource.close();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnClearEvent) {
			if (showDialogValidate()) {
				showDialogClearEvent();
			}

		}
		if (v == btnSetEvent) {
			// setNotification();
			if (showDialogValidate()) {
				// if setting ON OFF check
				if (Constance.bCheckOnOff) {
					Intent intent = new Intent(getApplicationContext(),
							ListEventNotificationActivity.class);
					startActivity(intent);
				} else {
					DialogUtil.doMessageDialog(SettingActivity.this, null,
							"You need select ON notification settings.", "OK",
							null);
				}
			}

		}

	}

	/**
	 * Validate record notification
	 * 
	 * @return true if have > 1 record
	 */
	protected Boolean validateRecordNotification() {
		int count = 0;
		try {
			notificationSource = new NotificationDataSource(
					SettingActivity.this);
			notificationSource.open();
			count = notificationSource.getNotificationCount();
			notificationSource.close();
			// check record
			if (count > 0) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Show dialog validate
	 * 
	 * @return
	 */
	private boolean showDialogValidate() {
		if (!validateRecordNotification()) {
			DialogUtil.doMessageDialog(SettingActivity.this, null,
					"No have data event list", "OK", null);
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Checkbox set notification showing
	 */
	private void checkBoxShowing() {
		if (Constance.strCheckBoxNotifiation == 1) {
			// 1 day
			checkOneDay.setChecked(true);
		} else if (Constance.strCheckBoxNotifiation == 2) {
			// 3 day
			checkThreeDay.setChecked(true);
		} else if (Constance.strCheckBoxNotifiation == 3) {
			// 1 week
			checkOneWeek.setChecked(true);
		} else {
			// nothing
			checkOneDay.setChecked(false);
			checkThreeDay.setChecked(false);
			checkOneWeek.setChecked(false);
		}
	}

	/**
	 * Assytask update checkbox
	 */
	private class updateTimeShowNotification extends
			AsyncTask<String, String, String> {
		@SuppressWarnings("unused")
		ArrayList<String> strCheckZero;
		ArrayList<String> strChooseDayZwro;
		ArrayList<String> strChooseDayUpdate = new ArrayList<String>();
		String fDate = null;

		@Override
		protected String doInBackground(String... params) {
			// get data from DB
			strCheckZero = notificationSource.getCheckEqualZero();
			strChooseDayZwro = notificationSource.getChooseDayZero();
			// convert day by 1day, 3day, 1 weeks
			for (int i = 0; i < strChooseDayZwro.size(); i++) {
				fDate = new SimpleDateFormat("yyyy/MM/dd")
						.format(ApplicationUntils
								.converStringtoDate(strChooseDayZwro.get(i)));
				// check data time
				fDate = ApplicationUntils.previewDaybyDay(fDate);
				strChooseDayUpdate.add(fDate);
				// set notification
				ApplicationUntils.setNotification(SettingActivity.this, fDate);
			}
			// update database
			notificationSource.updateChooseDayZero(strChooseDayUpdate);

			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			if (pDialog != null) {
				pDialog.dismiss();
			}
			notificationSource.close();

		}

		@Override
		protected void onPreExecute() {

			pDialog.setMessage("Updating data...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(false);
			pDialog.show();
			// open db
			notificationSource = new NotificationDataSource(
					SettingActivity.this);
			notificationSource.open();
		}

	}
	
	@Override
	protected void onStart(){
		super.onStart();
//		if (Constance.checkPortrait) {
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//		} else {
//			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
//		}
		
		radioOrientation
		.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			public void onCheckedChanged(RadioGroup rGroup,
					int checkedId) {
				clickRadio = rGroup.getCheckedRadioButtonId();
				// If the radiobutton that has changed in check state is
				// now checked...
				switch (clickRadio) {
				case R.id.btnActive:
					btnPortraint.setChecked(true);
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
					Constance.checkPortrait = 1;
					break;

				case R.id.btnDeactive:
					 btnLanscape.setChecked(true);
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
					Constance.checkPortrait = 2;
					break;
				case R.id.btnAutoRotate:
					 btnAutoRotate.setChecked(true);
					setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
					Constance.checkPortrait = 3;
					break;
				}
			}
		});
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if (pDialog != null) {
			pDialog.dismiss();
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		// save variable share preferent
		/* Create variable share preferences */
		SharedPreferences.Editor editor = preferences.edit();
		editor.putBoolean(Constance.CHECK_ON_OFF, Constance.bCheckOnOff);
		editor.putBoolean(Constance.CHECK_ORIENTATION, Constance.bOrientation);
		editor.putInt(Constance.CHECK_CHECKBOXNOTIFIATION,
				Constance.strCheckBoxNotifiation);
		editor.commit();
	}
	
}

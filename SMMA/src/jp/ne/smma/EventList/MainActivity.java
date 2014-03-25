package jp.ne.smma.EventList;

import jp.ne.smma.R;
import jp.ne.smma.Ultis.Constance;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.viewpagerindicator.IconPageIndicator;
import com.viewpagerindicator.PageIndicator;

public class MainActivity extends FragmentActivity {

	private PageIndicator mIndicator;
	private ImageView btnSetting;
	// preferences
	SharedPreferences preferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if(Constance.checkPortrait ){
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		else{
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
		PagerAdapter pageAdapter = new PagerAdapter(getSupportFragmentManager());
		ViewPager pager = (ViewPager) findViewById(R.id.myViewPager);
		pager.setAdapter(pageAdapter);
		pager.setOffscreenPageLimit(2);
		mIndicator = (IconPageIndicator) findViewById(R.id.indicator);
		mIndicator.setViewPager(pager);
		// get xml
		btnSetting = (ImageView) findViewById(R.id.btnSetting);
		btnSetting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(getApplicationContext(),
						SettingActivity.class);

				startActivity(intent);
			}
		});
		// get share prefrent
		// share Preference for setting
		preferences = PreferenceManager
				.getDefaultSharedPreferences(MainActivity.this);
		Constance.bCheckOnOff = preferences.getBoolean(Constance.CHECK_ON_OFF,
				true);
		Constance.bOrientation = preferences.getBoolean(
				Constance.CHECK_ORIENTATION, false);
		Constance.strCheckBoxNotifiation = preferences.getInt(
				Constance.CHECK_CHECKBOXNOTIFIATION, 0);

	}
	@Override
	public void onResume(){
		super.onResume();
		if(Constance.checkPortrait){
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			 Log.e("bbbbbbbbbb", "ffffffffffffffffff");
		}
		else{
			 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		}
	}


}

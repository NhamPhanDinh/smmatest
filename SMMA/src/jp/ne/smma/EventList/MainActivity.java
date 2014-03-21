package jp.ne.smma.EventList;

import jp.ne.smma.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.viewpagerindicator.IconPageIndicator;
import com.viewpagerindicator.PageIndicator;

public class MainActivity extends FragmentActivity {

	private PageIndicator mIndicator;
	private ImageView btnSetting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
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

	}

}

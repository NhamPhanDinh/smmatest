package jp.ne.smma.EventList;

import java.util.ArrayList;
import java.util.List;

import jp.ne.smma.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.viewpagerindicator.IconPagerAdapter;

class PagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

	private List<Fragment> fragments;
	protected static final int[] ICONS = new int[] {
			R.drawable.select_list_icon_pagerview,
			R.drawable.select_calendar_icon_pagerview,
			R.drawable.select_info_icon_pagerview };

	public PagerAdapter(FragmentManager fm) {
		super(fm);
		this.fragments = new ArrayList<Fragment>();
		fragments.add(new EventListFragment());
		fragments.add(new EventCalendarFragment());
		fragments.add(new AboutFragment());

	}

	@Override
	public Fragment getItem(int position) {
		return fragments.get(position);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	@Override
	public int getIconResId(int index) {
		return ICONS[index % ICONS.length];
	}
}

package jp.ne.smma.Ultis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
/**
 * Month info
 */
public class MonthInfo {

	private List<String> listDay = new ArrayList<String>();
	private HashMap<String, Integer> hm_DayOfMonth = new HashMap<String, Integer>();
	private List<String> listDayOfWeek = new ArrayList<String>();
	private int mYear;
	private int mMonth;
	public List<String> getListDay() {
		return listDay;
	}
	public void setListDay(List<String> listDay) {
		this.listDay = listDay;
	}
	public HashMap<String, Integer> getHm_DayOfMonth() {
		return hm_DayOfMonth;
	}
	public void setHm_DayOfMonth(HashMap<String, Integer> hm_DayOfMonth) {
		this.hm_DayOfMonth = hm_DayOfMonth;
	}
	public List<String> getListDayOfWeek() {
		return listDayOfWeek;
	}
	public void setListDayOfWeek(List<String> listDayOfWeek) {
		this.listDayOfWeek = listDayOfWeek;
	}
	public int getmYear() {
		return mYear;
	}
	public void setmYear(int mYear) {
		this.mYear = mYear;
	}
	public int getmMonth() {
		return mMonth;
	}
	public void setmMonth(int mMonth) {
		this.mMonth = mMonth;
	}
}

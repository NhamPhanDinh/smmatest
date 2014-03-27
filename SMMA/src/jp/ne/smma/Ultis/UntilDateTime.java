package jp.ne.smma.Ultis;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Until DateTime
 */
public class UntilDateTime {
	
	public static Date toDateFormat(String date) {
		Date d1 = null;
		
		java.text.DateFormat df = new java.text.SimpleDateFormat(
				"yyyy年MM月dd日"); //old M
		java.text.DateFormat df1 = new java.text.SimpleDateFormat(
				"yyyy/MM/dd");
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
		try {
			
			//df.setTimeZone(java.util.TimeZone.getDefault());
				java.util.Date dateF = df.parse(date);
		//df1.format(dateF);
		    d1 = dateF;
		} catch (java.text.ParseException e) {
		    e.printStackTrace();
		} 
		return d1;
	}
	
	public static long betweenDates(Date date, Date date1) {
		long temp = date1.getTime() - date.getTime();
		return (temp / (60 * 1000 * 60) / 24);
	}
	
	public static int getDate(Date date) {
		return date.getDate();
	}
	
	public static int getYear(Date date) {
		return date.getYear();
	}
	
	public static int getMonth(Date date) {
		return date.getMonth();
	}
}

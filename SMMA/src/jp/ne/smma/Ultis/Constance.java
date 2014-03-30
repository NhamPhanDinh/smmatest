package jp.ne.smma.Ultis;

import java.util.ArrayList;
import java.util.List;

import jp.ne.smma.R;
import jp.ne.smma.aboutsmma.DTO.ItemCalendar;

/**
 * Constance variable
 */
public class Constance {
	public static final String COLOR_ITEM_ABOUT = "COLOR_ITEM_ABOUT";
	public static final String COLOR_TEXT_INDEX_ABOUT = "COLOR_TEXT_ITEM_ABOUT";
	public static final String COLOR_INDEX_ABOUT = "COLOR_INDEX_ABOUT";
	public static final String COMPANY_NAME_ABOUT = "COMPANY_NAME_ABOUT";
	public static final String PALCE_ID_ABOUT = "PALCE_ID_ABOUT";
	public static final String LATITUDE_ABOUT = "LATITUDE_ABOUT";
	public static final String LONGITUDE_ABOUT = "LONGITUDE_ABOUT";

	public static boolean bCheckNetworkTimeOut = false;
	/* Variable check click on off setting */
	public static boolean bCheckOnOff = true; // default is ON
	/* Variable check click Orientation setting */
	public static boolean bOrientation = false; // default is not Orientation
												// (por)
	/* Variable check cout db when delete database */
	public static int countDatabaseNotification = 0;
	/* Variable save checkbox day notification database */
	public static int strCheckBoxNotifiation = 0; // 0-nothing, 1: 1day, 2:
													// 3day, 3: 1 week
	/* Variable save state */
	public static String CHECK_ON_OFF = "CHECK_ON_OFF";
	public static String CHECK_ORIENTATION = "CHECK_ORIENTATION";
	public static String CHECK_CHECKBOXNOTIFIATION = "CHECK_CHECKBOXNOTIFIATION";

	/* Url API */
	public static String url = "http://www.smma.jp/smma_api.php";

	/* Key success get json */
	public static String KEY_SUCCESS = "success";

	/* Key get path image form api about screen */
	public static String KEY_ABOUT_PATH_IMAGE = "ev_path_image";

	/* Key get company name form api about screen */
	public static String KEY_ABOUT_COMPANY_NAME = "ev_company_name";

	/* Key get company id form api company about screen */
	public static String KEY_ABOUT_CAT_ID = "ev_cat_id";

	/* Key get color form api company about screen */
	public static String KEY_ABOUT_COLOR = "ev_color";

	/* Key get latitude form api about screen */
	public static String KEY_ABOUT_LAT = "ev_lat";

	/* Key get longitude form api about screen */
	public static String KEY_ABOUT_LONG = "ev_lng";

	/* Key get palace id from api about screen */
	public static String KEY_ABOUT_PLACE = "ev_place_id";

	/* Key get data from api */
	public static String KEY_DATA = "data";

	public static String PATH_IMG_PALCE_DETAIL = "c_path_img";

	public static String CONTENT_PALCE_DETAIL = "c_content";

	public static String TEL_PALCE_DETAIL = "c_add_text_tel";

	public static String WEB_PALCE_DETAIL = "c_add_text_web";
	/* Key get calendar from api */
	public static String CALENDAR_EVENT_ID = "ev_id";
	public static String CALENDAR_KEY_ID = "ev_cat_id";
	public static String CALENDAR_KEY_COMPANYNAME = "ev_company_name";
	public static String CALENDAR_KEY_EVENTNAME = "ev_name";
	public static String CALENDAR_KEY_STARTDAY = "ev_date_from";
	public static String CALENDAR_KEY_ENDDAY = "ev_date_end";
	public static String CALENDAR_KEY_COLOR = "ev_color";

	// check orientation landscape
	public static boolean checkLanscape = false;

	// check orientation portrait
	public static boolean checkPortrait = true;
	/* Array list image company calendar */
	public static final Integer[] idCompany = new Integer[] { 10, 17, 18, 9, 11, 19, 12, 8,
			16, 15, 13, 14,55 };
	public static final String[] colorCodeCompany = new String[] { 
			"#009944",
			"#d72e8b",
			"#ea5504",
			"#000000",
			"#003f98",
			"#51318f",
			"#d7308c",
			"#014098", 
			"#9e774e",
			"#9f774e",
			"#ea5504", 
			"#9e774e", //
			"#008cd6" };

	public static final Integer[] imagesItemCompany = {
			R.drawable.bungakukan,
			R.drawable.tenmondai,
			R.drawable.rekishi_shiryokan,
			R.drawable.doubutsuen,
			R.drawable.media_take,
			R.drawable.bizyutukan,
			R.drawable.morino_hiroba,
			R.drawable.kagakukan, 
			R.drawable.hakubutsukan,//
			R.drawable.serizawa_bizyutsu_kougeikan,
			R.drawable.titeino_mori_musium,
			R.drawable.tohokudaigaku_hakubutsukan,
			R.drawable.kagakukan, };
	/* Data list */
	public static List<ItemCalendar> listItemCalendar = new ArrayList<ItemCalendar>();
	/* Click item */
	public static ArrayList<Integer> idCompanyClick = new ArrayList<Integer>();

}

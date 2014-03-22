package jp.ne.smma.Ultis;


/**
 * Constance variable
 */
public class Constance {
	public static final String COLOR_ITEM_ABOUT = "COLOR_ITEM_ABOUT";
	public static final String COLOR_TEXT_INDEX_ABOUT = "COLOR_TEXT_ITEM_ABOUT";
	public static final String COLOR_INDEX_ABOUT = "COLOR_INDEX_ABOUT";
	
	public static boolean bCheckNetworkTimeOut=false;
	/* Variable check click on off setting */
	public static boolean bCheckOnOff=true; //default is ON
	/* Variable check click Orientation setting */
	public static boolean bOrientation=false; //default is not Orientation (por)
	/* Variable check cout db when delete database */
	public static int countDatabaseNotification=0;
	/* Variable save checkbox day notification database */
	public static int strCheckBoxNotifiation=0; //0-nothing, 1: 1day, 2: 3day, 3: 1 week
}
